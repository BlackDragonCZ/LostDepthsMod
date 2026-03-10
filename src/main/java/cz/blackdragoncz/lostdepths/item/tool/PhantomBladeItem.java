package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PhantomBladeItem extends SwordItem {
	private static final float LOOKUP_DISTANCE = 20.0f;

	public PhantomBladeItem() {
		super(new Tier() {
			public int getUses() { return 0; }
			public float getSpeed() { return 4f; }
			public float getAttackDamageBonus() { return -0.5f; }
			public int getLevel() { return 1; }
			public int getEnchantmentValue() { return 0; }
			public Ingredient getRepairIngredient() { return Ingredient.of(); }
		}, 3, -3f, new Item.Properties().fireResistant());
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
		boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
		float damage = entity.getMaxHealth() * 0.75f;
		entity.hurt(WeaponDamageHelper.magicDamage(entity, sourceentity, "death.attack.phantom_blade"), damage);
		return retval;
	}

	private static BlockPos findSafeLanding(Level level, BlockPos pos) {
		for (int dy = 0; dy <= 2; dy++) {
			BlockPos feet = pos.above(dy);
			BlockPos head = feet.above();
			if (!level.getBlockState(feet).isSuffocating(level, feet) &&
					!level.getBlockState(head).isSuffocating(level, head)) {
				return feet;
			}
		}
		return null;
	}

	private void teleport(Level level, Player player) {
		Vec3 startPos = player.getEyePosition();
		Vec3 finalPos = startPos.add(player.getForward().scale(LOOKUP_DISTANCE));

		// Forward trace: if any unbreakable block is on path, cancel teleport entirely
		boolean[] hitUnbreakable = {false};
		BlockGetter.traverseBlocks(startPos, finalPos, level, (lvl, blockPos) -> {
			if (lvl.getBlockState(blockPos).is(BlockTags.create(LostdepthsMod.rl("unbreakable"))) ||
					lvl.getBlockState(blockPos.below()).is(BlockTags.create(LostdepthsMod.rl("unbreakable")))) {
				hitUnbreakable[0] = true;
				return blockPos;
			}
			return null;
		}, obj -> null);

		if (hitUnbreakable[0]) return;

		// Backward trace: find entity or open air to land
		BlockPos landingPos = BlockGetter.traverseBlocks(finalPos, startPos, level, (lvl, blockPos) -> {
			AABB aabb = new AABB(blockPos).inflate(1);
			if (!lvl.getNearbyEntities(LivingEntity.class,
					TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(),
					player, aabb).isEmpty()) {
				return blockPos;
			}
			if (lvl.getBlockState(blockPos).isAir() && lvl.getBlockState(blockPos.below()).isAir()) {
				return blockPos;
			}
			return null;
		}, obj -> null);

		if (landingPos == null) return;

		// Ensure player doesn't end up inside a solid block
		landingPos = findSafeLanding(level, landingPos);
		if (landingPos == null) return;

		player.teleportTo(landingPos.getX() + 0.5, landingPos.getY(), landingPos.getZ() + 0.5);

		// Find nearest entity at arrival
		AABB arrivalBox = new AABB(landingPos).inflate(3);
		List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class,
				TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(),
				player, arrivalBox);

		if (nearbyEntities.isEmpty()) return;

		LivingEntity target = nearbyEntities.get(0);

		// Transfer ALL negative effects from player to target
		List<MobEffectInstance> negativeEffects = new ArrayList<>();
		for (MobEffectInstance effect : player.getActiveEffects()) {
			if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
				negativeEffects.add(new MobEffectInstance(effect));
			}
		}

		int transferred = 0;
		for (MobEffectInstance effect : negativeEffects) {
			target.addEffect(new MobEffectInstance(effect));
			player.removeEffect(effect.getEffect());
			transferred++;
		}

		// 10% max health damage per effect transferred
		if (transferred > 0) {
			float damage = target.getMaxHealth() * 0.1f * transferred;
			target.hurt(WeaponDamageHelper.magicDamage(target, player, "death.attack.phantom_blade"), damage);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(player.getItemInHand(hand));
		player.startUsingItem(hand);
		player.getCooldowns().addCooldown(ar.getObject().getItem(), 35);

		if (!level.isClientSide()) {
			teleport(level, player);
		}

		return ar;
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.literal("§6Deals 75% Max Health Damage"));
		list.add(Component.literal("§5Right click to teleport up to 20 blocks to an entity"));
		list.add(Component.literal("§5through walls."));
		list.add(Component.literal("§bTeleport to an entity transfers ALL negative potion effects to them."));
		list.add(Component.literal("§5Teleport deals 10% Max Health Damage per effect transferred."));
	}
}
