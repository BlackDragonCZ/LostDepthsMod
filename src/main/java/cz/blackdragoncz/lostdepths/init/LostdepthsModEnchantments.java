package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.enchants.AdvancedEnchantments;
import cz.blackdragoncz.lostdepths.enchants.AdvancedFortune;
import cz.blackdragoncz.lostdepths.enchants.AdvancedLooting;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LostdepthsModEnchantments {
    public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, LostdepthsMod.MODID);
    public static final RegistryObject<AdvancedEnchantments> ADVANCED_FORTUNE = REGISTRY.register("advanced_fortune", AdvancedFortune::new);
    public static final RegistryObject<AdvancedEnchantments> ADVANCED_LOOTING = REGISTRY.register("advanced_looting", AdvancedLooting::new);
}
