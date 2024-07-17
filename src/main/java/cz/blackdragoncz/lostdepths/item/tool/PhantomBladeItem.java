
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PhantomBladeItem extends SwordItem {
	public PhantomBladeItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 4f;
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

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 3, -3f, new Item.Properties().fireResistant());
	}

	private static float LOOKUP_DISTANCE = 20.0f;
	private static float TRACE_ENTITY_DISTANCE = 5.0f;

	private void use(Level level, Player player, ItemStack stack)
	{
		boolean hasDamage = stack.getOrCreateTagElement("LostDepths").getBoolean("UseDamage");

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

			if (!hasDamage)
				return;

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
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(player.getItemInHand(hand));
		player.startUsingItem(hand);

		boolean hasDamage = ar.getObject().getOrCreateTagElement("LostDepths").getBoolean("UseDamage");
		player.getCooldowns().addCooldown(ar.getObject().getItem(), hasDamage ? 35 : 20);

		if (level.isClientSide())
			return ar;

		use(level, player, ar.getObject());

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
