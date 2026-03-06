package cz.blackdragoncz.lostdepths.item.book;

import cz.blackdragoncz.lostdepths.client.gui.InfusedBookEditScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;

public class InfusedWritableBookItem extends Item {

	public InfusedWritableBookItem() {
		super(new Item.Properties().stacksTo(1));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide()) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openBookScreen(player, stack, hand));
		}
		player.awardStat(Stats.ITEM_USED.get(this));
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	private static void openBookScreen(Player player, ItemStack stack, InteractionHand hand) {
		net.minecraft.client.Minecraft.getInstance().setScreen(new InfusedBookEditScreen(player, stack, hand));
	}

	public static boolean makeSureTagIsValid(@Nullable CompoundTag tag) {
		if (tag == null) return false;
		if (!tag.contains("pages", 9)) return false;
		ListTag pages = tag.getList("pages", 8);
		for (int i = 0; i < pages.size(); i++) {
			if (pages.getString(i).length() > 32767) return false;
		}
		return true;
	}
}
