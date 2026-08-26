package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.init.LostdepthsModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

/** Metal look, wooden-door behaviour. Hand-opening comes from INFUSED_IRON_SET, not from this class. */
public class InfusedIronDoorBlock extends DoorBlock {

	public InfusedIronDoorBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.COLOR_LIGHT_BLUE)
				.sound(SoundType.METAL)
				.strength(3f, 7f) // wooden-door break speed, iron-door blast resistance +2
				.requiresCorrectToolForDrops()
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY),
				LostdepthsModWoodTypes.INFUSED_IRON_SET);
	}

	// Window lets some light past a closed door. 15 would block fully, 0 not at all; tune this value to taste.
	@Override
	public int getLightBlock(BlockState state, BlockGetter level, BlockPos pos) {
		return state.getValue(OPEN) ? 0 : 4;
	}

	// Lower half only: breaking the upper half destroys the lower one *with* drops, so both would duplicate.
	@Override
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		if (state.getValue(HALF) != DoubleBlockHalf.LOWER) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(this, 1));
	}
}
