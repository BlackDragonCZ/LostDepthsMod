package cz.blackdragoncz.lostdepths.ability;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

import org.jetbrains.annotations.Nullable;

// Namespace based rather than a tag: lostdepths:tools holds only 3 items.
public final class DamageOrigin {

	private DamageOrigin() {
	}

	// Damage a Lost Depths item threw back at its attacker. Gear dealt it, so it pierces Spectros, but it is nobody's swing, so it cannot be dodged.
	public static class Reflected extends DamageSource {
		public Reflected(Holder<DamageType> type, Entity reflector) {
			super(type, reflector);
		}
	}

	public static boolean isReflected(DamageSource source) {
		return source instanceof Reflected;
	}

	public static boolean isLostdepthsWeapon(DamageSource source) {
		// Thorns is never a swing. Judging it by the reflector's main hand made the Soulbinder teleport-dodge vanilla Thorns ticks.
		if (source.is(DamageTypes.THORNS))
			return false;

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
