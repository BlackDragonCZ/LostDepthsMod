package cz.blackdragoncz.lostdepths.datagen;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LostdepthsModPoITags extends PoiTypeTagsProvider {

    public LostdepthsModPoITags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
        super(output, lookup, LostdepthsMod.MODID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var acquirable = tag(PoiTypeTags.ACQUIRABLE_JOB_SITE);

        acquirable.addOptional(new ResourceLocation("lostdepths", "cosmic_scientist"));
        acquirable.addOptional(new ResourceLocation("lostdepths", "atmospheric_scientist"));
        acquirable.addOptional(new ResourceLocation("lostdepths", "black_market_seller"));
        acquirable.addOptional(new ResourceLocation("lostdepths", "chemistry_operator"));
    }
}