package cz.blackdragoncz.lostdepths.item.shield;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MirrorShield extends Item {
    public MirrorShield(){
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("§bWhen held in the offhand, reflect 100% of max health damage taken."));
        list.add(Component.literal("§6WIP not work right now"));
    }
}
