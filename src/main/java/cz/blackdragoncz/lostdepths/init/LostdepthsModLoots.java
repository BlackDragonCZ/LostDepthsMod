package cz.blackdragoncz.lostdepths.init;

import com.mojang.serialization.Codec;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.loot.CaveSpiderLootModifier;
import cz.blackdragoncz.lostdepths.client.loot.IronGolemLootModifier;
import cz.blackdragoncz.lostdepths.client.loot.SpiderLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LostdepthsModLoots {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LostdepthsMod.MODID);

    public static final RegistryObject<Codec<IronGolemLootModifier>> IRON_LOOTINJECT_ENTITY = REGISTER.register("iron_lootinject_entity", IronGolemLootModifier.CODEC);
    public static final RegistryObject<Codec<CaveSpiderLootModifier>> CAVE_SPIDER_LOOTINJECT_ENTITY = REGISTER.register("cave_spider_lootinject_entity", CaveSpiderLootModifier.CODEC);
    public static final RegistryObject<Codec<SpiderLootModifier>> SPIDER_LOOTINJECT_ENTITY = REGISTER.register("spider_lootinject_entity", SpiderLootModifier.CODEC);
}