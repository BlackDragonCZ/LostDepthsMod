package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyContainerBlockEntity;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.CrystalDefinition;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.DepletionType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.OreDefinition;
import cz.blackdragoncz.lostdepths.procedures.OmniPickaxeBlockDestroyedWithToolProcedure;
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
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import java.util.ArrayList;
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
    /** Energy bar resyncs only once stored has moved this far. */
    private static final int SYNC_THRESHOLD = MAX_CAPACITY / 100;

    private static final int VANILLA_ENERGY_PER_TICK = 70;  // FE/t for vanilla ores
    private static final int VANILLA_INTERVAL = 100;        // 5s
    private static final int MODDED_INTERVAL = 300;         // 15s - lostdepths ores, FE/t comes from the ore tier

    // Status constants. 0-1 render red, 2-3 orange, 4 green.
    public static final int STATUS_REDSTONE = 0;  // switched off by a redstone signal
    public static final int STATUS_NO_ORE = 1;    // nothing valid underneath
    public static final int STATUS_MISSING = 2;   // no pickaxe / wrong tier / no solution / no energy
    public static final int STATUS_FULL = 3;      // output slots cannot take the next yield
    public static final int STATUS_ACTIVE = 4;    // working

    private NonNullList<ItemStack> items = NonNullList.withSize(TOTAL_SLOTS, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    private int tickCounter = 0;
    private int machineStatus = STATUS_NO_ORE;

    public final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> machineStatus;
                case 1 -> tickCounter;
                case 2 -> getMaxProgress();
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> machineStatus = value;
                case 1 -> tickCounter = value;
            }
        }

        @Override
        public int getCount() { return 3; }
    };

    public ResourceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get(), pos, state);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        // maxExtract 0: consumer only. Otherwise two adjacent extractors shuffle energy between each other.
        return new SyncedEnergyStorage(this, MAX_CAPACITY, MAX_TRANSFER, 0) {
            private int lastSynced = -1;

            @Override
            protected void updateClients() {
                // Was resyncing the whole BE every tick while running; 1% steps are finer than the bar anyway.
                int now = getEnergyStored();
                boolean edge = now == 0 || now == getMaxEnergyStored();
                if (lastSynced >= 0 && !edge && Math.abs(now - lastSynced) < SYNC_THRESHOLD) return;
                lastSynced = now;
                super.updateClients();
            }
        };
    }

    public int getProgress() { return tickCounter; }

    public int getMaxProgress() {
        Target target = resolveTarget();
        return target == null ? MODDED_INTERVAL : target.interval();
    }

    public int getOutputSignal() {
        int filled = 0;
        for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END; i++)
            if (!items.get(i).isEmpty()) filled++;
        return (int) ((filled / 4.0f) * 15);
    }

    // --- What is underneath ---

    /** Resolved via the registries, not description-id matching, so ore_empty and tree blocks are refused. */
    private record Target(@Nullable OreDefinition ore, @Nullable CrystalDefinition crystal, boolean vanilla,
                          BlockState state, int energyPerTick, int interval) {}

    @Nullable
    private Target resolveTarget() {
        if (level == null) return null;
        BlockState below = level.getBlockState(getBlockPos().below());
        Block block = below.getBlock();

        OreDefinition ore = LostdepthsModOres.findByBlock(block);
        if (ore != null)
            return new Target(ore, null, false, below, ore.minTier().energyPerTick(), MODDED_INTERVAL);

        CrystalDefinition crystal = LostdepthsModOres.findCrystal(block);
        if (crystal != null)
            return new Target(null, crystal, false, below, crystal.minTier().energyPerTick(), MODDED_INTERVAL);

        if (isVanillaOre(below))
            return new Target(null, null, true, below, VANILLA_ENERGY_PER_TICK, VANILLA_INTERVAL);

        return null;
    }

    private boolean isVanillaOre(BlockState state) {
        return state.is(BlockTags.GOLD_ORES) || state.is(BlockTags.IRON_ORES)
                || state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.REDSTONE_ORES)
                || state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.COPPER_ORES)
                || state.is(BlockTags.COAL_ORES) || state.is(BlockTags.EMERALD_ORES);
    }

    /** Tier for mod ores, harvest level for vanilla ones. */
    private boolean toolAccepts(Target target, ItemStack pickaxe) {
        if (pickaxe.isEmpty() || !(pickaxe.getItem() instanceof PickaxeItem)) return false;
        if (target.crystal() != null) return target.crystal().canMine(pickaxe.getItem());
        if (target.ore() != null) return target.ore().canMine(pickaxe.getItem());
        return pickaxe.isCorrectToolForDrops(target.state());
    }

    private int findSolutionSlot(Item requiredSolution) {
        for (int i = SLOT_SOLUTION_START; i <= SLOT_SOLUTION_END; i++) {
            if (items.get(i).getItem() == requiredSolution) return i;
        }
        return -1;
    }

    // --- Energy ---

    private static void pullEnergy(Level level, BlockPos pos, ResourceExtractorBlockEntity be) {
        if (be.energyStorage.receiveEnergy(MAX_TRANSFER, true) <= 0) return;

        for (Direction dir : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(pos.relative(dir));
            if (neighbor == null) continue;

            neighbor.getCapability(ForgeCapabilities.ENERGY, dir.getOpposite()).ifPresent(storage -> {
                // canExtract == "is a provider": mod consumers report false, so they are never drained.
                if (!storage.canExtract()) return;
                int room = be.energyStorage.receiveEnergy(MAX_TRANSFER, true);
                if (room <= 0) return;
                int moved = storage.extractEnergy(room, false);
                if (moved > 0) be.energyStorage.receiveEnergy(moved, false);
            });
        }
    }

    // --- Tick ---

    public static void serverTick(Level level, BlockPos pos, BlockState state, ResourceExtractorBlockEntity be) {
        if (level.isClientSide) return;

        pullEnergy(level, pos, be);

        if (level.hasNeighborSignal(pos)) {
            be.setStatus(STATUS_REDSTONE, true);
            return;
        }

        Target target = be.resolveTarget();
        if (target == null) {
            be.setStatus(STATUS_NO_ORE, true);
            return;
        }

        ItemStack pickaxe = be.items.get(SLOT_PICKAXE);
        if (!be.toolAccepts(target, pickaxe)) {
            be.setStatus(STATUS_MISSING, true);
            return;
        }

        if (be.energyStorage.getEnergyStored() < target.energyPerTick()) {
            // Not enough power: progress is cancelled outright rather than paused.
            be.setStatus(STATUS_MISSING, true);
            return;
        }

        // Dormant solution ore: charge it first, costs one solution, cycle restarts on the live ore.
        OreDefinition ore = target.ore();
        if (ore != null && ore.requiresSolution() && LostdepthsModOres.isUnpowered(ore, target.state().getBlock())) {
            int solutionSlot = be.findSolutionSlot(ore.activationSolution().get());
            if (solutionSlot == -1) {
                be.setStatus(STATUS_MISSING, true);
                return;
            }
            be.items.get(solutionSlot).shrink(1);
            level.setBlock(pos.below(), ore.oreBlock().get().defaultBlockState(), 3);
            be.energyStorage.extractEnergy(target.energyPerTick(), false);
            be.setStatus(STATUS_ACTIVE, true);
            be.setChanged();
            return;
        }

        if (!be.hasOutputRoom()) {
            // Hold progress: frees up mid-cycle and finishes rather than restarting.
            be.setStatus(STATUS_FULL, false);
            return;
        }

        // Validate the finishing tick before spending, so a blocked machine burns no energy.
        boolean completes = be.tickCounter + 1 >= target.interval();
        List<ItemStack> drops = null;
        if (completes) {
            drops = be.rollDrops(target, pickaxe);
            if (!be.insertAll(drops, true)) {
                be.setStatus(STATUS_FULL, false);
                return;
            }
        }

        be.setStatus(STATUS_ACTIVE, false);
        be.energyStorage.extractEnergy(target.energyPerTick(), false);
        be.tickCounter++;

        if (!completes) return;

        be.tickCounter = 0;
        be.insertAll(drops, false);
        be.onCycleComplete(target, pickaxe);
        be.setChanged();
    }

    private void setStatus(int status, boolean resetProgress) {
        this.machineStatus = status;
        if (resetProgress) this.tickCounter = 0;
    }

    /** Ore is never consumed; the only block change is a re-chargeable rolling 50/50 back to dormant. */
    private void onCycleComplete(Target target, ItemStack pickaxe) {
        if (pickaxe.isDamageableItem()) {
            CompoundTag tag = pickaxe.getTag();
            boolean unbreakable = tag != null && tag.getBoolean("Unbreakable");
            if (!unbreakable) {
                pickaxe.setDamageValue(pickaxe.getDamageValue() + 1);
                if (pickaxe.getDamageValue() >= pickaxe.getMaxDamage()) items.set(SLOT_PICKAXE, ItemStack.EMPTY);
            }
        }

        OreDefinition ore = target.ore();
        if (level != null && ore != null && ore.depletionType() == DepletionType.CHANCE_DEACTIVATE
                && ore.unpoweredBlock() != null && level.getRandom().nextBoolean()) {
            level.setBlock(getBlockPos().below(), ore.unpoweredBlock().get().defaultBlockState(), 3);
        }
    }

    // --- Yield ---

    private List<ItemStack> rollDrops(Target target, ItemStack pickaxe) {
        if (target.crystal() != null) {
            CrystalDefinition crystal = target.crystal();
            return List.of(new ItemStack(crystal.dropItem().get(), crystal.dropCount()));
        }

        if (target.ore() != null) {
            int count = target.ore().getDropCount(pickaxe.getItem());
            return count > 0 ? List.of(new ItemStack(target.ore().dropItem().get(), count)) : List.of();
        }

        // Loot table so fortune and other mods' tweaks apply.
        ServerLevel serverLevel = (ServerLevel) level;
        BlockPos orePos = getBlockPos().below();
        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(orePos))
                .withParameter(LootContextParams.TOOL, pickaxe)
                .withParameter(LootContextParams.BLOCK_STATE, target.state())
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, serverLevel.getBlockEntity(orePos));

        List<ItemStack> drops = new ArrayList<>(target.state().getDrops(builder));

        // Omni Pickaxe celestial bonus, same roll as hand-mining.
        if (pickaxe.getItem() == LostdepthsModItems.OMNI_PICKAXE.get()) {
            Item celestial = OmniPickaxeBlockDestroyedWithToolProcedure.getDropForOre(target.state().getBlock());
            if (celestial != null) {
                int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, pickaxe);
                int bonus = OmniPickaxeBlockDestroyedWithToolProcedure.rollBonusCount(serverLevel.getRandom(), fortune);
                if (bonus > 0) drops.add(new ItemStack(celestial, bonus));
            }
        }

        return drops;
    }

    // --- Output handling ---

    /** Cheap "any room at all" test for the full-storage status. */
    private boolean hasOutputRoom() {
        for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END; i++) {
            ItemStack slot = items.get(i);
            if (slot.isEmpty() || slot.getCount() < slot.getMaxStackSize()) return true;
        }
        return false;
    }

    /** All or nothing, so a yield is never partially voided. */
    private boolean insertAll(List<ItemStack> drops, boolean simulate) {
        if (drops.isEmpty()) return true;

        int[] counts = new int[TOTAL_SLOTS];
        for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END; i++) counts[i] = items.get(i).getCount();

        for (ItemStack drop : drops) {
            int remaining = drop.getCount();
            for (int i = SLOT_OUTPUT_START; i <= SLOT_OUTPUT_END && remaining > 0; i++) {
                ItemStack slot = items.get(i);
                boolean empty = slot.isEmpty() && counts[i] == 0;
                if (!empty && !(ItemStack.isSameItemSameTags(slot, drop) && counts[i] < slot.getMaxStackSize())) continue;

                int limit = empty ? drop.getMaxStackSize() : slot.getMaxStackSize();
                int room = limit - counts[i];
                int moved = Math.min(room, remaining);
                if (moved <= 0) continue;

                counts[i] += moved;
                remaining -= moved;

                if (!simulate) {
                    if (slot.isEmpty()) {
                        ItemStack placed = drop.copy();
                        placed.setCount(moved);
                        items.set(i, placed);
                    } else {
                        slot.grow(moved);
                    }
                }
            }
            if (remaining > 0) return false;
        }
        return true;
    }

    // --- Container ---

    @Override
    public Component getDisplayName() { return getDefaultName(); }

    @Override
    protected Component getDefaultName() { return Component.translatable("block.lostdepths.resource_extractor"); }

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
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
        tag.putInt("tickCounter", tickCounter);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) return new int[]{0, 1, 2, 3};
        return new int[]{4, 5, 6, 7};
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack stack, @Nullable Direction dir) {
        return canPlaceItem(i, stack);
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
