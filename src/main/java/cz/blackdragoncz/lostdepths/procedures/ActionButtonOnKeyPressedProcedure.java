package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;

public class ActionButtonOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Player player) {
		if (player == null)
			return;

		if (player.getMainHandItem().getItem() == LostdepthsModItems.ASPECT_OF_THE_STAR.get()) {
			if (world instanceof ServerLevel _level) {
				CompoundTag tag = player.getMainHandItem().getOrCreateTagElement("LostDepths");

				boolean newVal = !tag.getBoolean("UseDamage");
				tag.putBoolean("UseDamage", newVal);
				player.setItemInHand(InteractionHand.MAIN_HAND, player.getMainHandItem());
				player.displayClientMessage(Component.literal("Damage: " + (newVal ? "§2On" : "§4Off")), true);
			}
		}

		if (!player.isPassenger() && player.getMainHandItem().getItem() == LostdepthsModItems.GALAXY_DRAGON_STAFF.get()) {
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = LostdepthsModEntities.GALAXY_DRAGON.get().spawn(_level, BlockPos.containing(x, 1 + y, z), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setDeltaMovement(0, 0, 0);
				}
			}
		}
		if (player.isPassenger() && player.getMainHandItem().getItem() == LostdepthsModItems.GALAXY_DRAGON_STAFF.get()) {
			GalaxyDragonStaffRightclickedOnBlockProcedure.execute(player);
		}
	}
}
