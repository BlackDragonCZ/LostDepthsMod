package cz.blackdragoncz.lostdepths.enchants;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public abstract class AdvancedEnchantments extends Enchantment {

    protected boolean enable = true;

    protected boolean allowGenLoot = true;
    protected boolean allowBooks = true;
    protected boolean allowTreasure = false;
    protected boolean allowTrades = false;
    protected boolean allowEnchanting = true;

    protected int maxLevel = 3;

    protected AdvancedEnchantments(Rarity rarity, EnchantmentCategory type, EquipmentSlot[] slots) {
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
    public AdvancedEnchantments canApplyAtEnchantmentTable(boolean allowEnchanting) {
        this.allowEnchanting = allowEnchanting;
        return this;
    }
    //gets
    public boolean getEnchantableInEnchanting() { return allowEnchanting; }
    public boolean IsEnable() {
        return enable;
    }
    public boolean getAllowTreasure() {
        return allowTreasure;
    }
    public boolean getAllowTrades() {
        return allowTrades;
    }
    public boolean getAllowBooks() {
        return allowBooks;
    }
    public boolean getAllowGenLoot() {
        return allowGenLoot;
    }
    public int getMaxLevel() {
        return maxLevel;
    }

}
