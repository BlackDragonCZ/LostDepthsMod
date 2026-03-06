package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyContainerBlockEntity;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.world.inventory.ResourceExtractorMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ResourceExtractorBlockEntity extends BaseEnergyContainerBlockEntity {

    // Slot 0 = Pickaxe
    // Slots 1-3 = Solution inputs
    // Slots 4-7 = Output
    public static final int SLOT_PICKAXE = 0;
    public static final int SLOT_SOLUTION_START = 1;
    public static final int SLOT_SOLUTION_END = 3;
    public static final int SLOT_OUTPUT_START = 4;
    public static final int SLOT_OUTPUT_END = 7;
    private static final int TOTAL_SLOTS = 8;

    private static final int MAX_CAPACITY = 50000;
    private static final int MAX_TRANSFER = 800;
    private static final int ENERGY_PER_OP = 500;
    private static final int MODDED_INTERVAL = 300; // 15s
    private static final int VANILLA_INTERVAL = 100; // 5s

    private NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    private int tickCounter = 0;
    private boolean redstoneActive = true;

    public ResourceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get(), pos, state);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, MAX_CAPACITY, MAX_TRANSFER, 0);
    }

    public void setRedstoneActive(boolean active) { this.redstoneActive = active; }
    public int getProgress() { return tickCounter; }

    public int getMaxProgress() {
        BlockState below = getOreBelow();
        if (below == null) return MODDED_INTERVAL;
        return isVanillaOre(below) ? VANILLA_INTERVAL : MODDED_INTERVAL;
    }

    public int getOutputSignal() {
        int filled = 0;
        for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END; i++)
            if (!items.get(i).isEmpty()) filled++;
        return (int) ((filled / 4.0f) * 15);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResourceExtractorBlockEntity be) {
        if (level.isClientSide || !be.redstoneActive) return;

        BlockState oreState = be.getOreBelow();
        if (oreState == null) return;

        ItemStack pickaxe = be.items.get(SLOT_PICKAXE);
        if (pickaxe.isEmpty() || !(pickaxe.getItem() instanceof PickaxeItem)) return;
        if (be.energyStorage.getEnergyStored() < ENERGY_PER_OP) return;

        int interval = be.isVanillaOre(oreState) ? VANILLA_INTERVAL : MODDED_INTERVAL;
        be.tickCounter++;
        if (be.tickCounter < interval) return;
        be.tickCounter = 0;

        // Simulate mining loot
        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos orePos = pos.below();

        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(orePos))
                .withParameter(LootContextParams.TOOL, pickaxe)
                .withParameter(LootContextParams.BLOCK_STATE, oreState)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(orePos));

        List<ItemStack> drops = oreState.getDrops(builder);

        boolean inserted = false;
        for (ItemStack drop : drops) {
            ItemStack remaining = drop.copy();
            for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END && !remaining.isEmpty(); i++) {
                ItemStack slot = be.items.get(i);
                if (slot.isEmpty()) {
                    be.items.set(i, remaining.copy());
                    remaining = ItemStack.EMPTY;
                    inserted = true;
                } else if (ItemStack.isSameItemSameTags(slot, remaining) && slot.getCount() < slot.getMaxStackSize()) {
                    int toAdd = Math.min(slot.getMaxStackSize() - slot.getCount(), remaining.getCount());
                    slot.grow(toAdd);
                    remaining.shrink(toAdd);
                    inserted = true;
                }
            }
        }

        if (inserted) {
            be.energyStorage.extractEnergy(ENERGY_PER_OP, false);

            // Damage pickaxe if damageable and not unbreakable
            if (pickaxe.isDamageableItem()) {
                CompoundTag tag = pickaxe.getTag();
                boolean unbreakable = tag != null && tag.getBoolean("Unbreakable");
                if (!unbreakable) {
                    pickaxe.setDamageValue(pickaxe.getDamageValue() + 1);
                    if (pickaxe.getDamageValue() >= pickaxe.getMaxDamage()) {
                        be.items.set(SLOT_PICKAXE, ItemStack.EMPTY);
                    }
                }
            }
            be.setChanged();
        }
    }

    @Nullable
    private BlockState getOreBelow() {
        if (level == null) return null;
        BlockState state = level.getBlockState(getBlockPos().below());
        if (isVanillaOre(state) || isModdedOre(state)) return state;
        return null;
    }

    private boolean isVanillaOre(BlockState state) {
        return state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.IRON_ORES)
                || state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.COAL_ORES) || state.is(BlockTags.EMERALD_ORES);
    }

    private boolean isModdedOre(BlockState state) {
        String id = state.getBlock().getDescriptionId();
        return id.contains("lostdepths") && (id.contains("ore") || id.contains("crystal"));
    }

    // --- Container ---

    @Override
    public Component getDisplayName() { return getDefaultName(); }

    @Override
    protected Component getDefaultName() { return Component.literal("Resource Extractor"); }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new ResourceExtractorMenu(id, inv, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getContainerSize() { return TOTAL_SLOTS; }

    @Override
    protected NonNullList<ItemStack> getItems() { return items; }

    @Override
    protected void setItems(NonNullList<ItemStack> items) { this.items = items; }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
        this.tickCounter = tag.getInt("tickCounter");
        this.redstoneActive = !tag.contains("redstoneActive") || tag.getBoolean("redstoneActive");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("tickCounter", tickCounter);
        tag.putBoolean("redstoneActive", redstoneActive);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) return new int[]{0, 1, 2, 3};
        return new int[]{4, 5, 6, 7};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack stack, @Nullable Direction dir) {
        if (i == SLOT_PICKAXE) return stack.getItem() instanceof PickaxeItem;
        return i >= SLOT_SOLUTION_START && i <= SLOT_SOLUTION_END;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack stack, Direction dir) {
        return i >= SLOT_OUTPUT_START;
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack stack) {
        if (i == SLOT_PICKAXE) return stack.getItem() instanceof PickaxeItem;
        return i >= SLOT_SOLUTION_START && i <= SLOT_SOLUTION_END;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction facing) {
        if (!this.remove && facing != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP) return handlers[0].cast();
            if (facing == Direction.DOWN) return handlers[1].cast();
            return handlers[2].cast();
        }
        return super.getCapability(cap, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (var h : handlers) h.invalidate();
    }
}
