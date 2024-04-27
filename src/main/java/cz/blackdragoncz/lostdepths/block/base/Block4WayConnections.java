package cz.blackdragoncz.lostdepths.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Block4WayConnections extends NotFullBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static double NORTHZ1 = 4;
    public static double SOUTHZ2 = 12;
    public static double WESTX1 = 4;
    public static double EASTX2 = 12;

    protected float nodeWidth;
    protected float nodeHeight;
    protected float collisionY;

    public Block4WayConnections(Properties properties, float nodeWidth, float nodeHeight, float collisionY) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(NORTH, false));
        registerDefaultState(defaultBlockState().setValue(SOUTH, false));
        registerDefaultState(defaultBlockState().setValue(EAST, false));
        registerDefaultState(defaultBlockState().setValue(WEST, false));

        this.nodeWidth = nodeWidth;
        this.nodeHeight = nodeHeight;
        this.collisionY = collisionY;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NORTH, SOUTH, EAST, WEST);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState();
        for (Direction face : Direction.Plane.HORIZONTAL)
        {
            state = state.setValue(getPropertyBasedOnDirection(face), canConnectTo(context.getLevel(), context.getClickedPos(), face));
        }
        return state;
    }

    public boolean fenceCollision()
    {
        return false;
    }

    public final boolean isConnected(final BlockState state, final BooleanProperty property)
    {
        return state.getValue(property);
    }

    public BooleanProperty getPropertyBasedOnDirection(Direction direction)
    {
        switch (direction)
        {
            default:
            case DOWN:
            case UP:
            case NORTH:
                return NORTH;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
            case EAST:
                return EAST;
        }
    }

    public abstract boolean canConnectTo(Level world, BlockPos currentPos, Direction neighborDirection);

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getVoxelShape(state, false);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getVoxelShape(state, true);
    }

    public VoxelShape getVoxelShape(BlockState state, boolean collision)
    {
        if (isConnected(state, NORTH))
        {
            NORTHZ1 = 0;
        } else
        {
            NORTHZ1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, SOUTH))
        {
            SOUTHZ2 = 16;
        } else
        {
            SOUTHZ2 = nodeWidth / 2 + 8;
        }
        if (isConnected(state, WEST))
        {
            WESTX1 = 0;
        } else
        {
            WESTX1 = 8 - (nodeWidth / 2);
        }
        if (isConnected(state, EAST))
        {
            EASTX2 = 16;
        } else
        {
            EASTX2 = nodeWidth / 2 + 8;
        }
        return Block.box(WESTX1, 8 - (nodeHeight / 2), NORTHZ1, EASTX2, (collision && fenceCollision()) ? 24 : (8 + (nodeHeight / 2)), SOUTHZ2);
    }

}
