package cz.blackdragoncz.lostdepths.item.tool;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AspectOfTheStar extends SwordItem {

    public boolean toggleDamage = false;
    public String keybindToggle = "Not Bind";


    public AspectOfTheStar() {
        super(new Tier() {
            public int getUses() {
                return 0;
            }

            public float getSpeed() {
                return 1f;
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

            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of();
            }
        }, 18, -2f, new Item.Properties().fireResistant());
    }
/*
    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity) {
       boolean retval = super.hurtEnemy(itemstack, entity, sourceentity);
        BaneOfVenomsLivingEntityIsHitWithToolProcedure.execute(entity);
        return retval;
    }*/

    public static double onRightClick(LivingEntity entity, boolean toggleDamage) {
        double enemyHealth;
        double trueDamageValue;

        if (entity instanceof LivingEntity) {
            enemyHealth = (entity.getMaxHealth() + entity.getArmorValue()) * 0.08;
            trueDamageValue = entity.getMaxHealth() * 0.02;

        }

       // return enemyHealth;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("§5Right Click: Teleport up to 8 blocks ahead of you,"));
        list.add(Component.literal("§5deals 8% Max Health of target as damage"));
        list.add(Component.literal("§dDeals 2% True Damage to entities near the trail"));

        list.add(Component.literal("§6Toggle damage by teleport ability with §2" + keybindToggle + "."));
        list.add(Component.literal("§6Damage: §4" + toggleDamage));
    }
}
