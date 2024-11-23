package cz.blackdragoncz.lostdepths.config;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = LostdepthsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LostDepthsConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<Integer> THE_PROTECTOR_SPAWN_WEIGHT_VAL =
            BUILDER.comment("Specify the protector spawn weight")
                    .define("the_protector_spawn_weight", 3);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int THE_PROTECTOR_SPAWN_WEIGHT = 3;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        THE_PROTECTOR_SPAWN_WEIGHT = THE_PROTECTOR_SPAWN_WEIGHT_VAL.get();
    }

}