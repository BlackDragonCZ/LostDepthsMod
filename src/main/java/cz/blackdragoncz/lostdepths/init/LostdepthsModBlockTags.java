package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class LostdepthsModBlockTags {

    public static final TagKey<Block> NEEDS_FORGEFIRE_AXE_TOOL = TagKey.create(Registries.BLOCK, LostdepthsMod.rl("needs_forgefire_axe_tool"));

}
