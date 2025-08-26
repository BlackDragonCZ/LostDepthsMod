package cz.blackdragoncz.lostdepths.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AdvancedEnchantments extends Enchantment {

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
    public boolean getEnchantableInEnchanting() { return allowEnchanting; }
    public boolean IsEnable() {
        return enable;
    }

    public boolean getAllowTreasureOnly() {
        return allowTreasure;
    }
    public boolean getAllowTrades() {
        return enable && allowTrades;
    }
    public boolean getAllowBooks() {
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

    public boolean canApplyAtEnchantmentTable(ItemStack stack) {
        return allowEnchanting && super.canApplyAtEnchantingTable(stack);
    }
}
