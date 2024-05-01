package cz.blackdragoncz.lostdepths.block.entity.base;

import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.util.IEnergyAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;

import javax.annotation.Nullable;

public abstract class BaseEnergyContainerBlockEntity extends BaseContainerBlockEntity implements IEnergyAccessor, WorldlyContainer {

    protected SyncedEnergyStorage energyStorage;

    public BaseEnergyContainerBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.energyStorage = createEnergyStorage();
    }

    protected abstract SyncedEnergyStorage createEnergyStorage();

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.get("energyStorage") instanceof IntTag intTag)
            energyStorage.deserializeNBT(intTag);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("energyStorage", energyStorage.serializeNBT());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && capability == ForgeCapabilities.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    protected abstract NonNullList<ItemStack> getItems();

    protected abstract void setItems(NonNullList<ItemStack> pItemStacks);

    @Override
    public boolean isEmpty() {
        return this.getItems().stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return this.getItems().get(pIndex);
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        ItemStack itemstack = ContainerHelper.removeItem(this.getItems(), pIndex, pCount);
        if (!itemstack.isEmpty()) {
            this.setChanged();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return ContainerHelper.takeItem(this.getItems(), pIndex);
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        this.getItems().set(pIndex, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }

        this.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.getItems().clear();
    }

}
