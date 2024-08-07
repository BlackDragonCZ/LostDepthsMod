
package cz.blackdragoncz.lostdepths.entity.projectile;

import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class LostDarkEntityProjectile extends AbstractArrow implements ItemSupplier {
	public LostDarkEntityProjectile(PlayMessages.SpawnEntity packet, Level world) {
		super(LostdepthsModEntities.LOST_DARK_PROJECTILE.get(), world);
	}

	public LostDarkEntityProjectile(EntityType<? extends LostDarkEntityProjectile> type, Level world) {
		super(type, world);
	}

	public LostDarkEntityProjectile(EntityType<? extends LostDarkEntityProjectile> type, double x, double y, double z, Level world) {
		super(type, x, y, z, world);
	}

	public LostDarkEntityProjectile(EntityType<? extends LostDarkEntityProjectile> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void doPostHurtEffects(LivingEntity livingEntity) {
		super.doPostHurtEffects(livingEntity);
		livingEntity.setArrowCount(livingEntity.getArrowCount() - 1);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return new ItemStack(Items.FIRE_CHARGE);
	}

	@Override
	protected ItemStack getPickupItem() {
		return new ItemStack(Items.FIRE_CHARGE);
	}
}
