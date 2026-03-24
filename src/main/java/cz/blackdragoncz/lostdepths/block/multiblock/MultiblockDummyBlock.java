package cz.blackdragoncz.lostdepths.block.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class MultiblockDummyBlock extends Block implements EntityBlock {

    public static final BooleanProperty DEBUG_VISIBLE = BooleanProperty.create("debug_visible");

    public MultiblockDummyBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(6f, 12f)
                .noOcclusion()
                .noLootTable()
                .pushReaction(PushReaction.BLOCK));
        registerDefaultState(stateDefinition.any().setValue(DEBUG_VISIBLE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DEBUG_VISIBLE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultiblockDummyBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(DEBUG_VISIBLE) ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return Collections.emptyList();
    }

    @Override
    @SuppressWarnings("deprecation")
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Debug stick toggles visibility of dummy block
        if (player.getItemInHand(hand).is(Items.DEBUG_STICK) && player.hasPermissions(2)) {
            if (!level.isClientSide()) {
                boolean visible = !state.getValue(DEBUG_VISIBLE);
                level.setBlock(pos, state.setValue(DEBUG_VISIBLE, visible), 3);
                player.displayClientMessage(Component.literal("§7[Debug] Dummy block " + (visible ? "§avisible" : "§chidden")), true);
            }
            return InteractionResult.SUCCESS;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof MultiblockDummyBlockEntity dummy) {
            BlockPos controllerPos = dummy.getControllerPos();
            BlockState controllerState = level.getBlockState(controllerPos);
            return controllerState.getBlock().use(controllerState, level, controllerPos, player, hand, hit);
        }
        return InteractionResult.PASS;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        // Punching a dummy part breaks the controller
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MultiblockDummyBlockEntity dummy) {
                BlockPos controllerPos = dummy.getControllerPos();
                level.destroyBlock(controllerPos, true);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MultiblockDummyBlockEntity dummy) {
                BlockPos controllerPos = dummy.getControllerPos();
                BlockState controllerState = level.getBlockState(controllerPos);
                if (!(controllerState.getBlock() instanceof MultiblockDummyBlock)) {
                    level.destroyBlock(controllerPos, true);
                }
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }
}
