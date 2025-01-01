package cz.blackdragoncz.lostdepths.item.security;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class SecurityPassItem extends Item {

    private int clearance;
    private int groupClearance;

    public SecurityPassItem(int clearanceNumber, char groupSignature) {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
        this.clearance = clearanceNumber;
        this.groupClearance = groupSignature;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("\u00A76Use on security gates to grant security clearance."));
    }

    public int getClearance() {
        return clearance;
    }
    public char getGroupClearance() {
        return groupClearance;
    }
}
