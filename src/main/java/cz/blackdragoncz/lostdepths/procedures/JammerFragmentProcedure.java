package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

// Breaking a jammer yields its fragment and detonates. Shared by JammerA-D, which pass their own fragment.
public class JammerFragmentProcedure {

	private static final float BLAST_RADIUS = 12;

	public static void execute(LevelAccessor world, BlockPos pos, Entity entity, Item fragment) {
		// The fragment used to be handed out on both sides, which left a ghost item on the client.
		if (!(world instanceof Level level) || level.isClientSide())
			return;
		if (entity instanceof Player player)
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(fragment));
		level.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, BLAST_RADIUS, Level.ExplosionInteraction.BLOCK);
	}
}
