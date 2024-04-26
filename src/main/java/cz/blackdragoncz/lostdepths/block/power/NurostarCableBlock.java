package cz.blackdragoncz.lostdepths.block.power;

import cz.blackdragoncz.lostdepths.block.power.entity.NurostarCableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
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

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class NurostarCableBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    private static final VoxelShape CABLE = box(5.25,5.25,5.25,10.75,10.75,10.75);
    private static final VoxelShape[] MULTIPART = new VoxelShape[] {
            box(5.25, 5.25, 0, 10.75, 10.75, 6),
            box(10.75, 5.25, 5.25, 16, 10.75, 10.75),
            box(5.25, 5.25, 10.75, 10.75, 10.75, 16),
            box(0, 5.25, 5.25, 5.25, 10.75, 10.75),
            box(5.25, 10.75, 5.25, 10.75, 16, 10.75),
            box(5.25, 0, 5.25, 10.75, 6, 10.75)};

    public NurostarCableBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(3f, 4f));
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
        return false;//!(tile instanceof NurostarCableBlockEntity && direction.getOpposite());
    }

    @Override
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
    }

    public boolean[] canAttach(BlockState state, Level world, BlockPos pos, Direction direction) {
        return new boolean[] { world.getBlockState(pos.relative(direction)).getBlock() == this || canConnectEnergy(world, pos, direction),
                canConnectEnergy(world, pos, direction) };
    }

    private BlockState createCableState(Level world, BlockPos pos) {
        final BlockState state = defaultBlockState();
        boolean[] north = canAttach(state, world, pos, Direction.NORTH);
        boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
        boolean[] west = canAttach(state, world, pos, Direction.WEST);
        boolean[] east = canAttach(state, world, pos, Direction.EAST);
        boolean[] up = canAttach(state, world, pos, Direction.UP);
        boolean[] down = canAttach(state, world, pos, Direction.DOWN);
        FluidState fluidState = world.getFluidState(pos);
        return state.setValue(NORTH, north[0] && !north[1]).setValue(SOUTH, south[0] && !south[1]).setValue(WEST, west[0] && !west[1])
                .setValue(EAST, east[0] && !east[1]).setValue(UP, up[0] && !up[1]).setValue(DOWN, down[0] && !down[1])
                .setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
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
    }

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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
        super.createBlockStateDefinition(builder);
    }

    //common
    @Override
    public void appendHoverText(ItemStack itemstack, BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter world, BlockPos pos, Mob entity) {
        return BlockPathTypes.BLOCKED;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
