
package cz.blackdragoncz.lostdepths.item.chemistry;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.init.LostdepthsModFluids;

public class ConcentratedAcidItem extends BucketItem {
	public ConcentratedAcidItem() {
		super(LostdepthsModFluids.CONCENTRATED_ACID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON));
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}
}
