package cz.blackdragoncz.lostdepths.item.part;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class MasterPartRegistryItem extends Item {
    private String description;

    public MasterPartRegistryItem(int itemMaxStacks, @Nullable String description) {
        super(new Item.Properties().stacksTo(itemMaxStacks).fireResistant().rarity(Rarity.COMMON));
        this.description = description;
    }

    public MasterPartRegistryItem(int itemMaxStacks) {
        super(new Item.Properties().stacksTo(itemMaxStacks).rarity(Rarity.COMMON));
    }

    public MasterPartRegistryItem() {
        this(64);
    }

    @Override
    @Nullable
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        if (description != null) {
            list.add(Component.literal(description));
        }
    }
}
