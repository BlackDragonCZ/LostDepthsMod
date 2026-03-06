package cz.blackdragoncz.lostdepths.item.book;

import cz.blackdragoncz.lostdepths.client.gui.InfusedBookViewScreen;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.network.ContractSignMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class InfusedWrittenBookItem extends Item {

	public InfusedWrittenBookItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			// Contract signing
			if (level.isClientSide()) {
				ContractSignMessage.sendToServer(hand);
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}
		if (level.isClientSide()) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openBookScreen(stack));
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	private static void openBookScreen(ItemStack stack) {
		net.minecraft.client.Minecraft.getInstance().setScreen(new InfusedBookViewScreen(stack));
	}

	public static boolean makeSureTagIsValid(@Nullable CompoundTag tag) {
		if (!InfusedWritableBookItem.makeSureTagIsValid(tag)) return false;
		if (!tag.contains("title", 8)) return false;
		String title = tag.getString("title");
		return title.length() <= 32 && tag.contains("author", 8);
	}

	@Override
	public Component getName(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag != null) {
			String title = tag.getString("title");
			if (!StringUtil.isNullOrEmpty(title)) {
				return Component.literal(title);
			}
		}
		return super.getName(stack);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		if (stack.hasTag()) {
			CompoundTag tag = stack.getTag();
			String author = tag.getString("author");
			if (!StringUtil.isNullOrEmpty(author)) {
				tooltip.add(Component.translatable("book.byAuthor", author).withStyle(ChatFormatting.GRAY));
			}
			int generation = tag.getInt("generation");
			if (generation > 0) {
				tooltip.add(Component.translatable("book.generation." + generation).withStyle(ChatFormatting.GRAY));
			}
			if (tag.contains("contract_signer")) {
				String signer = tag.getString("contract_signer");
				tooltip.add(Component.literal("Signed by: ").withStyle(ChatFormatting.GRAY)
						.append(Component.literal(signer).withStyle(ChatFormatting.GOLD)));
			}
		}
	}

	public boolean canBeCopied(ItemStack stack) {
		CompoundTag tag = stack.getTag();
		if (tag == null) return false;
		// Cannot copy signed books
		if (tag.contains("contract_signer")) return false;
		// Cannot copy generation 2+
		int gen = tag.getInt("generation");
		return gen < 2;
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
