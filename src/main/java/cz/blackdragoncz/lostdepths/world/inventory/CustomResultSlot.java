package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.recipe.LDCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CustomResultSlot<RECIPE extends LDCraftingRecipe> extends ResultSlot {

    private final RecipeType<RECIPE> recipeType;
    private final AbstractContainerMenu containerMenu;

    public CustomResultSlot(RecipeType<RECIPE> recipeType, AbstractContainerMenu containerMenu, Player pPlayer, CraftingContainer pCraftSlots, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, pCraftSlots, pContainer, pSlot, pXPosition, pYPosition);
        this.recipeType = recipeType;
        this.containerMenu = containerMenu;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
        NonNullList<ItemStack> nonnulllist = pPlayer.level().getRecipeManager().getRemainingItemsFor(recipeType, craftSlots, pPlayer.level());
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack itemstack = craftSlots.getItem(i);
            ItemStack itemstack1 = nonnulllist.get(i);
            if (!itemstack.isEmpty()) {
                craftSlots.removeItem(i, 1);
                itemstack = craftSlots.getItem(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    craftSlots.setItem(i, itemstack1);
                } else if (ItemStack.isSameItemSameTags(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    craftSlots.setItem(i, itemstack1);
                } else if (!player.getInventory().add(itemstack1)) {
                    player.drop(itemstack1, false);
                }
            }
        }

        containerMenu.slotsChanged(craftSlots);
    }
}
