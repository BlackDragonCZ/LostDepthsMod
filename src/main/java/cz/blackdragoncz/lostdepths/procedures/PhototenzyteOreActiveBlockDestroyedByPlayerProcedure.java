package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class PhototenzyteOreActiveBlockDestroyedByPlayerProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.CELESTIAL_PICKAXE.get()) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(LostdepthsModItems.PHOTOTENZYTE.get());
				_setstack.setCount(1);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
			if (3 > Mth.nextInt(RandomSource.create(), 0, 10)) {
				world.setBlock(BlockPos.containing(x, y, z), LostdepthsModBlocks.PHOTOTENZYTE_ORE.get().defaultBlockState(), 3);
			} else {
				world.setBlock(BlockPos.containing(x, y, z), LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE.get().defaultBlockState(), 3);
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.NIGHTMARE_PICKAXE.get()) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = new ItemStack(LostdepthsModItems.PHOTOTENZYTE.get());
				_setstack.setCount(3);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
			if (3 > Mth.nextInt(RandomSource.create(), 0, 10)) {
				world.setBlock(BlockPos.containing(x, y, z), LostdepthsModBlocks.PHOTOTENZYTE_ORE.get().defaultBlockState(), 3);
			} else {
				world.setBlock(BlockPos.containing(x, y, z), LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE.get().defaultBlockState(), 3);
			}
		} else {
			world.setBlock(BlockPos.containing(x, y, z), LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE.get().defaultBlockState(), 3);
		}
	}
}
