package cz.blackdragoncz.lostdepths.datagen;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class LostdepthsModBlockTags extends BlockTagsProvider {

    public LostdepthsModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, ExistingFileHelper efh) {
        super(output, lookup, LostdepthsMod.MODID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                LostdepthsModBlocks.CLOVINITE_ORE.get(),
                LostdepthsModBlocks.SUNDER_WOOD.get(),
                LostdepthsModBlocks.SUNDER_WOOD_SAP.get(),
                LostdepthsModBlocks.CELESTIAL_LOGS_BLUE.get(),
                LostdepthsModBlocks.CELESTIAL_LOGS_RED.get(),
                LostdepthsModBlocks.CELESTIAL_LEAVES_BLUE.get(),
                LostdepthsModBlocks.CELESTIAL_LEAVES_RED.get(),
                LostdepthsModBlocks.SUNDER_LEAVES.get(),
                LostdepthsModBlocks.SUNDER_PLANKS.get(),
                LostdepthsModBlocks.SUNDER_FENCE.get(),
                LostdepthsModBlocks.SUNDER_SLAB.get(),
                LostdepthsModBlocks.SUNDER_STAIRS.get(),
                LostdepthsModBlocks.FERRO_LOG.get(),
                LostdepthsModBlocks.FERRO_LEAVES.get(),
                LostdepthsModBlocks.INFUSED_SIGN.get(),
                LostdepthsModBlocks.INFUSED_HANGING_SIGN.get(),
                LostdepthsModBlocks.INFUSED_WALL_HANGING_SIGN.get(),
                LostdepthsModBlocks.INFUSED_WALL_SIGN.get()
        );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                LostdepthsModBlocks.MYRITE_ORE.get(),
                LostdepthsModBlocks.MYRITE_ORE_ACTIVE.get(),
                LostdepthsModBlocks.LUCIENT_ORE.get(),
                LostdepthsModBlocks.LUCIENT_ORE_ACTIVE.get(),
                LostdepthsModBlocks.HYPERIUM_ORE.get(),
                LostdepthsModBlocks.PSYCHERIUM_ORE.get(),
                LostdepthsModBlocks.VARLLERIUM_ORE.get(),
                LostdepthsModBlocks.BIOLLITERITE_ORE.get(),
                LostdepthsModBlocks.NECROTONITE_ORE.get(),
                LostdepthsModBlocks.NOXHERTIUM_ORE.get(),
                LostdepthsModBlocks.COGNITIUM_ORE.get(),
                LostdepthsModBlocks.MULTIVERSITE_ORE.get(),
                LostdepthsModBlocks.PHOTOTENZYTE_ORE.get(),
                LostdepthsModBlocks.PHOTOTENZYTE_ORE_ACTIVE.get(),
                LostdepthsModBlocks.FIRERITE_ORE.get(),
                LostdepthsModBlocks.MELWORIUM_ORE.get(),
                LostdepthsModBlocks.MORFARITE_ORE.get(),
                LostdepthsModBlocks.CRYZULITE_ORE.get(),
                LostdepthsModBlocks.ZERITHIUM_ORE.get(),
                LostdepthsModBlocks.SERPENTINE_ORE_UNPOWERED.get(),
                LostdepthsModBlocks.SERPENTINE_ORE.get(),
                LostdepthsModBlocks.HARD_CRYSTAL_B.get(),
                LostdepthsModBlocks.HARD_CRYSTAL_R.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICKS.get(),
                LostdepthsModBlocks.INFUSED_IRON_PILLAR.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICK_STAIRS.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICK_SLAB.get(),
                LostdepthsModBlocks.GALACTIC_WORKSTATION.get(),
                LostdepthsModBlocks.FACTORY_BLOCK.get(),
                LostdepthsModBlocks.O_FACTORY_BLOCK.get(),
                LostdepthsModBlocks.QUANTUM_TRANSPORTER.get(),
                LostdepthsModBlocks.SPACE_ROCK.get(),
                LostdepthsModBlocks.SPACE_ROCK_DIRT.get(),
                LostdepthsModBlocks.INFUSED_IRON_CROSS_GLOW.get(),
                LostdepthsModBlocks.NEO_GLASS.get(),
                LostdepthsModBlocks.CELESTIAL_CHEST.get(),
                LostdepthsModBlocks.CELESTIAL_CHEST_OPEN.get(),
                LostdepthsModBlocks.ALLOY_WORKSTATION.get(),
                LostdepthsModBlocks.RUINED_WORKBENCH.get(),
                LostdepthsModBlocks.RUINED_ARCH.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICKS_A.get(),
                LostdepthsModBlocks.FACTORY_PILLAR.get(),
                LostdepthsModBlocks.NEOSTEEL_LANTERN.get(),
                LostdepthsModBlocks.EXTRA_TERESTRIAL_COMPRESSOR.get(),
                LostdepthsModBlocks.NEOSTEEL_LANTERN_2.get(),
                LostdepthsModBlocks.INFUSED_DARK_BRICKS.get(),
                LostdepthsModBlocks.INFUSED_DARK_TILES.get(),
                LostdepthsModBlocks.PRINT_TECH.get(),
                LostdepthsModBlocks.ATMOS_TECH.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICKS_WALL.get(),
                LostdepthsModBlocks.INFUSED_IRON_BRICK_WALLS.get(),
                LostdepthsModBlocks.BLACK_MARKET.get(),
                LostdepthsModBlocks.JAMMER_A.get(),
                LostdepthsModBlocks.JAMMER_B.get(),
                LostdepthsModBlocks.JAMMER_C.get(),
                LostdepthsModBlocks.JAMMER_D.get(),
                LostdepthsModBlocks.CHEM_TECH.get(),
                LostdepthsModBlocks.TERMINAL_MONITOR.get(),
                LostdepthsModBlocks.TERMINAL_CONSOLE_1.get(),
                LostdepthsModBlocks.TERMINAL_CONSOLE_2.get(),
                LostdepthsModBlocks.ULTRA_RESISTIVE_GLASS.get(),
                LostdepthsModBlocks.DEEP_SPACE_ROCK.get(),
                LostdepthsModBlocks.FUNGAL_SPACE_ROCK.get(),
                LostdepthsModBlocks.GALACTIC_COMPRESSOR.get(),
                LostdepthsModBlocks.STAR_CHEST.get(),
                LostdepthsModBlocks.STAR_CHEST_OPEN.get(),
                LostdepthsModBlocks.DRACONIC_CHEST.get(),
                LostdepthsModBlocks.DRACONIC_CHEST_OPEN.get(),
                LostdepthsModBlocks.DRACONIC_FLAG.get(),
                LostdepthsModBlocks.INFUSED_SIGN.get(),
                LostdepthsModBlocks.INFUSED_HANGING_SIGN.get(),
                LostdepthsModBlocks.INFUSED_WALL_HANGING_SIGN.get(),
                LostdepthsModBlocks.INFUSED_WALL_SIGN.get(),
                LostdepthsModBlocks.NUROSTAR_GENERATOR.get(),
                LostdepthsModBlocks.NUROSTAR_CABLE.get(),
                LostdepthsModBlocks.BLACK_HOLE_COMPRESSOR.get()
        );
        tag(BlockTags.DIRT).add(LostdepthsModBlocks.SPACE_ROCK_DIRT.get());
        tag(BlockTags.DRAGON_IMMUNE).add(
                LostdepthsModBlocks.TREASUREPILLAR.get(),
                LostdepthsModBlocks.TREASURESTAIRS.get(),
                LostdepthsModBlocks.TREASURESLABS.get(),
                LostdepthsModBlocks.TREASURECROSS.get(),
                LostdepthsModBlocks.TREASURE_GLASS.get(),
                LostdepthsModBlocks.TREASURE_CROSS_GLOW.get(),
                LostdepthsModBlocks.TREASURE_POWER_BEAM.get(),
                LostdepthsModBlocks.TREASURE_NEO_SLAB.get(),
                LostdepthsModBlocks.TREASURE_FACTORY_PILLAR.get(),
                LostdepthsModBlocks.TREASURE_DARK_BRICKS.get(),
                LostdepthsModBlocks.TREASURE_DARK_TILES.get(),
                LostdepthsModBlocks.TREASURE_GLOWSTEEL.get(),
                LostdepthsModBlocks.TREASURE_NEO_SLAB_BLOCK.get(),
                LostdepthsModBlocks.ULTRA_RESISTIVE_GLASS.get()
        );
        tag(BlockTags.WITHER_IMMUNE).add(
                LostdepthsModBlocks.TREASUREPILLAR.get(),
                LostdepthsModBlocks.TREASURESTAIRS.get(),
                LostdepthsModBlocks.TREASURESLABS.get(),
                LostdepthsModBlocks.TREASURECROSS.get(),
                LostdepthsModBlocks.TREASURE_GLASS.get(),
                LostdepthsModBlocks.TREASURE_CROSS_GLOW.get(),
                LostdepthsModBlocks.TREASURE_POWER_BEAM.get(),
                LostdepthsModBlocks.TREASURE_NEO_SLAB.get(),
                LostdepthsModBlocks.TREASURE_FACTORY_PILLAR.get(),
                LostdepthsModBlocks.TREASURE_DARK_BRICKS.get(),
                LostdepthsModBlocks.TREASURE_DARK_TILES.get(),
                LostdepthsModBlocks.TREASURE_GLOWSTEEL.get(),
                LostdepthsModBlocks.TREASURE_NEO_SLAB_BLOCK.get(),
                LostdepthsModBlocks.ULTRA_RESISTIVE_GLASS.get()
        );
        tag(BlockTags.FENCES).add(LostdepthsModBlocks.SUNDER_FENCE.get());
        tag(BlockTags.LEAVES).add(LostdepthsModBlocks.CELESTIAL_LEAVES_BLUE.get(), LostdepthsModBlocks.CELESTIAL_LEAVES_RED.get());
        tag(BlockTags.LOGS).add(LostdepthsModBlocks.CELESTIAL_LOGS_BLUE.get(), LostdepthsModBlocks.CELESTIAL_LOGS_RED.get());
        tag(BlockTags.PLANKS).add(LostdepthsModBlocks.SUNDER_PLANKS.get());
        tag(BlockTags.STAIRS).add(LostdepthsModBlocks.INFUSED_IRON_BRICK_STAIRS.get(), LostdepthsModBlocks.TREASURESTAIRS.get());
        tag(BlockTags.WALLS).add(LostdepthsModBlocks.INFUSED_IRON_BRICKS_WALL.get(), LostdepthsModBlocks.INFUSED_IRON_BRICK_WALLS.get());
        tag(BlockTags.WOODEN_FENCES).add(LostdepthsModBlocks.SUNDER_FENCE.get());
        tag(BlockTags.WALL_HANGING_SIGNS).add(LostdepthsModBlocks.INFUSED_WALL_HANGING_SIGN.get());

        tag(BlockTags.OVERWORLD_CARVER_REPLACEABLES).add(
                Blocks.DEEPSLATE,
                LostdepthsModBlocks.SPACE_ROCK_DIRT.get(),
                LostdepthsModBlocks.SPACE_ROCK.get(),
                LostdepthsModBlocks.DEEP_SPACE_ROCK.get(),
                LostdepthsModBlocks.FUNGAL_SPACE_ROCK.get()
        );
    }
}
