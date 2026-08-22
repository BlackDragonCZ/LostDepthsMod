package cz.blackdragoncz.lostdepths.client.hologram;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

// Walks the pre-culled face list and emits one quad each. No per-frame geometry work beyond this loop; the culling already happened at bake time.
public final class HologramRenderer {

	private HologramRenderer() {
	}

	// Vanilla-style directional shading so the voxel silhouette still reads without textures or lighting.
	private static float shade(Direction direction) {
		return switch (direction) {
			case UP -> 1.0f;
			case DOWN -> 0.5f;
			case NORTH, SOUTH -> 0.8f;
			case EAST, WEST -> 0.6f;
		};
	}

	public static void render(HologramMesh mesh, HologramDefinition definition, PoseStack poseStack, MultiBufferSource buffer, boolean culled) {
		if (mesh.faceCount() == 0)
			return;

		VertexConsumer consumer = buffer.getBuffer(culled ? HologramRenderTypes.HOLOGRAM_CULLED : HologramRenderTypes.HOLOGRAM);
		Matrix4f pose = poseStack.last().pose();

		float tintR = ((definition.tint() >> 16) & 0xFF) / 255f;
		float tintG = ((definition.tint() >> 8) & 0xFF) / 255f;
		float tintB = (definition.tint() & 0xFF) / 255f;
		float alpha = definition.alpha();

		for (int i = 0; i < mesh.faceCount(); i++) {
			Direction direction = mesh.direction(i);
			int color = mesh.color(i);
			float shade = shade(direction);
			float r = (((color >> 16) & 0xFF) / 255f) * tintR * shade;
			float g = (((color >> 8) & 0xFF) / 255f) * tintG * shade;
			float b = ((color & 0xFF) / 255f) * tintB * shade;

			float x = mesh.x(i);
			float y = mesh.y(i);
			float z = mesh.z(i);
			for (int corner = 0; corner < 4; corner++) {
				float[] offset = CORNERS[direction.get3DDataValue()][corner];
				consumer.vertex(pose, x + offset[0], y + offset[1], z + offset[2]).color(r, g, b, alpha).endVertex();
			}
		}
	}

	// Corners of a unit cube face, indexed by Direction.get3DDataValue(): DOWN, UP, NORTH, SOUTH, WEST, EAST.
	private static final float[][][] CORNERS = {
			{{0, 0, 0}, {0, 0, 1}, {1, 0, 1}, {1, 0, 0}},
			{{0, 1, 1}, {0, 1, 0}, {1, 1, 0}, {1, 1, 1}},
			{{1, 0, 0}, {1, 1, 0}, {0, 1, 0}, {0, 0, 0}},
			{{0, 0, 1}, {0, 1, 1}, {1, 1, 1}, {1, 0, 1}},
			{{0, 0, 0}, {0, 1, 0}, {0, 1, 1}, {0, 0, 1}},
			{{1, 0, 1}, {1, 1, 1}, {1, 1, 0}, {1, 0, 0}}
	};
}
