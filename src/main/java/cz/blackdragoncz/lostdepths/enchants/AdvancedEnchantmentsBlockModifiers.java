package cz.blackdragoncz.lostdepths.enchants;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cz.blackdragoncz.lostdepths.init.LostdepthsModEnchantments;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Map;

public class AdvancedEnchantmentsBlockModifiers extends LootModifier {

    public static final Codec<AdvancedEnchantmentsBlockModifiers> CODEC = RecordCodecBuilder.create(inst ->
            codecStart(inst).apply(inst, AdvancedEnchantmentsBlockModifiers::new)
    );

    protected AdvancedEnchantmentsBlockModifiers(LootItemCondition[] conditions) {
        super(conditions);
    }


    @Nonnull
    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @NotNull
    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!context.hasParam(LootContextParams.TOOL)) return generatedLoot;

        ItemStack tool = context.getParam(LootContextParams.TOOL);
        if (tool.isEmpty()) return generatedLoot;

        @SuppressWarnings("deprecation")
        int afLevel = EnchantmentHelper.getItemEnchantmentLevel(LostdepthsModEnchantments.ADVANCED_FORTUNE.get(), tool);
        if (afLevel <= 0) return generatedLoot;

        ItemStack fakeTool = tool.copy();

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
        enchantments.remove(LostdepthsModEnchantments.ADVANCED_FORTUNE.get());
        int newFortune = afLevel + 3;
        enchantments.put(Enchantments.BLOCK_FORTUNE, newFortune);
        EnchantmentHelper.setEnchantments(enchantments, fakeTool);

        ResourceLocation queriedId = context.getQueriedLootTableId();

        LootTable table = context.getLevel().getServer().getLootData().getLootTable(queriedId);
        if (table == LootTable.EMPTY) return generatedLoot;

        LootParams.Builder builder = new LootParams.Builder(context.getLevel())
                .withParameter(LootContextParams.ORIGIN, context.getParam(LootContextParams.ORIGIN))
                .withParameter(LootContextParams.TOOL, fakeTool)
                .withOptionalParameter(LootContextParams.THIS_ENTITY, context.getParamOrNull(LootContextParams.THIS_ENTITY))
                .withOptionalParameter(LootContextParams.BLOCK_STATE, context.getParamOrNull(LootContextParams.BLOCK_STATE))
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY, context.getParamOrNull(LootContextParams.BLOCK_ENTITY))
                .withOptionalParameter(LootContextParams.EXPLOSION_RADIUS, context.getParamOrNull(LootContextParams.EXPLOSION_RADIUS));

        LootParams newParams = builder.create(LootContextParamSets.BLOCK);

        ObjectArrayList<ItemStack> replaced = new ObjectArrayList<>();
        table.getRandomItems(newParams, replaced::add);

        return replaced;
    }
}
