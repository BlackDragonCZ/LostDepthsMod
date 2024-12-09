package cz.blackdragoncz.lostdepths.block.security;

import cz.blackdragoncz.lostdepths.client.ClientSide;
import cz.blackdragoncz.lostdepths.item.security.SecurityPassItem;
import cz.blackdragoncz.lostdepths.util.SecurityClearanceSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SecurityClearanceBlock extends Block {

    private int requiredClearance;

    public SecurityClearanceBlock(int requiredClearance) {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(-1, 3600000)
                .pushReaction(PushReaction.BLOCK)
        );

        this.requiredClearance = requiredClearance;
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

    @Override
    public InteractionResult use(@NotNull BlockState blockState, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SecurityPassItem securityPass)
        {
            if (this.requiredClearance <= securityPass.getClearance())
            {
                if (!world.isClientSide()) {
                    world.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
                } else {
                    world.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
                }

                if (player instanceof ServerPlayer serverPlayer) {
                    SecurityClearanceSystem.giveClearance(serverPlayer, this.requiredClearance);
                }
                else
                {
                    ClientSide.setSecurityClearance(this.requiredClearance);
                }
            } else {
                if (!world.isClientSide()) {
                    world.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:gate_error")), SoundSource.MASTER, 1, 1);
                } else {
                    world.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:gate_error")), SoundSource.MASTER, 1, 1, false);
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
