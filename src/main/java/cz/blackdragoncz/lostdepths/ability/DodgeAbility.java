package cz.blackdragoncz.lostdepths.ability;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

// A signature is a one-way oath: a signer cannot land a Lost Depths hit on the book's owner, but the
// owner is not bound in return and hits the signer normally. Do not make this mutual.
public class DodgeAbility extends SpecialAbility {

    private static final Direction[] HORIZONTAL = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

    public DodgeAbility(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean onIncomingAttack(Player target, LivingEntity attacker, ItemStack stack, DamageSource source, float amount) {
        // Only players sign books, so mobs are never affected.
        if (!(attacker instanceof Player attackingPlayer))
            return false;
        if (!SoulBinding.isOwner(stack, target))
            return false;
        if (!SoulBinding.matchesSigner(stack, attackingPlayer))
            return false;
        if (!isLostdepthsWeapon(source, attackingPlayer))
            return false;

        sidestep(target);
        target.level().playSound(null, target.blockPosition(), LostdepthsModSounds.DASH.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        return true;
    }

    // Namespace check rather than a tag, so every current and future mod weapon counts with no list to maintain.
    private static boolean isLostdepthsWeapon(DamageSource source, Player attacker) {
        Entity direct = source.getDirectEntity();
        // Judge a projectile by the projectile: by the time it lands the shooter may have swapped hands.
        if (direct != null && direct != attacker)
            return isLostdepths(ForgeRegistries.ENTITY_TYPES.getKey(direct.getType()));
        return isLostdepths(ForgeRegistries.ITEMS.getKey(attacker.getMainHandItem().getItem()));
    }

    private static boolean isLostdepths(@Nullable ResourceLocation id) {
        return id != null && LostdepthsMod.MODID.equals(id.getNamespace());
    }

    // One block in a random horizontal direction. The hit is negated either way; a blocked player just stands still.
    private static void sidestep(Player target) {
        int start = target.level().getRandom().nextInt(HORIZONTAL.length);
        for (int i = 0; i < HORIZONTAL.length; i++) {
            Direction dir = HORIZONTAL[(start + i) % HORIZONTAL.length];
            Vec3 dest = target.position().add(dir.getStepX(), 0, dir.getStepZ());
            if (!canStandAt(target, dest))
                continue;

            if (target instanceof ServerPlayer serverPlayer)
                serverPlayer.connection.teleport(dest.x, dest.y, dest.z, target.getYRot(), target.getXRot());
            else
                target.teleportTo(dest.x, dest.y, dest.z);
            return;
        }
    }

    private static boolean canStandAt(Player player, Vec3 dest) {
        Level level = player.level();
        AABB box = player.getBoundingBox().move(dest.x - player.getX(), 0, dest.z - player.getZ());
        if (!level.noCollision(player, box))
            return false;

        BlockPos feet = BlockPos.containing(dest);
        BlockPos ground = feet.below();
        if (level.getBlockState(ground).isAir() && !player.getAbilities().flying)
            return false;

        return !isHazard(level, ground) && !isHazard(level, feet) && !isHazard(level, feet.above());
    }

    private static boolean isHazard(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.LAVA) || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE) || state.is(Blocks.MAGMA_BLOCK)
                || state.is(Blocks.CAMPFIRE) || state.is(Blocks.SOUL_CAMPFIRE) || state.is(Blocks.CACTUS)
                || state.is(Blocks.SWEET_BERRY_BUSH) || state.is(Blocks.POWDER_SNOW);
    }
}
