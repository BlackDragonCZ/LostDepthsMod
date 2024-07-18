
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.util.ICustomHoldPose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.entity.FluxLanternProjectileEntity;

public class FluxLanternItem extends Item implements ICustomHoldPose {
	public FluxLanternItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}

	@Override
	public int getUseDuration(ItemStack itemstack) {
		return 72000;
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0f;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(entity.getItemInHand(hand));
		entity.startUsingItem(hand);
		return ar;
	}

	@Override
	public void releaseUsing(ItemStack itemstack, Level world, LivingEntity entity, int time) {
		if (!world.isClientSide() && entity instanceof ServerPlayer player) {
			ItemStack stack = ProjectileWeaponItem.getHeldProjectile(entity, e -> e.getItem() == FluxLanternProjectileEntity.PROJECTILE_ITEM.getItem());
			if (stack == ItemStack.EMPTY) {
				for (int i = 0; i < player.getInventory().items.size(); i++) {
					ItemStack teststack = player.getInventory().items.get(i);
					if (teststack != null && teststack.getItem() == FluxLanternProjectileEntity.PROJECTILE_ITEM.getItem()) {
						stack = teststack;
						break;
					}
				}
			}
			if (player.getAbilities().instabuild || stack != ItemStack.EMPTY) {
				FluxLanternProjectileEntity projectile = FluxLanternProjectileEntity.shoot(world, entity, world.getRandom());
				itemstack.hurtAndBreak(1, entity, e -> e.broadcastBreakEvent(entity.getUsedItemHand()));
				if (player.getAbilities().instabuild) {
					projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
				} else {
					if (stack.isDamageableItem()) {
						if (stack.hurt(1, world.getRandom(), player)) {
							stack.shrink(1);
							stack.setDamageValue(0);
							if (stack.isEmpty())
								player.getInventory().removeItem(stack);
						}
					} else {
						stack.shrink(1);
						if (stack.isEmpty())
							player.getInventory().removeItem(stack);
					}
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.literal("§6Shift+Right Click: Mark a location and dimension for fluxation."));
		list.add(Component.literal("§5Shoot a projectile that fluxes players."));
		list.add(Component.literal("§5Fluxes hit players on melee hit"));

		boolean hasDamage = stack.getOrCreateTagElement("LostDepths").getBoolean("UseDamage");
		double hasDimension = 0.0f;
		double hasX = 0.0f;
		double hasY = 0.0f;
		double hasZ = 0.0f;

		boolean marked = false;
		if (stack.getOrCreateTagElement("LostDepths").getBoolean("Marked")){
			marked = true;
		}

		if (marked) {
			list.add(Component.literal("§2Flux Marked:"));
			list.add(Component.literal("§2Dimension: " + hasDimension));
			list.add(Component.literal("§2X: " + hasX));
			list.add(Component.literal("§2Y: " + hasY));
			list.add(Component.literal("§2Z: " + hasZ));
		}else {
			list.add(Component.literal("§cNO FLUX LOCATION SET"));
		}
		//example of dimension position:
		/*
		Flux Marked:
		Dim: 0
		X: 1
		Y: 2
		Z: 3
		 */
	}
}
