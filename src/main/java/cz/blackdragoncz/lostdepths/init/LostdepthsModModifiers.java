package cz.blackdragoncz.lostdepths.init;

import com.mojang.serialization.Codec;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.enchants.AdvancedEnchantmentsBlockModifiers;
import cz.blackdragoncz.lostdepths.enchants.AdvancedEnchantmentsLootModifiers;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LostdepthsModModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LostdepthsMod.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADVANCED_FORTUNE = REGISTRY.register("advanced_fortune",
            () -> AdvancedEnchantmentsBlockModifiers.CODEC);
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADVANCED_LOOTING = REGISTRY.register("advanced_looting",
            () -> AdvancedEnchantmentsLootModifiers.CODEC);
}
