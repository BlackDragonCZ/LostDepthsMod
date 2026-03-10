package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

import java.util.List;

public class GhostHunterItem extends SwordItem {
	private static final float LOOKUP_DISTANCE = 30.0f;

	public GhostHunterItem() {
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
		float damage = entity.getMaxHealth() * 0.1f;
		entity.hurt(WeaponDamageHelper.magicDamage(entity, sourceentity, "death.attack.ghost_hunter"), damage);
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

		double distance = player.position().distanceTo(landingPos.getCenter());
		player.teleportTo(landingPos.getX() + 0.5, landingPos.getY(), landingPos.getZ() + 0.5);

		// Arrival damage: up to 50% max health, scaled by distance travelled
		AABB arrivalBox = new AABB(landingPos).inflate(3);
		List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class,
				TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(),
				player, arrivalBox);

		float distanceScale = (float) Math.min(1.0, distance / LOOKUP_DISTANCE);
		for (LivingEntity entity : nearbyEntities) {
			float damage = entity.getMaxHealth() * 0.5f * distanceScale;
			entity.hurt(WeaponDamageHelper.magicDamage(entity, player, "death.attack.ghost_hunter"), damage);
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
		list.add(Component.literal("§6Deals 10% max health damage"));
		list.add(Component.literal("§5Right click to teleport up to 30 blocks to an entity."));
		list.add(Component.literal("§dOn arrival:"));
		list.add(Component.literal("§cDeal up to 50% max health damage to very close enemies"));
		list.add(Component.literal("§7§oDamage based on distance travelled."));
	}
}
