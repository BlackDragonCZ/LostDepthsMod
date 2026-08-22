package cz.blackdragoncz.lostdepths.client.hologram;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

// Subclassed only to reach RenderType's protected state shards. Untextured coloured quads, translucent, no depth write, so the hologram reads as a
// projection rather than a solid. Culling is off for now: the mesh emits one quad per visible face and wrong winding would silently hide them.
public final class HologramRenderTypes extends RenderType {

	public static final RenderType HOLOGRAM = create("lostdepths_hologram", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 262144, false, true,
			CompositeState.builder()
					.setShaderState(POSITION_COLOR_SHADER)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setCullState(NO_CULL)
					.setLightmapState(NO_LIGHTMAP)
					.setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false));

	// Same pass with back-face culling on. Correct only if every quad is wound counter-clockwise from outside; if faces vanish, CORNERS in
	// HologramRenderer is reversed. Roughly halves the quads that reach the GPU.
	public static final RenderType HOLOGRAM_CULLED = create("lostdepths_hologram_culled", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 262144, false, true,
			CompositeState.builder()
					.setShaderState(POSITION_COLOR_SHADER)
					.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
					.setCullState(CULL)
					.setLightmapState(NO_LIGHTMAP)
					.setWriteMaskState(COLOR_WRITE)
					.createCompositeState(false));

	private HologramRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setup, Runnable clear) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setup, clear);
	}
}
