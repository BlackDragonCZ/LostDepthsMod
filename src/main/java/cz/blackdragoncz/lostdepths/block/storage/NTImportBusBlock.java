package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTImportBusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;

import javax.annotation.Nullable;

public class NTImportBusBlock extends Block implements EntityBlock {
	public static final DirectionProperty FACING = BlockStateProperties.FACING;

	public NTImportBusBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
		registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTImportBusBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) return null;
		return (lvl, pos, st, be) -> {
			if (be instanceof NTImportBusBlockEntity bus) {
				NTImportBusBlockEntity.serverTick(lvl, pos, st, bus);
			}
		};
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, block, neighborPos, movedByPiston);
		StorageNetwork.notifyControllerNearby(level, pos);
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
		if (!state.is(newState.getBlock())) {
			StorageNetwork.notifyControllerNearby(level, pos);
		}
		super.onRemove(state, level, pos, newState, movedByPiston);
	}
}
