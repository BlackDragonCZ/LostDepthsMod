package cz.blackdragoncz.lostdepths.world.structures;

import cz.blackdragoncz.lostdepths.init.LostdepthsModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

// One NBT template placed as a structure piece. TemplateStructurePiece clips placement to the chunk currently generating, which is what the old
// core-block approach could not do - see LostdepthsModStructures.
public class TemplatePiece extends TemplateStructurePiece {

	public TemplatePiece(StructureTemplateManager manager, ResourceLocation template, BlockPos pos, Rotation rotation, Mirror mirror) {
		super(LostdepthsModStructures.TEMPLATE_PIECE.get(), 0, manager, template, template.toString(), settings(rotation, mirror), pos);
	}

	public TemplatePiece(StructureTemplateManager manager, CompoundTag tag) {
		super(LostdepthsModStructures.TEMPLATE_PIECE.get(), tag, manager, template -> settings(Rotation.valueOf(tag.getString("Rot")), Mirror.valueOf(tag.getString("Mi"))));
	}

	private static StructurePlaceSettings settings(Rotation rotation, Mirror mirror) {
		return new StructurePlaceSettings().setRotation(rotation).setMirror(mirror).setIgnoreEntities(false).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rot", this.placeSettings.getRotation().name());
		tag.putString("Mi", this.placeSettings.getMirror().name());
	}

	@Override
	protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {
	}
}
