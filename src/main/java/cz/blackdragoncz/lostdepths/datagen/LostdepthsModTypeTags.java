package cz.blackdragoncz.lostdepths.datagen;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LostdepthsModTypeTags extends DamageTypeTagsProvider {

    public LostdepthsModTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
        super(output, lookup, LostdepthsMod.MODID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.BYPASSES_ARMOR).add(LostdepthsModDamageTypes.TRUE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_SHIELD).add(LostdepthsModDamageTypes.TRUE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(LostdepthsModDamageTypes.TRUE_DAMAGE);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(LostdepthsModDamageTypes.TRUE_DAMAGE);
    }
}
