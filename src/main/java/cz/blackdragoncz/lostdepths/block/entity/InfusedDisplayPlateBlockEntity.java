package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedDisplayPlateBlockEntity extends BlockEntity {

	private ItemStack heldItem = ItemStack.EMPTY;
	// 0-7 like a vanilla item frame; maps only use every second step
	private int rotation;

	public InfusedDisplayPlateBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.INFUSED_DISPLAY_PLATE.get(), pos, state);
	}

	public ItemStack getHeldItem() {
		return this.heldItem;
	}

	public int getRotation() {
		return this.rotation;
	}

	public void setHeldItem(ItemStack stack) {
		this.heldItem = stack;
		if (stack.isEmpty())
			this.rotation = 0;
		this.setChanged();
		if (this.level != null && !this.level.isClientSide) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
			DisplayPlateMapSync.onHeldItemChanged(this.level, this);
		}
	}

	public void setRotation(int value) {
		this.rotation = value & 7;
		this.setChanged();
		if (this.level != null && !this.level.isClientSide)
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Item", this.heldItem.serializeNBT());
		tag.putByte("Rotation", (byte) this.rotation);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.heldItem = ItemStack.of(tag.getCompound("Item"));
		this.rotation = tag.getByte("Rotation") & 7;
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		this.saveAdditional(tag);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}
}
