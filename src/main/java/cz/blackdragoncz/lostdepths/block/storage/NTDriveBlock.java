package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTDriveBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class NTDriveBlock extends Block implements EntityBlock {
	public NTDriveBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTDriveBlockEntity(pos, state);
	}
}
