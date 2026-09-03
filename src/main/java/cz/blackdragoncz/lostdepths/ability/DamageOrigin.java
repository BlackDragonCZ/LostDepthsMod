package cz.blackdragoncz.lostdepths.ability;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

// Namespace based rather than a tag: lostdepths:tools holds only 3 items.
public final class DamageOrigin {

	private DamageOrigin() {
	}

	public static boolean isLostdepthsWeapon(DamageSource source) {
		Entity direct = source.getDirectEntity();
		Entity attacker = source.getEntity();

		// Judge a projectile by itself: the shooter may have swapped hands mid-flight.
		if (direct != null && direct != attacker)
			return isLostdepths(ForgeRegistries.ENTITY_TYPES.getKey(direct.getType()));
		if (attacker instanceof LivingEntity living)
			return isLostdepths(ForgeRegistries.ITEMS.getKey(living.getMainHandItem().getItem()));
		return false;
	}

	public static boolean isLostdepths(@Nullable ResourceLocation id) {
		return id != null && LostdepthsMod.MODID.equals(id.getNamespace());
	}
}
