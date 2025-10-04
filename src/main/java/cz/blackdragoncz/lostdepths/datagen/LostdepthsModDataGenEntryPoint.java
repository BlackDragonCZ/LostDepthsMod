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
        ExistingFileHelper efh = event.getExistingFileHelper();

        if (event.includeServer()) {
            var datapack = new LostdepthsDatapackProvider(out, event.getLookupProvider());
            gen.addProvider(true, datapack);

            CompletableFuture<HolderLookup.Provider> patchedLookup = datapack.getRegistryProvider();

            gen.addProvider(true, new LostdepthsModTypeTags(out, patchedLookup, efh));

            var blockTags = new LostdepthsModBlockTags(out, patchedLookup, efh);
            gen.addProvider(true, blockTags);
/*
            var poiTags = new LostdepthsModPoITags(out, patchedLookup, efh);
            gen.addProvider(true, poiTags);*/
        }
    }

    private LostdepthsModDataGenEntryPoint() {}
}
