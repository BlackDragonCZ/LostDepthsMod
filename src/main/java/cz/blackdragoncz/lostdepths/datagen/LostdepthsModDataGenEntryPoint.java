package cz.blackdragoncz.lostdepths.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class LostdepthsModDataGenEntryPoint {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        var gen = event.getGenerator();
        var out = gen.getPackOutput();
        CompletableFuture< HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper efh = event.getExistingFileHelper();

        if (event.includeServer()) {
            gen.addProvider(true, new LostdepthsDatapackProvider(out, lookup));
            gen.addProvider(true, new LostdepthsModTypeTags(out, lookup, efh));
        }
    }

    private LostdepthsModDataGenEntryPoint() {}
}
