package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.recipe.LDCraftingRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

public class CustomResultSlot<RECIPE extends LDCraftingRecipe> extends ResultSlot {

    private final RecipeType<RECIPE> recipeType;
    private final AbstractContainerMenu containerMenu;

    // Our own copy of ResultSlot's private removeCount, so checkTakeAchievements below can stay
    // independent of the superclass.
    private int craftedCount;

    public CustomResultSlot(RecipeType<RECIPE> recipeType, AbstractContainerMenu containerMenu, Player pPlayer, CraftingContainer pCraftSlots, Container pContainer, int pSlot, int pXPosition, int pYPosition) {
        super(pPlayer, pCraftSlots, pContainer, pSlot, pXPosition, pYPosition);
        this.recipeType = recipeType;
        this.containerMenu = containerMenu;
    }

    @Override
    public ItemStack remove(int pAmount) {
        if (this.hasItem())
            this.craftedCount += Math.min(pAmount, this.getItem().getCount());
        return super.remove(pAmount);
    }

    @Override
    protected void onQuickCraft(ItemStack pStack, int pAmount) {
        this.craftedCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    @Override
    protected void onSwapCraft(int pNumItemsCrafted) {
        this.craftedCount += pNumItemsCrafted;
    }

    /*
     * Reimplemented rather than calling super, and it must stay that way.
     *
     * FastSuite and Polymorph inject into ResultSlot.checkTakeAchievements and re-run a *vanilla*
     * RecipeType.CRAFTING lookup against craftSlots. Our grids are not 3x3 - the Module Creator is
     * 4 wide by 1 tall - and FastSuite's cache indexed straight off the end of it, crashing the client
     * with ArrayIndexOutOfBoundsException on every take. That lookup is meaningless to us anyway: our
     * result comes from our own recipe type, not from vanilla crafting.
     */
    @Override
    protected void checkTakeAchievements(ItemStack pStack) {
        if (this.craftedCount > 0) {
            pStack.onCraftedBy(this.player.level(), this.player, this.craftedCount);
            net.minecraftforge.event.ForgeEventFactory.firePlayerCraftingEvent(this.player, pStack, this.craftSlots);
        }
        if (this.container instanceof RecipeHolder recipeHolder)
            recipeHolder.awardUsedRecipes(this.player, this.craftSlots.getItems());
        this.craftedCount = 0;
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
