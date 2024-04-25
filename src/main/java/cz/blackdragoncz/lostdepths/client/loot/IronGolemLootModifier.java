package cz.blackdragoncz.lostdepths.client.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;


public class IronGolemLootModifier extends LootModifier {

    public static final Supplier<Codec<IronGolemLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, IronGolemLootModifier::new)));
    public static final ResourceLocation INJECTABLE_LOOT = new ResourceLocation(LostdepthsMod.MODID, "loot/iron_golem_loot_table");

    public IronGolemLootModifier(final LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> loot, LootContext context) {
        context.getLevel().getServer().getLootData().getLootTable(INJECTABLE_LOOT).getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), loot::add));
        return loot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

}
