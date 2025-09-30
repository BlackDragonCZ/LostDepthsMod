
package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import com.google.common.collect.Multimap;
import cz.blackdragoncz.lostdepths.util.MiningSpeedPerk;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

import cz.blackdragoncz.lostdepths.procedures.CrystalizedPickaxeMakeItemGlowProcedure;
import org.jetbrains.annotations.NotNull;

public class ForgefirePickaxeItem extends PickaxeItem {
    private static final int MiningSpeedBonus = 5;

	public ForgefirePickaxeItem() {
		super(new Tier() {
			public int getUses() {
				return 0;
			}

			public float getSpeed() {
				return 2f;
			}

			public float getAttackDamageBonus() {
				return 2f;
			}

			public int getLevel() {
				return 1;
			}

			public int getEnchantmentValue() {
				return 15;
			}

			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}
		}, 1, -3f, new Item.Properties().fireResistant());
	}

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float base = super.getDestroySpeed(stack, state);
        return base > 1.0f ? base + MiningSpeedPerk.bonusForLevel(MiningSpeedBonus) : base;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return MiningSpeedPerk.withLine(super.getDefaultAttributeModifiers(slot), slot, this, MiningSpeedBonus);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.BLOCK_EFFICIENCY) return false;
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
	}


	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return CrystalizedPickaxeMakeItemGlowProcedure.execute();
	}
}
