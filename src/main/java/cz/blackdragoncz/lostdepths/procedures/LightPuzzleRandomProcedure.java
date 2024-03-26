package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

import java.util.Map;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class LightPuzzleRandomProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double i = 0;
		double j = 0;
		if (true) {
			i = Mth.nextInt(RandomSource.create(), 0, 10) + x;
			j = Mth.nextInt(RandomSource.create(), 0, 10) + z;
			if ((world.getBlockState(BlockPos.containing(i, y, j))).getBlock() == LostdepthsModBlocks.LIGHT_PUZZLE_B.get()) {
				{
					BlockPos _bp = BlockPos.containing(i, y, j);
					BlockState _bs = LostdepthsModBlocks.LIGHT_PUZZLE_A.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
						Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
						if (_property != null && _bs.getValue(_property) != null)
							try {
								_bs = _bs.setValue(_property, (Comparable) entry.getValue());
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(i, y, j);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null)
						_blockEntity.getPersistentData().putBoolean("status", false);
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
		if (true) {
			i = Mth.nextInt(RandomSource.create(), 0, 10) + x;
			j = Mth.nextInt(RandomSource.create(), 0, 10) + z;
			if ((world.getBlockState(BlockPos.containing(i, y, j))).getBlock() == LostdepthsModBlocks.LIGHT_PUZZLE_B.get()) {
				{
					BlockPos _bp = BlockPos.containing(i, y, j);
					BlockState _bs = LostdepthsModBlocks.LIGHT_PUZZLE_A.get().defaultBlockState();
					BlockState _bso = world.getBlockState(_bp);
					for (Map.Entry<Property<?>, Comparable<?>> entry : _bso.getValues().entrySet()) {
						Property _property = _bs.getBlock().getStateDefinition().getProperty(entry.getKey().getName());
						if (_property != null && _bs.getValue(_property) != null)
							try {
								_bs = _bs.setValue(_property, (Comparable) entry.getValue());
							} catch (Exception e) {
							}
					}
					world.setBlock(_bp, _bs, 3);
				}
				if (!world.isClientSide()) {
					BlockPos _bp = BlockPos.containing(i, y, j);
					BlockEntity _blockEntity = world.getBlockEntity(_bp);
					BlockState _bs = world.getBlockState(_bp);
					if (_blockEntity != null)
						_blockEntity.getPersistentData().putBoolean("status", false);
					if (world instanceof Level _level)
						_level.sendBlockUpdated(_bp, _bs, _bs, 3);
				}
			}
		}
	}
}
