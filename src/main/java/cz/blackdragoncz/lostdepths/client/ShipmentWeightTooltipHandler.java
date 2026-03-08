package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.client.gui.ShipmentFillerScreen;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.recipe.ShipmentFillerRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ShipmentWeightTooltipHandler {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        ItemStack hoveredItem = event.getItemStack();
        if (hoveredItem.isEmpty()) return;

        // Only show weight if player has shipment_box in inventory or shipment filler GUI is open
        boolean hasBox = false;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (player.getInventory().getItem(i).getItem() == LostdepthsModItems.SHIPMENT_BOX.get()) {
                hasBox = true;
                break;
            }
        }

        if (!hasBox) {
            Minecraft mc = Minecraft.getInstance();
            if (!(mc.screen instanceof ShipmentFillerScreen)) {
                return;
            }
        }

        // Find weight for this item from recipes
        if (player.level() == null) return;
        List<ShipmentFillerRecipe> recipes = LostDepthsModRecipeType.SHIPMENT_FILLER.get().getRecipes(player.level());
        for (ShipmentFillerRecipe recipe : recipes) {
            if (recipe.getItem().getItem() == hoveredItem.getItem()) {
                event.getToolTip().add(Component.literal("Shipment Weight: " + recipe.getWeight() + " lbs").withStyle(ChatFormatting.GOLD));
                return;
            }
        }
    }
}
