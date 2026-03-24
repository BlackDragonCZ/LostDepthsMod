package cz.blackdragoncz.lostdepths.block.storage;

import cz.blackdragoncz.lostdepths.block.entity.storage.NTControllerBlockEntity;
import cz.blackdragoncz.lostdepths.util.TextEffects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nullable;
import java.util.List;

public class NTControllerBlock extends Block implements EntityBlock {
	public NTControllerBlock() {
		super(BlockBehaviour.Properties.of()
				.mapColor(MapColor.STONE)
				.strength(3.5f)
				.sound(SoundType.STONE)
				.requiresCorrectToolForDrops());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new NTControllerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) return null;
		return (lvl, pos, st, be) -> {
			if (be instanceof NTControllerBlockEntity controller) {
				NTControllerBlockEntity.serverTick(lvl, pos, st, controller);
			}
		};
	}

	@Override
	public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> list, TooltipFlag flag) {
		long tick = 0;
		try {
			if (Minecraft.getInstance().level != null) {
				tick = Minecraft.getInstance().level.getGameTime();
			}
		} catch (Exception ignored) {}

		MutableComponent line = Component.empty();
		line.append(Component.literal("You just need ").withStyle(ChatFormatting.GRAY));
		line.append(TextEffects.rainbow("imagination", tick));
		line.append(Component.literal(" to use this.").withStyle(ChatFormatting.GRAY));
		list.add(line);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean movedByPiston) {
		super.neighborChanged(state, level, pos, block, neighborPos, movedByPiston);
		if (!level.isClientSide() && level.getBlockEntity(pos) instanceof NTControllerBlockEntity controller) {
			controller.onNeighborChanged();
		}
	}
}
