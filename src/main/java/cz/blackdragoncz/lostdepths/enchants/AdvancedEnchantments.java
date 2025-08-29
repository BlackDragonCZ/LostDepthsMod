package cz.blackdragoncz.lostdepths.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AdvancedEnchantments extends Enchantment {

    // private final AdvancedEnchantments.RarityLvl rarityLvl;

    protected boolean enable = true;

    protected boolean allowGenLoot = false;
    protected boolean allowBooks = true;
    protected boolean allowTreasure = false;
    protected boolean allowTrades = false;
    protected boolean allowEnchanting = false;

    protected int maxLevel = 3;

    protected AdvancedEnchantments(Rarity rarity, EnchantmentCategory type, EquipmentSlot... slots) {
        super(rarity, type, slots);
    }
    //setup
    public AdvancedEnchantments setEnable(boolean enable) {
        this.enable = enable;
        return this;
    }
    public AdvancedEnchantments setAllowTreasure(boolean allowTreasure) {
        this.allowTreasure = allowTreasure;
        return this;
    }
    public AdvancedEnchantments setAllowTrades(boolean allowTrades) {
        this.allowTrades = allowTrades;
        return this;
    }
    public AdvancedEnchantments setAllowBooks(boolean allowBooks) {
        this.allowBooks = allowBooks;
        return this;
    }
    public AdvancedEnchantments setAllowGenLoot(boolean allowGenLoot) {
        this.allowGenLoot = allowGenLoot;
        return this;
    }
    public AdvancedEnchantments setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }
    //gets

    public boolean allowEnchantingTable() { return allowEnchanting; }
    public boolean IsEnable() {
        return enable;
    }
    @Override
    public boolean isTreasureOnly() {
        return allowTreasure;
    }

    @Override
    public boolean isTradeable() {
        return enable && allowTrades;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return enable && allowBooks;
    }
    public boolean getAllowGenLoot() {
        return enable && allowGenLoot;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMaxCost(int level) {
        return enable ? maxDelegate(level) : -1;
    }

    protected int maxDelegate(int level) {
        return getMinCost(level) + 5;
    }
    /*
    public AdvancedEnchantments.RarityLvl getRarity() {
        return this.rarityLvl;
    }

    public static enum RarityLvl {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1),
        COSMIC(1);

        private final int weight;

        private RarityLvl(int pWeight) {
            this.weight = pWeight;
        }
         // Retrieves the weight of Rarity.
        public int getWeight() {
            return this.weight;
        }
    }*/

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return allowEnchanting && super.canApplyAtEnchantingTable(stack);
    }
}
