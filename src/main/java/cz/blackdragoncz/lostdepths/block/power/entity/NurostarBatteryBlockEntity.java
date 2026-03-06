package cz.blackdragoncz.lostdepths.block.power.entity;

import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyContainerBlockEntity;
import cz.blackdragoncz.lostdepths.energy.PowerCable;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.world.inventory.NurostarBatteryMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NurostarBatteryBlockEntity extends BaseEnergyContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(2, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    // Slot 0 = charge input (battery fills this item with energy)
    // Slot 1 = drain output (battery drains energy from this item)

    private static final int TRANSFER_RATE = 800; // Same as generator MaxExtract
    private static final int CHARGE_RATE = 400; // Energy per tick to/from items

    private final int maxCapacity;
    private final boolean isLarge;

    public NurostarBatteryBlockEntity(BlockPos pos, BlockState state) {
        this(LostdepthsModBlockEntities.NUROSTAR_BATTERY.get(), pos, state, 100000, false);
    }

    protected NurostarBatteryBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, int capacity, boolean large) {
        super(type, pos, state);
        this.maxCapacity = capacity;
        this.isLarge = large;
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, maxCapacity, TRANSFER_RATE, TRANSFER_RATE);
    }

    public static NurostarBatteryBlockEntity createLarge(BlockPos pos, BlockState state) {
        return new NurostarBatteryBlockEntity(LostdepthsModBlockEntities.NUROSTAR_LARGE_BATTERY.get(), pos, state, 450000, true);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, NurostarBatteryBlockEntity be) {
        if (level.isClientSide) return;

        boolean changed = false;

        // Slot 0: charge input — transfer energy FROM battery TO item
        ItemStack chargeItem = be.items.get(0);
        if (!chargeItem.isEmpty()) {
            LazyOptional<IEnergyStorage> cap = chargeItem.getCapability(ForgeCapabilities.ENERGY);
            if (cap.isPresent()) {
                IEnergyStorage itemEnergy = cap.orElseThrow(IllegalStateException::new);
                if (itemEnergy.canReceive()) {
                    int toTransfer = be.energyStorage.extractEnergy(CHARGE_RATE, true);
                    int accepted = itemEnergy.receiveEnergy(toTransfer, false);
                    if (accepted > 0) {
                        be.energyStorage.extractEnergy(accepted, false);
                        changed = true;
                    }
                }
            }
        }

        // Slot 1: drain output — transfer energy FROM item TO battery
        ItemStack drainItem = be.items.get(1);
        if (!drainItem.isEmpty()) {
            LazyOptional<IEnergyStorage> cap = drainItem.getCapability(ForgeCapabilities.ENERGY);
            if (cap.isPresent()) {
                IEnergyStorage itemEnergy = cap.orElseThrow(IllegalStateException::new);
                if (itemEnergy.canExtract()) {
                    int toTransfer = be.energyStorage.receiveEnergy(CHARGE_RATE, true);
                    int extracted = itemEnergy.extractEnergy(toTransfer, false);
                    if (extracted > 0) {
                        be.energyStorage.receiveEnergy(extracted, false);
                        changed = true;
                    }
                }
            }
        }

        // Distribute energy to adjacent machines via cables
        List<BlockEntity> energyEntities = new ArrayList<>();

        for (Direction direction : Direction.values()) {
            BlockEntity entity = level.getBlockEntity(pos.relative(direction));
            if (entity == null) continue;

            if (entity instanceof NurostarCableBlockEntity cableEntity) {
                PowerCable cable = cableEntity.getThisCable();
                if (cable != null) {
                    cable.collectEnergy(energyEntities);
                }
            } else if (!(entity instanceof NurostarGeneratorBlockEntity) && !(entity instanceof NurostarBatteryBlockEntity)) {
                LazyOptional<IEnergyStorage> energyOpt = entity.getCapability(ForgeCapabilities.ENERGY);
                if (energyOpt.isPresent()) {
                    IEnergyStorage es = energyOpt.orElse(null);
                    if (es != null && es.canReceive() && !energyEntities.contains(entity)) {
                        energyEntities.add(entity);
                    }
                }
            }
        }

        energyEntities.removeIf(e -> e instanceof NurostarGeneratorBlockEntity || e instanceof NurostarBatteryBlockEntity);

        if (!energyEntities.isEmpty()) {
            int energyAllocated = be.energyStorage.extractEnergy(TRANSFER_RATE, true);
            int energyPerReceiver = energyAllocated / energyEntities.size();
            int consumedEnergy = 0;

            for (BlockEntity energyEntity : energyEntities) {
                LazyOptional<IEnergyStorage> energyOpt = energyEntity.getCapability(ForgeCapabilities.ENERGY);
                if (energyOpt.isPresent()) {
                    IEnergyStorage es = energyOpt.orElse(null);
                    if (es != null) {
                        consumedEnergy += es.receiveEnergy(energyPerReceiver, false);
                    }
                }
            }

            if (consumedEnergy > 0) {
                be.energyStorage.extractEnergy(consumedEnergy, false);
                changed = true;
            }
        }

        if (changed) {
            be.setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, this.items);
    }

    @Override
    public Component getDisplayName() {
        return getDefaultName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal(isLarge ? "Nurostar Large Battery" : "Nurostar Battery");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new NurostarBatteryMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getContainerSize() {
        return 2;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[]{0, 1};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction dir) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER) {
            if (facing == Direction.UP) return handlers[0].cast();
            if (facing == Direction.DOWN) return handlers[1].cast();
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
