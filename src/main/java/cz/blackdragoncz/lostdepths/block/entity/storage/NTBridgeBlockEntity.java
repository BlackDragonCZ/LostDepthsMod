package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Internal NT bridge — separates two controller segments into independent networks.
 * Configurable sharing: items, energy, crafting tasks.
 * When placed between two controllers, each controller remains master of its segment.
 * Without a bridge, a second controller becomes a slave (hot standby).
 */
public class NTBridgeBlockEntity extends BlockEntity implements StorageNetworkNode {
	private boolean shareItems = true;
	private boolean shareEnergy = false;
	private boolean shareCrafting = false;

	public NTBridgeBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_BRIDGE.get(), pos, state);
	}

	protected NTBridgeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public boolean isShareItems() {
		return shareItems;
	}

	public void setShareItems(boolean shareItems) {
		this.shareItems = shareItems;
		setChanged();
	}

	public boolean isShareEnergy() {
		return shareEnergy;
	}

	public void setShareEnergy(boolean shareEnergy) {
		this.shareEnergy = shareEnergy;
		setChanged();
	}

	public boolean isShareCrafting() {
		return shareCrafting;
	}

	public void setShareCrafting(boolean shareCrafting) {
		this.shareCrafting = shareCrafting;
		setChanged();
	}

	@Override
	public int getEnergyPerTick() {
		return 3;
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		shareItems = tag.getBoolean("ShareItems");
		shareEnergy = tag.getBoolean("ShareEnergy");
		shareCrafting = tag.getBoolean("ShareCrafting");
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putBoolean("ShareItems", shareItems);
		tag.putBoolean("ShareEnergy", shareEnergy);
		tag.putBoolean("ShareCrafting", shareCrafting);
	}
}
