
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.entity.projectile.EntityFluxBall;
import cz.blackdragoncz.lostdepths.util.ICustomHoldPose;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

import cz.blackdragoncz.lostdepths.entity.projectile.FluxLanternProjectileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn){
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (playerIn.isCrouching()){
			if (!stack.hasTag()){
				stack.setTag(new CompoundTag());
			}

			stack.getTag().putInt("flux_dimension_id", worldIn.dimension().hashCode());
			stack.getTag().putDouble("flux_pos_x", playerIn.getX());
			stack.getTag().putDouble("flux_pos_y", playerIn.getY());
			stack.getTag().putDouble("flux_pos_z", playerIn.getZ());
			worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 2.0f, 1.0f);

			if (worldIn.isClientSide){
				playerIn.displayClientMessage(Component.literal("§5Coordinates marked for fluxation."), true);
			}
		} else if (stack.hasTag() && stack.getTag().contains("flux_dimension_id")) {
			if (!worldIn.isClientSide){
				int dimId = stack.getTag().getInt("flux_dimension_id");
				double goX = stack.getTag().getDouble("flux_pos_x");
				double goY = stack.getTag().getDouble("flux_pos_y");
				double goZ = stack.getTag().getDouble("flux_pos_z");

				EntityFluxBall shot = new EntityFluxBall(worldIn, playerIn) {
					@Override
					protected void defineSynchedData() {

					}
				};
				shot.setFlux(dimId, goX, goY, goZ);
				shot.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0.0f, 1.5f, 1.0f);
				worldIn.addFreshEntity(shot);
			}
			playerIn.playSound(SoundEvents.SNOWBALL_THROW, 1.0f, 1.0f);
		}

		return super.use(worldIn, playerIn, handIn);
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

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(stack, world, list, flag);
		list.add(Component.literal("§6Shift+Right Click: Mark a location and dimension for fluxation."));
		list.add(Component.literal("§5Shoot a projectile that fluxes players."));
		list.add(Component.literal("§5Fluxes hit players on melee hit"));


		if (stack.hasTag() && stack.getTag().contains("flux_dimension_id")) {
			list.add(Component.literal("§2Flux Marked:"));
			list.add(Component.literal("§2Dimension: " + stack.getTag().getInt("flux_dimension_id")));
			list.add(Component.literal("§2X: " + stack.getTag().getInt("flux_pos_x")));
			list.add(Component.literal("§2Y: " + stack.getTag().getInt("flux_pos_y")));
			list.add(Component.literal("§2Z: " + stack.getTag().getInt("flux_pos_z")));
		}else {
			list.add(Component.literal("§cNO FLUX LOCATION SET"));
		}
	}
}
