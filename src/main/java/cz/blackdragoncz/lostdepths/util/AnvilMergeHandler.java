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
        ItemStack left  = event.getLeft();
        ItemStack right = event.getRight();
        if (left.isEmpty() || right.isEmpty()) return;

        Map<Enchantment, Integer> rightMap = EnchantmentHelper.getEnchantments(right);
        boolean rightIsBook = right.is(Items.ENCHANTED_BOOK);

        Enchantment ADVANCED_FORTUNE = LostdepthsModEnchantments.ADVANCED_FORTUNE.get();
        Enchantment ADVANCED_LOOTING = LostdepthsModEnchantments.ADVANCED_LOOTING.get();

        Enchantment VANILLA_FORTUNE = Enchantments.BLOCK_FORTUNE;
        Enchantment VANILLA_LOOTING = Enchantments.MOB_LOOTING;

        int afRight = rightMap.getOrDefault(ADVANCED_FORTUNE, 0);
        int alRight = rightMap.getOrDefault(ADVANCED_LOOTING, 0);

        if (left.is(Items.ENCHANTED_BOOK)) return;

        if (afRight <= 0 && alRight <= 0) return;

        ItemStack out = left.copy();
        Map<Enchantment, Integer> result = new HashMap<>(EnchantmentHelper.getEnchantments(out));

        boolean changed = false;

        java.util.function.Function<MergeSpec, Boolean> mergeOne = specification -> {
            int rightLvl = rightMap.getOrDefault(specification.advanced, 0);
            if (rightLvl <= 0) return false;

            if (!specification.advanced.canEnchant(out)) return false;

            int leftLvl = result.getOrDefault(specification.advanced, 0);
            int merged;
            if (leftLvl > 0) {
                merged = (rightIsBook && leftLvl == rightLvl)
                        ? Math.min(specification.advanced.getMaxLevel(), leftLvl + 1)
                        : Math.max(leftLvl, rightLvl);
            } else {
                merged = rightLvl;
            }

            result.put(specification.advanced, merged);
            result.remove(specification.vanilla);
            return true;
        };

        boolean appliedAF = mergeOne.apply(new MergeSpec(ADVANCED_FORTUNE, VANILLA_FORTUNE));
        boolean appliedAL = mergeOne.apply(new MergeSpec(ADVANCED_LOOTING, VANILLA_LOOTING));

        changed = appliedAF || appliedAL;

        if (!changed) return;

        EnchantmentHelper.setEnchantments(result, out);

        if ((appliedAF && !ADVANCED_FORTUNE.canEnchant(out)) || (appliedAL && !ADVANCED_LOOTING.canEnchant(out))) return;

        int prior = left.getBaseRepairCost() + right.getBaseRepairCost();
        int afLvl = result.getOrDefault(ADVANCED_FORTUNE, 0);
        int alLvl = result.getOrDefault(ADVANCED_LOOTING, 0);
        int cost = 4 + (afLvl * 2) + (alLvl * 2) + prior;
        if (cost < 1) cost = 1;

        out.setRepairCost(Math.max(left.getBaseRepairCost(), 0) + 1);

        event.setOutput(out);
        event.setCost(cost);
        event.setMaterialCost(1);
    }

    private record MergeSpec(Enchantment advanced, Enchantment vanilla) {}
}
