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

	public InfusedDisplayPlateBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.INFUSED_DISPLAY_PLATE.get(), pos, state);
	}

	public ItemStack getHeldItem() {
		return this.heldItem;
	}

	public void setHeldItem(ItemStack stack) {
		this.heldItem = stack;
		this.setChanged();
		if (this.level != null && !this.level.isClientSide) {
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Item", this.heldItem.serializeNBT());
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.heldItem = ItemStack.of(tag.getCompound("Item"));
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
