package cz.blackdragoncz.lostdepths.init;

import com.mojang.serialization.Codec;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.client.loot.IronGolemLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LostdepthsModLoots {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> REGISTER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, LostdepthsMod.MODID);

    public static final RegistryObject<Codec<IronGolemLootModifier>> IRON_LOOTINJECT_ENTITY = REGISTER.register("iron_lootinject_entity", IronGolemLootModifier.CODEC);
}