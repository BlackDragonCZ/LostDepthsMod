package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.network.LostdepthsModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber
public class ForgefirePickaxeProcedure {

    private static final ResourceKey<Level> DIM_LOST_DUNGEONS = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:lost_dungeons"));

    private static boolean isLava(Level level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        return fluidState.getType() == Fluids.LAVA || fluidState.getType() == Fluids.FLOWING_LAVA;
    }

    // No saved dive coordinates: prefer the player's own bed or anchor, then world spawn. The last flag is
    // "respawning after the end fight", which here just means do not burn a respawn anchor charge - the
    // player is travelling home, not dying. Bed/anchor is only usable if it is in the level we return to.
    private static Vec3 safeReturnPos(ServerPlayer player, ServerLevel level) {
        BlockPos respawn = player.getRespawnPosition();
        if (respawn != null && player.getRespawnDimension() == level.dimension()) {
            Optional<Vec3> found = Player.findRespawnPositionAndUseSpawnBlock(level, respawn, player.getRespawnAngle(), player.isRespawnForced(), true);
            if (found.isPresent())
                return found.get();
        }
        BlockPos spawn = level.getSharedSpawnPos();
        return new Vec3(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
    }

    private static boolean isDivingInLava(Level level, BlockPos pos) {
        return isLava(level, pos) && isLava(level, pos.above(1)) && isLava(level, pos.above(2));
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingAttackEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().getEntity() != null) {
                return;
            }

            if (!(event.getSource().is(DamageTypes.IN_FIRE)
                    || event.getSource().is(DamageTypes.ON_FIRE)
                    || event.getSource().is(DamageTypes.HOT_FLOOR)
                    || event.getSource().is(DamageTypes.FIREBALL)
                    || event.getSource().is(DamageTypes.FIREWORKS)
                    || event.getSource().is(DamageTypes.UNATTRIBUTED_FIREBALL)
                    || event.getSource().is(DamageTypes.LAVA)
            )) {
                return;
            }

            ItemStack mainHandStack = player.getItemInHand(InteractionHand.MAIN_HAND);
            ItemStack offHandStack = player.getItemInHand(InteractionHand.OFF_HAND);

            if (mainHandStack.getItem() == LostdepthsModItems.FORGEFIRE_AXE.get() || offHandStack.getItem() == LostdepthsModItems.FORGEFIRE_AXE.get()) {
                event.setCanceled(true);
                player.clearFire();
            }

            if (mainHandStack.getItem() == LostdepthsModItems.FORGEFIRE_PICKAXE.get() || offHandStack.getItem() == LostdepthsModItems.FORGEFIRE_PICKAXE.get()) {
                event.setCanceled(true);
                player.clearFire();

                Level level = player.level();

                BlockPos playerBlockPos = BlockPos.containing(player.getX(), player.getY(), player.getZ());

                if (isDivingInLava(level, playerBlockPos)) {
                    if (player instanceof ServerPlayer serverPlayer && !level.isClientSide()) {
                        if (level.dimension() == Level.OVERWORLD) {
                            ServerLevel nextLevel = serverPlayer.server.getLevel(DIM_LOST_DUNGEONS);

                            if (nextLevel == null)
                                return;

                            player.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY, null)
                                    .ifPresent((cap) -> {
                                        cap.x = player.getX();
                                        cap.y = player.getY();
                                        cap.z = player.getZ();
                                        cap.syncPlayerVariables(player);
                                    });

                            serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                            serverPlayer.teleportTo(nextLevel, serverPlayer.getX(), 65, serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                            serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                            for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                            serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                        } else if (level.dimension() == DIM_LOST_DUNGEONS) {
                            ServerLevel nextLevel = serverPlayer.server.getLevel(Level.OVERWORLD);

                            if (nextLevel == null)
                                return;

                            LostdepthsModVariables.PlayerVariables variables = player.getCapability(LostdepthsModVariables.PLAYER_VARIABLES_CAPABILITY).orElse(new LostdepthsModVariables.PlayerVariables());

                            // Reaching lost_dungeons any other way (portal, command, respawn) leaves these unset,
                            // and 0/0/0 would drop the player into the void on the way back.
                            double returnX = variables.x, returnY = variables.y + 2, returnZ = variables.z;
                            if (variables.x == 0 && variables.y == 0 && variables.z == 0) {
                                Vec3 fallback = safeReturnPos(serverPlayer, nextLevel);
                                returnX = fallback.x;
                                returnY = fallback.y;
                                returnZ = fallback.z;
                            }

                            serverPlayer.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.WIN_GAME, 0));
                            serverPlayer.teleportTo(nextLevel, returnX, returnY, returnZ, serverPlayer.getYRot(), serverPlayer.getXRot());
                            serverPlayer.connection.send(new ClientboundPlayerAbilitiesPacket(serverPlayer.getAbilities()));
                            for (MobEffectInstance _effectinstance : serverPlayer.getActiveEffects())
                                serverPlayer.connection.send(new ClientboundUpdateMobEffectPacket(serverPlayer.getId(), _effectinstance));
                            serverPlayer.connection.send(new ClientboundLevelEventPacket(1032, BlockPos.ZERO, 0, false));
                        }
                    }
                }
            }
        }
    }

}
