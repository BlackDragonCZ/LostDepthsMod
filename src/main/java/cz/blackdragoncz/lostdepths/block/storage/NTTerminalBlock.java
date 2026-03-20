package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTTerminalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class NTTerminalBlock extends Block implements EntityBlock {
	public NTTerminalBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTTerminalBlockEntity(pos, state);
	}
}
