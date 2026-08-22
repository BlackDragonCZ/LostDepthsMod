package cz.blackdragoncz.lostdepths.world.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import cz.blackdragoncz.lostdepths.init.LostdepthsModStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.List;
import java.util.Optional;

// Places a fixed grid of NBT templates as one structure. Replaces the old feature + core-block scheme: every piece is registered up front, so the
// generator writes each one only into the chunk it belongs to instead of stamping blocks into chunks that may not exist yet.
public class TemplateGridStructure extends Structure {

	public static final Codec<TemplateGridStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			settingsCodec(instance),
			Vec3i.CODEC.listOf().fieldOf("offsets").forGetter(s -> s.offsets),
			ResourceLocation.CODEC.listOf().fieldOf("templates").forGetter(s -> s.templates),
			Codec.BOOL.optionalFieldOf("random_rotation", false).forGetter(s -> s.randomRotation),
			Codec.BOOL.optionalFieldOf("random_mirror", false).forGetter(s -> s.randomMirror)
	).apply(instance, TemplateGridStructure::new));

	private final List<Vec3i> offsets;
	private final List<ResourceLocation> templates;
	private final boolean randomRotation;
	private final boolean randomMirror;

	public TemplateGridStructure(StructureSettings settings, List<Vec3i> offsets, List<ResourceLocation> templates, boolean randomRotation, boolean randomMirror) {
		super(settings);
		if (offsets.size() != templates.size())
			throw new IllegalArgumentException("offsets and templates must be the same length");
		this.offsets = offsets;
		this.templates = templates;
		this.randomRotation = randomRotation;
		this.randomMirror = randomMirror;
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		RandomSource random = context.random();
		Rotation rotation = randomRotation ? Rotation.getRandom(random) : Rotation.NONE;
		Mirror mirror = randomMirror ? Mirror.values()[random.nextInt(2)] : Mirror.NONE;

		// One shared ground height for the whole grid, so a multi-part structure never ends up stepped across uneven terrain.
		int x = context.chunkPos().getMinBlockX();
		int z = context.chunkPos().getMinBlockZ();
		int y = context.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
		BlockPos origin = new BlockPos(x, y, z);

		return Optional.of(new GenerationStub(origin, builder -> {
			StructureTemplateManager manager = context.structureTemplateManager();
			for (int i = 0; i < templates.size(); i++)
				builder.addPiece(new TemplatePiece(manager, templates.get(i), origin.offset(offsets.get(i)), rotation, mirror));
		}));
	}

	@Override
	public StructureType<?> type() {
		return LostdepthsModStructures.TEMPLATE_GRID.get();
	}
}
