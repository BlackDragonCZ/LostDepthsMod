package cz.blackdragoncz.lostdepths.item.ingot;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class MultiversitePlatesItem extends Item {
    public MultiversitePlatesItem() {
        super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.COMMON));
    }

}
