package cz.blackdragoncz.lostdepths.util;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class RecipeManager {

    private final ItemStack output;
    private final List<Ingredient> inputs;
    private final int energyRequired;
    private static final RecipeManager INSTANCE = new RecipeManager();

    @SubscribeEvent
    public void onRegisterRecipes(RecipeManagerLoadingEvent event) {

    }
}
