package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.ItemHandlerHelper;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class VarlleriumOreBlockBreakedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		BlockState blockType = Blocks.AIR.defaultBlockState();
		double dropRate = 0;
		double dropRate2 = 0;
		ItemStack toolType = ItemStack.EMPTY;
		ItemStack dropItem = ItemStack.EMPTY;
		ItemStack toolType2 = ItemStack.EMPTY;
		String oreData = "";
		blockType = LostdepthsModBlocks.VARLLERIUM_ORE.get().defaultBlockState();
		oreData = "varllerium";
		dropItem = new ItemStack(LostdepthsModItems.RAW_VARLLERIUM.get());
		toolType = new ItemStack(LostdepthsModItems.CELESTIAL_PICKAXE.get());
		toolType2 = new ItemStack(LostdepthsModItems.NIGHTMARE_PICKAXE.get());
		dropRate = 1;
		dropRate2 = 3;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == toolType.getItem()) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = dropItem;
				_setstack.setCount((int) dropRate);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
			{
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockState _bs = LostdepthsModBlocks.ORE_EMPTY.get().defaultBlockState();
				BlockEntity _be = world.getBlockEntity(_bp);
				CompoundTag _bnbt = null;
				if (_be != null) {
					_bnbt = _be.saveWithFullMetadata();
					_be.setRemoved();
				}
				world.setBlock(_bp, _bs, 3);
				if (_bnbt != null) {
					_be = world.getBlockEntity(_bp);
					if (_be != null) {
						try {
							_be.load(_bnbt);
						} catch (Exception ignored) {
						}
					}
				}
			}
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putString("oreType", oreData);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == toolType2.getItem()) {
			if (entity instanceof Player _player) {
				ItemStack _setstack = dropItem;
				_setstack.setCount((int) dropRate2);
				ItemHandlerHelper.giveItemToPlayer(_player, _setstack);
			}
			{
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockState _bs = LostdepthsModBlocks.ORE_EMPTY.get().defaultBlockState();
				BlockEntity _be = world.getBlockEntity(_bp);
				CompoundTag _bnbt = null;
				if (_be != null) {
					_bnbt = _be.saveWithFullMetadata();
					_be.setRemoved();
				}
				world.setBlock(_bp, _bs, 3);
				if (_bnbt != null) {
					_be = world.getBlockEntity(_bp);
					if (_be != null) {
						try {
							_be.load(_bnbt);
						} catch (Exception ignored) {
						}
					}
				}
			}
			if (!world.isClientSide()) {
				BlockPos _bp = BlockPos.containing(x, y, z);
				BlockEntity _blockEntity = world.getBlockEntity(_bp);
				BlockState _bs = world.getBlockState(_bp);
				if (_blockEntity != null)
					_blockEntity.getPersistentData().putString("oreType", oreData);
				if (world instanceof Level _level)
					_level.sendBlockUpdated(_bp, _bs, _bs, 3);
			}
		} else {
			world.setBlock(BlockPos.containing(x, y, z), blockType, 3);
		}
	}
}
