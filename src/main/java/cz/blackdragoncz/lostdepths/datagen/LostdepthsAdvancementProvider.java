package cz.blackdragoncz.lostdepths.datagen;

import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancementBuilder;
import cz.blackdragoncz.lostdepths.advancements.LostdepthsAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

// Writes data/lostdepths/advancements/*.json from the definitions in LostdepthsAdvancements.
public final class LostdepthsAdvancementProvider implements ForgeAdvancementProvider.AdvancementGenerator {

	@Override
	public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {
		Map<LostdepthsAdvancementBuilder, Advancement> built = new HashMap<>();
		for (LostdepthsAdvancementBuilder builder : LostdepthsAdvancements.all())
			built.put(builder, builder.build(built, saver, existingFileHelper));
	}
}
