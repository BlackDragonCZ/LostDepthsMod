package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class LightPuzzleControllerOnBlockRightClickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		double i = 0;
		double j = 0;
		boolean parser = false;
		ItemStack require = ItemStack.EMPTY;
		ItemStack result = ItemStack.EMPTY;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:lightpuzzlec")))) {
			parser = true;
			i = 1 + x;
			while (i <= 9 + x && parser == true) {
				j = 1 + z;
				while (j <= 8 + z && parser == true) {
					parser = new Object() {
						public boolean getValue(LevelAccessor world, BlockPos pos, String tag) {
							BlockEntity blockEntity = world.getBlockEntity(pos);
							if (blockEntity != null)
								return blockEntity.getPersistentData().getBoolean(tag);
							return false;
						}
					}.getValue(world, BlockPos.containing(i, y, j), "status");
					j = j + 1;
				}
				i = i + 1;
			}
			if (parser) {
				if (true) {
					require = new ItemStack(LostdepthsModItems.OVER_CHARGE_COIL.get());
					result = new ItemStack(LostdepthsModItems.GIGA_CHARGE_COIL.get());
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == require.getItem()) {
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = require;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (entity instanceof Player _player) {
							ItemStack _setstack = result;
							_setstack.setCount(1);
							ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
						}
					}
					LightPuzzleResetProcedure.execute(world, x, y, z);
				}
				if (true) {
					require = new ItemStack(LostdepthsModBlocks.RUINED_ARCH.get());
					result = new ItemStack(LostdepthsModItems.BLADE_OF_FORGOTTEN.get());
					if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == require.getItem()) {
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = require;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (entity instanceof Player _player) {
							ItemStack _setstack = result;
							_setstack.setCount(1);
							ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
						}
					}
					LightPuzzleResetProcedure.execute(world, x, y, z);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("Puzzle is not completed"), true);
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ItemStack.EMPTY.getItem()) {
			LightPuzzleResetProcedure.execute(world, x, y, z);
			LightPuzzleRandomProcedure.execute(world, x, y, z);
		}
	}
}
