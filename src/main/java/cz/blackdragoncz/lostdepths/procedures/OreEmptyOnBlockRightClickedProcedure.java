package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModOres;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

/** Holding a celestial tool and right-clicking an empty node reports how long it still needs to regrow. */
public class OreEmptyOnBlockRightClickedProcedure {

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (!(entity instanceof Player player) || player.level().isClientSide()) return;

		ItemStack held = entity instanceof LivingEntity liv ? liv.getMainHandItem() : ItemStack.EMPTY;
		if (!held.is(ItemTags.create(new ResourceLocation("lostdepths:celestial_tools")))) return;

		BlockEntity be = world.getBlockEntity(BlockPos.containing(x, y, z));
		double elapsed = be != null ? be.getPersistentData().getDouble("timeLeft") : 0;
		int remaining = (int) Math.max(0, LostdepthsModOres.REGROW_SECONDS - elapsed);

		player.displayClientMessage(Component.literal(remaining + " seconds for regrow."), false);
	}
}
