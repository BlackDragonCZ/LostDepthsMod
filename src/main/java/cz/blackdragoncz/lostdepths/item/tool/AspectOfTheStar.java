package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModKeyMappings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AspectOfTheStar extends SwordItem {

    public boolean toggleDamage = false;
    public String keybindToggle = LostdepthsModKeyMappings.ACTION_BUTTON.getKey().getName();



    public AspectOfTheStar() {
        super(new Tier() {
            public int getUses() {
                return 0;
            }

            public float getSpeed() {
                return 1f;
            }

            public float getAttackDamageBonus() {
                return -0.5f;
            }

            public int getLevel() {
                return 1;
            }

            public int getEnchantmentValue() {
                return 0;
            }

            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(LostdepthsModItems.INFUSED_IRON.get());
            }
        }, 18, -1.5f, new Item.Properties().fireResistant());
    }

    private static float LOOKUP_DISTANCE = 8.0f;
    private static float TRACE_ENTITY_DISTANCE = 2.0f;

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(player.getItemInHand(hand));
        player.startUsingItem(hand);
        player.getCooldowns().addCooldown(ar.getObject().getItem(), 35);

        if (level.isClientSide())
            return ar;

        Vec3 startPos = player.getEyePosition();
        Vec3 finalPos = player.getEyePosition().add(player.getForward().multiply(LOOKUP_DISTANCE, LOOKUP_DISTANCE, LOOKUP_DISTANCE));

        AtomicReference<BlockPos> lastCheckedPos = new AtomicReference<>();
        BlockPos checkedPos = BlockGetter.traverseBlocks(startPos, finalPos, level, (lvl, blockPos) -> {
            if (lvl.getBlockState(blockPos).is(BlockTags.create(LostdepthsMod.rl("unbreakable"))) ||
                    lvl.getBlockState(blockPos.below()).is(BlockTags.create(LostdepthsMod.rl("unbreakable"))))
            {
                return blockPos;
            }

            lastCheckedPos.set(blockPos);

            return null;
        }, (obj) -> null);

        if (checkedPos != null) {
            checkedPos = lastCheckedPos.get();
        }

        BlockPos pos = BlockGetter.traverseBlocks(checkedPos != null ? checkedPos.getCenter() : finalPos, startPos, level, (Level lvl, BlockPos blockPos) -> {
            AABB aabb = new AABB(blockPos.getX() - 1, blockPos.getY() - 1, blockPos.getZ() - 1, blockPos.getX() + 1, blockPos.getY() + 1, blockPos.getZ() + 1);

            if (!lvl.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), player, aabb).isEmpty())
            {
                //System.out.println("Found entity in path");
                return blockPos;
            }
            if (lvl.getBlockState(blockPos).isAir() && lvl.getBlockState(blockPos.below()).isAir()) {
                return blockPos;
            }
            return null;
        }, (levelis) -> {
            return null;
        });

        if (pos != null) {
            pos = pos.below();
            player.teleportTo(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);

            // Find entities on found path
            List<LivingEntity> pathEntities = new ArrayList<>();
            BlockGetter.traverseBlocks(startPos, pos.getCenter(), pathEntities, (livingEntities, blockPos) -> {
                BlockPos belowPos = blockPos.below();
                AABB aabb = new AABB(belowPos.getX() - (TRACE_ENTITY_DISTANCE-1), belowPos.getY() - (TRACE_ENTITY_DISTANCE-1), belowPos.getZ() - (TRACE_ENTITY_DISTANCE-1), blockPos.getX() + TRACE_ENTITY_DISTANCE, blockPos.getY() + TRACE_ENTITY_DISTANCE, blockPos.getZ() + TRACE_ENTITY_DISTANCE);
                livingEntities.addAll(level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), player, aabb));
                return null;
            }, (obj) -> null);

            // Damage path entities
            for (LivingEntity entity : pathEntities) {
                entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC)), entity.getMaxHealth() * 0.08f);
            }

            // Damage final pos entities
            BlockPos abovePos = pos.above();
            AABB aabb = new AABB(pos.getX(), pos.getY(), pos.getZ(), abovePos.getX() + 1, abovePos.getY() + 1, abovePos.getZ() + 1);
            List<LivingEntity> entities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), player, aabb);

            for (LivingEntity entity : entities) {
                entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.INDIRECT_MAGIC)), entity.getMaxHealth() * 0.02f);
            }
        }

        return ar;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("§5Right Click: Teleport up to 8 blocks ahead of you,"));
        list.add(Component.literal("§5deals 8% Max Health of target as damage"));
        list.add(Component.literal("§dDeals 2% True Damage to entities near the trail"));

        list.add(Component.literal("§6Toggle damage by teleport ability with §2").append(Component.keybind(keybindToggle)).append("."));
        list.add(Component.literal("§6Damage: §4" + toggleDamage));
    }
}
