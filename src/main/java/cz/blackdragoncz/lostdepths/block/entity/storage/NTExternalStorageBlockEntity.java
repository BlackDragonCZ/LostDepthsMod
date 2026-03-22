package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NTExternalStorageBlockEntity extends BlockEntity implements StorageNetworkNode {
	public NTExternalStorageBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_EXTERNAL_STORAGE.get(), pos, state);
	}
}
