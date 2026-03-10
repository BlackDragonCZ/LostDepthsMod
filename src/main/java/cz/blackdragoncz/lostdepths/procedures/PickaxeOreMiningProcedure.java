package cz.blackdragoncz.lostdepths.procedures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.DepletionType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.OreDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Unified handler for all pickaxe ore mining (onDestroyedByPlayer).
 * Replaces the individual per-ore MCreator procedures.
 */
public class PickaxeOreMiningProcedure {

	/**
	 * Called when a player mines an active/mineable ore.
	 * Determines drops from the ore registry and handles depletion.
	 */
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, OreDefinition oreDef) {
		if (entity == null) return;

		Item heldItem = (entity instanceof LivingEntity liv ? liv.getMainHandItem() : ItemStack.EMPTY).getItem();
		int dropCount = oreDef.getDropCount(heldItem);
		BlockPos pos = BlockPos.containing(x, y, z);

		if (dropCount > 0 && entity instanceof Player player) {
			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(oreDef.dropItem().get(), dropCount));

			switch (oreDef.depletionType()) {
				case TO_EMPTY -> {
					setBlockPreserveNBT(world, pos, LostdepthsModBlocks.ORE_EMPTY.get().defaultBlockState());
					if (oreDef.oreEmptyTag() != null && !world.isClientSide()) {
						BlockEntity be = world.getBlockEntity(pos);
						if (be != null) {
							be.getPersistentData().putString("oreType", oreDef.oreEmptyTag());
							if (world instanceof Level level) {
								BlockState bs = world.getBlockState(pos);
								level.sendBlockUpdated(pos, bs, bs, 3);
							}
						}
					}
				}
				case CHANCE_DEACTIVATE -> {
					if (oreDef.unpoweredBlock() != null && 3 > Mth.nextInt(RandomSource.create(), 0, 10)) {
						world.setBlock(pos, oreDef.unpoweredBlock().get().defaultBlockState(), 3);
					} else if (oreDef.activeBlock() != null) {
						world.setBlock(pos, oreDef.activeBlock().get().defaultBlockState(), 3);
					}
				}
				case TO_BEDROCK -> {
					world.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3);
				}
			}
		} else {
			// Wrong pickaxe: restore the ore block
			resetOre(world, pos, oreDef);
		}
	}

	/**
	 * Called when a dormant/unpowered ore is mined or exploded.
	 * Simply restores the block — no drops.
	 */
	public static void executeDormant(LevelAccessor world, double x, double y, double z, OreDefinition oreDef) {
		BlockPos pos = BlockPos.containing(x, y, z);
		if (oreDef.unpoweredBlock() != null) {
			setBlockPreserveNBT(world, pos, oreDef.unpoweredBlock().get().defaultBlockState());
		}
	}

	private static void resetOre(LevelAccessor world, BlockPos pos, OreDefinition oreDef) {
		if (oreDef.depletionType() == DepletionType.CHANCE_DEACTIVATE && oreDef.activeBlock() != null) {
			world.setBlock(pos, oreDef.activeBlock().get().defaultBlockState(), 3);
		} else {
			// For TO_EMPTY / TO_BEDROCK ores, the "active" block is the ore itself
			// Use description ID match to find the correct block from registry
			String descId = "block.lostdepths." + oreDef.id();
			for (var entry : net.minecraftforge.registries.ForgeRegistries.BLOCKS.getEntries()) {
				if (entry.getValue().getDescriptionId().equals(descId)) {
					world.setBlock(pos, entry.getValue().defaultBlockState(), 3);
					return;
				}
			}
		}
	}

	private static void setBlockPreserveNBT(LevelAccessor world, BlockPos pos, BlockState newState) {
		BlockEntity be = world.getBlockEntity(pos);
		CompoundTag nbt = null;
		if (be != null) {
			nbt = be.saveWithFullMetadata();
			be.setRemoved();
		}
		world.setBlock(pos, newState, 3);
		if (nbt != null) {
			be = world.getBlockEntity(pos);
			if (be != null) {
				try { be.load(nbt); } catch (Exception ignored) {}
			}
		}
	}
}
