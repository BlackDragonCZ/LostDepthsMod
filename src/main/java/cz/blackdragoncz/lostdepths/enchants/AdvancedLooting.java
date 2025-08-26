package cz.blackdragoncz.lostdepths.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class AdvancedLooting extends AdvancedEnchantments{

    private static final Set<Item> VALID_ITEMS = new HashSet<>();

    public static boolean addValidItem(Item container) {
        return VALID_ITEMS.add(container);
    }

    public AdvancedLooting() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
        maxLevel = 3;
    }

    public boolean canApplyAtEnchantmentTable(ItemStack stack) {
        Item item = stack.getItem();
        return enable && stack.getItem() instanceof AxeItem || VALID_ITEMS.contains(item);
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 5;
    }

    @Override
    protected int maxDelegate(int level) {
        return getMinCost(level) + 50;
    }

    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean checkCompatibility(@NotNull Enchantment validity) {
        return super.checkCompatibility(validity) && validity != Enchantments.MOB_LOOTING;
    }
}
