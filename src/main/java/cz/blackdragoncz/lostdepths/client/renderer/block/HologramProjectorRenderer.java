package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cz.blackdragoncz.lostdepths.block.entity.HologramProjectorBlockEntity;
import cz.blackdragoncz.lostdepths.client.hologram.HologramDefinition;
import cz.blackdragoncz.lostdepths.client.hologram.HologramMesh;
import cz.blackdragoncz.lostdepths.client.hologram.HologramRenderer;
import cz.blackdragoncz.lostdepths.client.hologram.HologramTemplates;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class HologramProjectorRenderer implements BlockEntityRenderer<HologramProjectorBlockEntity> {

	public HologramProjectorRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(HologramProjectorBlockEntity be, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!be.shouldRender())
			return;
		ResourceLocation id = be.getHologram();
		if (id == null)
			return;
		HologramTemplates.Entry entry = HologramTemplates.get(id);
		if (entry == null)
			return;

		HologramMesh mesh = entry.mesh();
		HologramDefinition definition = entry.definition();
		float scale = definition.scale();

		poseStack.pushPose();
		poseStack.translate(0.5 + be.offsetX(), be.offsetY(), 0.5 + be.offsetZ());

		// Yaw, then pitch, then roll, applied about the hologram's own centre.
		if (be.rotationY() != 0)
			poseStack.mulPose(Axis.YP.rotationDegrees(be.rotationY()));
		if (be.rotationX() != 0)
			poseStack.mulPose(Axis.XP.rotationDegrees(be.rotationX()));
		if (be.rotationZ() != 0)
			poseStack.mulPose(Axis.ZP.rotationDegrees(be.rotationZ()));

		if (definition.spin() != 0) {
			float time = (be.getLevel() == null ? 0 : be.getLevel().getGameTime()) + partialTick;
			poseStack.mulPose(Axis.YP.rotationDegrees(time * definition.spin()));
		}

		poseStack.scale(scale, scale, scale);
		// Structure templates have a corner origin. Centre on all three axes, not just X/Z, so the X and Z rotations pivot through the middle of the
		// hologram instead of swinging it around its base.
		poseStack.translate(-mesh.sizeX() / 2.0f, -mesh.sizeY() / 2.0f, -mesh.sizeZ() / 2.0f);

		HologramRenderer.render(mesh, definition, poseStack, buffer, be.isCulled());
		poseStack.popPose();
	}

	// The hologram extends far past the projector; without this it stops rendering as soon as the block is out of view.
	@Override
	public boolean shouldRenderOffScreen(HologramProjectorBlockEntity be) {
		return true;
	}
}
