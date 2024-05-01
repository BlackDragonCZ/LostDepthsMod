package cz.blackdragoncz.lostdepths.block.power.entity;

import com.mojang.logging.LogUtils;
import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyContainerBlockEntity;
import cz.blackdragoncz.lostdepths.energy.PowerCable;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import cz.blackdragoncz.lostdepths.world.inventory.NurostarGeneratorMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@NothingNullByDefault
public class NurostarGeneratorBlockEntity extends BaseEnergyContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    private static final ItemStack FIRST_SLOT = new ItemStack(LostdepthsModItems.INFUSED_CRYSTAL.get());
    private static final ItemStack SECOND_SLOT = new ItemStack(Items.REDSTONE);
    private static final ItemStack THIRD_WASTE_SLOT = new ItemStack(LostdepthsModItems.CELESTIAL_REDSTONE.get());

    private static final int MaxCapacity = 100000;
    private static final int MaxExtract = 200;
    private static final int EnergyPerConsume = 63000;
    private static final int ConsumeTickTime = 2;
    private static final int EnergyPerWaste = 10000;

    private int tickCounter = 0;
    private int usedEnergy = 0;

    public NurostarGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get(), pos, state);
    }

    public boolean updateTick() {
        tickCounter++;

        if (tickCounter == ConsumeTickTime) {
            tickCounter = 0;
            return true;
        }

        return false;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items);
        if (compound.contains("currentWaste"))
            this.usedEnergy = compound.getInt("usedEnergy");
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);

        ContainerHelper.saveAllItems(compound, this.items);
        compound.putInt("usedEnergy", this.usedEnergy);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, MaxCapacity, EnergyPerConsume, MaxExtract);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, NurostarGeneratorBlockEntity blockEntity) {
        if (level.isClientSide) {
            return;
        }

        if (blockEntity.updateTick()) {
            ItemStack firstSlot = blockEntity.getItems().get(0);
            ItemStack secondSlot = blockEntity.getItems().get(1);
            ItemStack wasteSlot = blockEntity.getItems().get(2);

            if (!firstSlot.isEmpty() && !secondSlot.isEmpty() && blockEntity.energyStorage.receiveEnergy(EnergyPerConsume, true) == EnergyPerConsume) {
                firstSlot.setCount(firstSlot.getCount() - 1);
                secondSlot.setCount(secondSlot.getCount() - 1);
                blockEntity.setItem(0, firstSlot);
                blockEntity.setItem(1, secondSlot);
                blockEntity.energyStorage.receiveEnergy(EnergyPerConsume, false);
            }

            if (wasteSlot.getCount() < wasteSlot.getMaxStackSize()) {
                if (blockEntity.usedEnergy >= EnergyPerWaste) {
                    blockEntity.usedEnergy -= EnergyPerWaste;

                    if (wasteSlot.getItem() != THIRD_WASTE_SLOT.getItem()) {
                        blockEntity.setItem(2, new ItemStack(THIRD_WASTE_SLOT.getItem(), 1));
                    }
                    else
                    {
                        wasteSlot.setCount(wasteSlot.getCount() + 1);
                        blockEntity.setItem(2, wasteSlot);
                    }
                }
            }
        }

        List<BlockEntity> energyEntities = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity entity = level.getBlockEntity(blockPos.relative(direction));

            if (entity == null)
                continue;

            if (entity instanceof NurostarCableBlockEntity cableEntity) {
                PowerCable cable = cableEntity.getThisCable();

                if (cable == null)
                    continue;

                cable.collectEnergy(energyEntities);
            } else if (!(entity instanceof NurostarGeneratorBlockEntity)) {
                LazyOptional<IEnergyStorage> energyOpt = entity.getCapability(ForgeCapabilities.ENERGY);

                if (energyOpt.isPresent()) {
                    IEnergyStorage energyStorage = energyOpt.orElse(null);

                    if (energyStorage.canReceive() && !energyEntities.contains(entity)) {
                        energyEntities.add(entity);
                    }
                }
            }
        }

        energyEntities.removeIf((entity -> entity instanceof NurostarGeneratorBlockEntity));

        if (energyEntities.isEmpty())
            return;

        int energyReceiversCount = energyEntities.size();

        int energyAllocated = blockEntity.energyStorage.extractEnergy(MaxExtract, true);
        int energyPerReceiver = energyAllocated / energyReceiversCount;
        int consumedEnergy = 0;

        for (BlockEntity energyEntity : energyEntities) {
            if (energyEntity instanceof NurostarGeneratorBlockEntity) {
                continue;
            }

            LazyOptional<IEnergyStorage> energyOpt = energyEntity.getCapability(ForgeCapabilities.ENERGY);

            if (energyOpt.isPresent()) {
                IEnergyStorage energyStorage = energyOpt.orElse(null);
                consumedEnergy += energyStorage.receiveEnergy(energyPerReceiver, false);
            }
        }

        if (consumedEnergy == 0)
            return;

        blockEntity.usedEnergy += consumedEnergy;
        blockEntity.energyStorage.extractEnergy(consumedEnergy, false);
    }

    @Override
    public Component getDisplayName() {
        return getDefaultName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Nurostar Generator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new NurostarGeneratorMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return new int[] { 0, 1 };
        }
        return new int[] { 2 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack stack, @Nullable Direction dir) {
        if (i == 0 && stack.getItem() == FIRST_SLOT.getItem() && dir == Direction.UP)
            return true;
        if (i == 1 && stack.getItem() == SECOND_SLOT.getItem() && dir == Direction.UP)
            return true;

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction dir) {
        return stack.getItem() == THIRD_WASTE_SLOT.getItem();
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack stack) {
        if (i == 0 && stack.getItem() == FIRST_SLOT.getItem())
            return true;
        if (i == 1 && stack.getItem() == SECOND_SLOT.getItem())
            return true;

        return false;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @javax.annotation.Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            if (facing == Direction.DOWN)
                return handlers[1].cast();
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlers)
            handler.invalidate();
    }
}
