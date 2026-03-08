package cz.blackdragoncz.lostdepths.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShipmentBoxItem extends Item {

    public static final int MAX_WEIGHT = 100;

    public ShipmentBoxItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int weight = getWeight(stack);
        tooltip.add(Component.literal("Weight: " + weight + "/" + MAX_WEIGHT + " lbs").withStyle(ChatFormatting.GRAY));

        ListTag items = getStoredItems(stack);
        if (!items.isEmpty()) {
            tooltip.add(Component.literal("Contents: " + items.size() + " items").withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public static int getWeight(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return 0;
        return tag.getInt("ShipmentWeight");
    }

    public static void setWeight(ItemStack stack, int weight) {
        stack.getOrCreateTag().putInt("ShipmentWeight", weight);
    }

    public static ListTag getStoredItems(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) return new ListTag();
        return tag.getList("ShipmentItems", Tag.TAG_COMPOUND);
    }

    public static void addStoredItem(ItemStack box, String itemId, int weight) {
        CompoundTag tag = box.getOrCreateTag();
        ListTag items = tag.getList("ShipmentItems", Tag.TAG_COMPOUND);
        CompoundTag entry = new CompoundTag();
        entry.putString("Item", itemId);
        entry.putInt("Weight", weight);
        items.add(entry);
        tag.put("ShipmentItems", items);
    }

    public static boolean canAddWeight(ItemStack box, int additionalWeight) {
        return getWeight(box) + additionalWeight <= MAX_WEIGHT;
    }
}
