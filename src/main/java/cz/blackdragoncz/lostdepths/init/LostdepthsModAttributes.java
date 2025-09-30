package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.entity.ai.attributes.Attribute;

public final class LostdepthsModAttributes {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, LostdepthsMod.MODID);
    public static final RegistryObject<Attribute> MINING_SPEED = REGISTRY.register("mining_speed", () -> new RangedAttribute("attribute.name.lostdepths.mining_speed", 0.0D, 0.0D, 1024.0D)
            .setSyncable(true));
}
