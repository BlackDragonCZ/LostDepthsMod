
package cz.blackdragoncz.lostdepths.recipe.brewing;

import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;

import cz.blackdragoncz.lostdepths.init.LostdepthsModPotions;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class UltraHealRBrewingRecipe extends BrewingRecipe {
	public UltraHealRBrewingRecipe() {
		super(Ingredient.of(Items.POTION), Ingredient.of(Items.POTION), new ItemStack(Items.POTION));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(new UltraHealRBrewingRecipe()));
	}

	@Override
	public boolean isInput(ItemStack input) {
		Item inputItem = input.getItem();
		return (inputItem == Items.POTION || inputItem == Items.SPLASH_POTION || inputItem == Items.LINGERING_POTION) && PotionUtils.getPotion(input) == Potions.HEALING;
	}

	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return Ingredient.of(new ItemStack(LostdepthsModBlocks.DRUIDS_FLOWER.get())).test(ingredient);
	}

	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		if (isInput(input) && isIngredient(ingredient)) {
			return PotionUtils.setPotion(new ItemStack(input.getItem()), LostdepthsModPotions.ULTRA_HEAL_POTION.get());
		}
		return ItemStack.EMPTY;
	}
}
