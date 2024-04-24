package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;
import cz.blackdragoncz.lostdepths.procedures.CrystalizedPickaxeMakeItemGlowProcedure;
import cz.blackdragoncz.lostdepths.world.entity.projectile.ThrownDraconicTrident;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractCustomTrident extends SwordItem implements Vanishable {

    public AbstractCustomTrident(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    protected abstract EntityType<ThrownDraconicTrident> getThrowEntity();

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return CrystalizedPickaxeMakeItemGlowProcedure.execute();
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !pPlayer.isCreative();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            if (i < 10) {
                return;
            }

            if (!pLevel.isClientSide) {
                pStack.hurtAndBreak(1, player, (p_43388_) -> {
                    p_43388_.broadcastBreakEvent(pEntityLiving.getUsedItemHand());
                });

                if (!player.isInLava()) {
                    ThrownDraconicTrident projectile = new ThrownDraconicTrident(getThrowEntity(), pLevel, pEntityLiving, pStack);

                    if (player.isOnFire()) {
                        projectile.setIgniteEntity(true);
                    }

                    projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    pLevel.addFreshEntity(projectile);
                    pLevel.playSound((Player)null, projectile, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(pStack);
                    }
                }
            }

            int j = 3;

            if (player.isInLava()) {
                float f7 = player.getYRot();
                float f = player.getXRot();
                float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * ((1.0F + (float)j) / 4.0F);
                f1 *= f5 / f4;
                f2 *= f5 / f4;
                f3 *= f5 / f4;
                player.push((double)f1, (double)f2, (double)f3);
                player.startAutoSpinAttack(20);
                if (player.onGround()) {
                    float f6 = 1.1999999F;
                    player.move(MoverType.SELF, new Vec3(0.0D, (double)1.1999999F, 0.0D));
                }

                SoundEvent soundevent;
                if (j >= 3) {
                    soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
                } else if (j == 2) {
                    soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
                } else {
                    soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
                }

                pLevel.playSound((Player)null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
            }

            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.LOYALTY;
    }
}
