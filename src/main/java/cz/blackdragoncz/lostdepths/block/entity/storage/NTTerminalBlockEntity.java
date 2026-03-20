package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NTTerminalBlockEntity extends BlockEntity {
	public NTTerminalBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_TERMINAL.get(), pos, state);
	}
}
