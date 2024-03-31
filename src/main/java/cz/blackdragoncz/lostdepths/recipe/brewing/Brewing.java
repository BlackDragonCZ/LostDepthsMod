
package cz.blackdragoncz.lostdepths.recipe.brewing;

import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Brewing {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.ACID_TONGUE.get())),
					new ItemStack(LostdepthsModItems.ACIDBLOOD_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.INFUSED_REDSTONE.get())),
					new ItemStack(LostdepthsModItems.CORRUPTED_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(LostdepthsModItems.MADRIGEN_SOLUTION.get())),
					Ingredient.of(new ItemStack(LostdepthsModItems.POWER_CORE.get())),
					new ItemStack(LostdepthsModItems.ENDER_VOLTAIC_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.EXPLOSIVE_SAC.get())),
					new ItemStack(LostdepthsModItems.FIREBLOOD_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.GLOWING_SAC.get())),
					new ItemStack(LostdepthsModItems.GLOWBLOOD_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(LostdepthsModItems.MAGMA_SOLUTION.get())),
					Ingredient.of(new ItemStack(LostdepthsModItems.CORROSIVE_TEAR.get())),
					new ItemStack(LostdepthsModItems.MADRIGEN_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.MAGMA_COMPOUND.get())),
					new ItemStack(LostdepthsModItems.MAGMA_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.NIGHTMARE_POWDER.get())),
					new ItemStack(LostdepthsModItems.NIGHTMARE_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.DUSKER_EGGS.get())),
					new ItemStack(LostdepthsModItems.POLYCHARGE_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.EMBER_SAC.get())),
					new ItemStack(LostdepthsModItems.QUICKFLAME_SOLUTION.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(LostdepthsModItems.POLY_AMPLIFICATION_SOLUTION.get())),
					Ingredient.of(new ItemStack(LostdepthsModItems.BLAZING_STAR.get())),
					new ItemStack(LostdepthsModItems.VIAL_OF_SOLARUM.get())
			));

			BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
					Ingredient.of(new ItemStack(Items.POTION)),
					Ingredient.of(new ItemStack(LostdepthsModItems.VOLATILE_BLOOD.get())),
					new ItemStack(LostdepthsModItems.VOLATILITY_SOLUTION.get())
			));


		});
	}
}
