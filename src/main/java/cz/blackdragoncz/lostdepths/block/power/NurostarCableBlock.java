package cz.blackdragoncz.lostdepths.block.power;

import cz.blackdragoncz.lostdepths.block.base.Block6WayConnections;
import cz.blackdragoncz.lostdepths.block.power.entity.NurostarCableBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class NurostarCableBlock extends Block6WayConnections implements SimpleWaterloggedBlock, EntityBlock {

    private static final VoxelShape CABLE = box(5.25,5.25,5.25,10.75,10.75,10.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[] {
            box(5.25, 5.25, 0, 10.75, 10.75, 6),
            box(10.75, 5.25, 5.25, 16, 10.75, 10.75),
            box(5.25, 5.25, 10.75, 10.75, 10.75, 16),
            box(0, 5.25, 5.25, 5.25, 10.75, 10.75),
            box(5.25, 10.75, 5.25, 10.75, 16, 10.75),
            box(5.25, 0, 5.25, 10.75, 6, 10.75)};

    public NurostarCableBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(3f, 4f), 4, 4, 4);

    }

    @Override
    public boolean canConnectTo(Level world, BlockPos currentPos, Direction neighborDirection) {
        final BlockPos neighborPos = currentPos.relative(neighborDirection);
        final BlockState neighborState = world.getBlockState(neighborPos);
        BlockState ownState = world.getBlockState(currentPos);
        return isValidConnection(neighborState, ownState, world, currentPos, neighborDirection, neighborPos);
    }

    private boolean isValidConnection(BlockState neighborState, BlockState ownState, Level world, BlockPos currentPos, Direction neighborDirection, BlockPos neighborPos) {
        Block nb = neighborState.getBlock();

        if (nb instanceof NurostarCableBlock) {
            return true;
        }

        if (canConnectEnergy(world, currentPos, neighborDirection))
            return true;

        return false;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean flag) {
        //Utils.debug("neighbor changed", state, world, pos, block, neighbor, flag);
        if(!world.isClientSide) {
            for (Direction face : Direction.values())
            {
                state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(world, pos, face));
            }
            world.setBlockAndUpdate(pos, state);

            world.getBlockEntity(pos, LostdepthsModBlockEntities.NUROSTAR_CABLE.get())
                    .ifPresent(NurostarCableBlockEntity::updateEnergySides);
        }
        super.neighborChanged(state, world, pos, block, neighbor, flag);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        VoxelShape voxelShape = CABLE;
        if (blockGetter instanceof Level world) {
            if (state.getValue(NORTH) || canConnectEnergy(world, pos, Direction.NORTH))
                voxelShape = Shapes.or(voxelShape, MULTIPART[0]);
            if (state.getValue(EAST) || canConnectEnergy(world, pos, Direction.EAST))
                voxelShape = Shapes.or(voxelShape, MULTIPART[1]);
            if (state.getValue(SOUTH) || canConnectEnergy(world, pos, Direction.SOUTH))
                voxelShape = Shapes.or(voxelShape, MULTIPART[2]);
            if (state.getValue(WEST) || canConnectEnergy(world, pos, Direction.WEST))
                voxelShape = Shapes.or(voxelShape, MULTIPART[3]);
            if (state.getValue(UP) || canConnectEnergy(world, pos, Direction.UP))
                voxelShape = Shapes.or(voxelShape, MULTIPART[4]);
            if (state.getValue(DOWN) || canConnectEnergy(world, pos, Direction.DOWN))
                voxelShape = Shapes.or(voxelShape, MULTIPART[5]);
        }
        return voxelShape;
    }

    public boolean canConnectEnergy(Level world, BlockPos pos, Direction direction) {
        BlockEntity tile = world.getBlockEntity(pos.relative(direction));
        if (tile == null)
            return false;
        return tile.getCapability(ForgeCapabilities.ENERGY).isPresent();
    }

    /*@Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        var newState = createCableState(level, pos);

        if (newState != state) {
            level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
        }

        if (level.getBlockEntity(pos) instanceof NurostarCableBlockEntity cable) {
            var oldSides = EnumSet.copyOf(cable.energySides);

            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(level, pos, direction)) {
                    cable.energySides.add(direction);
                }
            }

            if (!oldSides.equals(cable.energySides)) {
                //cable.sync();
            }
        }

        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }*/

    /*@Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving) {
        BlockEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof NurostarCableBlockEntity cable) {
            cable.energySides.clear();
            for (Direction direction : Direction.values()) {
                if (canConnectEnergy(world, pos, direction)) {
                    cable.energySides.add(direction);
                }
            }
            //cable.sync();
        }
        super.onPlace(state, world, pos, oldState, isMoving);
    }*/

    public static Optional<Direction> getHitSide(Vec3 hit, BlockPos pos) {
        double x = hit.x - pos.getX();
        double y = hit.y - pos.getY();
        double z = hit.z - pos.getZ();
        if (x > 0.0D && x < 0.4D)
            return Optional.of(Direction.WEST);
        else if (x > 0.6D && x < 1.0D)
            return Optional.of(Direction.EAST);
        else if (z > 0.0D && z < 0.4D)
            return Optional.of(Direction.NORTH);
        else if (z > 0.6D && z < 1.0D)
            return Optional.of(Direction.SOUTH);
        else if (y > 0.6D && y < 1.0D)
            return Optional.of(Direction.UP);
        else if (y > 0.0D && y < 0.4D)
            return Optional.of(Direction.DOWN);
        return Optional.empty();
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
        return BlockPathTypes.BLOCKED;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new NurostarCableBlockEntity(blockPos, blockState);
    }

    public boolean triggerEvent(BlockState pState, Level pLevel, BlockPos pPos, int pId, int pParam) {
        super.triggerEvent(pState, pLevel, pPos, pId, pParam);
        BlockEntity $$5 = pLevel.getBlockEntity(pPos);
        return $$5 == null ? false : $$5.triggerEvent(pId, pParam);
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        BlockEntity $$3 = pLevel.getBlockEntity(pPos);
        return $$3 instanceof MenuProvider ? (MenuProvider)$$3 : null;
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>) pTicker : null;
    }
}
