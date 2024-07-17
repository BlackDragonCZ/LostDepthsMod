package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class GhostHunterItem extends SwordItem {
    public GhostHunterItem() {
        super(new Tier() {
            public int getUses() {
                return 0;
            }

            public float getSpeed() {
                return 4f;
            }

            public float getAttackDamageBonus() {
                return -0.5f;
            }

            public int getLevel() {
                return 1;
            }

            public int getEnchantmentValue() {
                return 0;
            }

            public Ingredient getRepairIngredient() {
                return Ingredient.of();
            }
        }, 3, -3f, new Item.Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("§6Deals 10% max health damage"));
        list.add(Component.literal("§5Right click to teleport up to 30 blocks to an entity."));
        list.add(Component.literal("§dOn arrival:"));
        list.add(Component.literal("§cDeal up to 50% max health damage to very close enemies"));
        list.add(Component.literal("§7§oDamage based on distance travelled."));
    }
}
