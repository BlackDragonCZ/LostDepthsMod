package cz.blackdragoncz.lostdepths.item.chemistry;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BucketItem;

import cz.blackdragoncz.lostdepths.init.LostdepthsModFluids;

public class ConcentratedAcidItem extends BucketItem {
	public ConcentratedAcidItem() {
		super(LostdepthsModFluids.CONCENTRATED_ACID, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON));
	}
}
