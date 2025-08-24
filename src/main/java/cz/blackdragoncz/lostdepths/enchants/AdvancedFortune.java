package cz.blackdragoncz.lostdepths.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.HashSet;
import java.util.Set;

public class AdvancedFortune extends AdvancedEnchantments{

    private static final Set<Item> VALID_ITEMS = new HashSet<>();

    public static boolean addValidItem(Item container) {
        return VALID_ITEMS.add(container);
    }

    public AdvancedFortune() {
        super(Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.values());
        maxLevel = 3;
    }

    public boolean canApplyAtEnchantmentTable(ItemStack stack) {
        Item item = stack.getItem();
        return enable && stack.getItem() instanceof PickaxeItem || VALID_ITEMS.contains(item);
    }
}
