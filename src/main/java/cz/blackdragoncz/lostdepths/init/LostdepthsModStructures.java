package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.world.structures.TemplateGridStructure;
import cz.blackdragoncz.lostdepths.world.structures.TemplatePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LostdepthsModStructures {

	public static final DeferredRegister<StructureType<?>> TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, LostdepthsMod.MODID);
	public static final DeferredRegister<StructurePieceType> PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, LostdepthsMod.MODID);

	public static final RegistryObject<StructureType<?>> TEMPLATE_GRID = TYPES.register("template_grid", () -> typed());
	public static final RegistryObject<StructurePieceType> TEMPLATE_PIECE = PIECES.register("template_piece", () -> (StructurePieceType.StructureTemplateType) TemplatePiece::new);

	public static final ResourceKey<Structure> RUINS = ResourceKey.create(Registries.STRUCTURE, LostdepthsMod.rl("ruins"));
	public static final ResourceKey<Structure> MANUFACTORY = ResourceKey.create(Registries.STRUCTURE, LostdepthsMod.rl("manufactory"));

	@SuppressWarnings("unchecked")
	private static <S extends Structure> StructureType<S> typed() {
		return () -> (com.mojang.serialization.Codec<S>) TemplateGridStructure.CODEC;
	}

	private LostdepthsModStructures() {
	}
}
