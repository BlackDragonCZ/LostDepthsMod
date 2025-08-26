package cz.blackdragoncz.lostdepths.util;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilMergeHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;

        Enchantment AF      = LostdepthsModEnchantments.ADVANCED_FORTUNE.get();
        Enchantment FORTUNE = Enchantments.BLOCK_FORTUNE;
        Enchantment SILK_TOUCH = Enchantments.SILK_TOUCH;

        Map<Enchantment, Integer> leftMap  = EnchantmentHelper.getEnchantments(left);
        Map<Enchantment, Integer> rightMap = EnchantmentHelper.getEnchantments(right);

        int afRight = rightMap.getOrDefault(AF, 0);
        if (afRight <= 0) return;

        if (left.is(Items.ENCHANTED_BOOK)) return;
        if (!left.isEnchantable() && leftMap.isEmpty()) return;
        if (!AF.canEnchant(left)) return;

        ItemStack out = left.copy();

        Map<Enchantment, Integer> result = new HashMap<>(leftMap);

        boolean rightIsBook = right.is(Items.ENCHANTED_BOOK);
        for (Map.Entry<Enchantment, Integer> en : rightMap.entrySet()) {
            Enchantment ench = en.getKey();
            if (ench == AF) continue;

            int rLvl = en.getValue();
            int lLvl = result.getOrDefault(ench, 0);
            int merged;

            if (rightIsBook && lLvl == rLvl && rLvl > 0) {
                merged = Math.min(ench.getMaxLevel(), rLvl + 1);
            } else {
                merged = Math.max(lLvl, rLvl);
            }

            if (merged > 0) result.put(ench, merged); else result.remove(ench);
        }

        int afLeft  = result.getOrDefault(AF, 0);
        int mergedAF;
        if (afLeft > 0) {
            if (rightIsBook && afLeft == afRight) {
                mergedAF = Math.min(AF.getMaxLevel(), afLeft + 1);
            } else {
                mergedAF = Math.max(afLeft, afRight);
            }
        } else {
            mergedAF = afRight;
        }

        if (mergedAF > 0) result.put(AF, mergedAF); else result.remove(AF);
        result.remove(FORTUNE);

        EnchantmentHelper.setEnchantments(result, out);

        if (!AF.canEnchant(out)) return;

        int prior = left.getBaseRepairCost() + right.getBaseRepairCost();
        boolean fortuneWasPresent = leftMap.containsKey(FORTUNE) || leftMap.containsKey(SILK_TOUCH);
        int cost = 4 + (mergedAF * 2) + (fortuneWasPresent ? 2 : 0) + prior;
        if (cost < 1) cost = 1;

        event.setOutput(out);
        event.setCost(cost);
        event.setMaterialCost(1);
    }
}
