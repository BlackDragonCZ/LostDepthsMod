package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cz.blackdragoncz.lostdepths.block.decor.InfusedDisplayPlateBlock;
import cz.blackdragoncz.lostdepths.block.entity.InfusedDisplayPlateBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class InfusedDisplayPlateRenderer implements BlockEntityRenderer<InfusedDisplayPlateBlockEntity> {

	public InfusedDisplayPlateRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(InfusedDisplayPlateBlockEntity be, float partialTick, PoseStack poseStack,
					   MultiBufferSource buffer, int packedLight, int packedOverlay) {
		ItemStack heldItem = be.getHeldItem();
		if (heldItem.isEmpty())
			return;

		BlockState state = be.getBlockState();
		AttachFace face = state.getValue(InfusedDisplayPlateBlock.FACE);
		Direction facing = state.getValue(InfusedDisplayPlateBlock.FACING);

		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel model = itemRenderer.getModel(heldItem, be.getLevel(), null, 0);
		boolean isBlockItem = model.isGui3d();

		// Plate is 2px thick. Item sits above the plate surface.
		// 4px = 4/16 = 0.25
		float itemOffset = 2f / 16f;

		poseStack.pushPose();

		// Start at block center
		poseStack.translate(0.5, 0.5, 0.5);

		switch (face) {
			case FLOOR -> {
				// Plate on floor, item faces up
				// Move item down toward floor: 0.5 - itemOffset = distance from center
				poseStack.translate(0, -0.5 + itemOffset, 0);
				// Rotate item to face up (lay flat on plate)
				// Y rotation for horizontal facing
				poseStack.mulPose(Axis.YP.rotationDegrees(getHorizontalAngle(facing)));
			}
			case CEILING -> {
				// Plate on ceiling, item faces down
				poseStack.translate(0, 0.5 - itemOffset, 0);
				// Flip upside down
				poseStack.mulPose(Axis.ZP.rotationDegrees(180));
				poseStack.mulPose(Axis.YP.rotationDegrees(getHorizontalAngle(facing)));
			}
			case WALL -> {
				// Plate on wall, item faces outward (facing direction)
				float dx = facing.getStepX() * (0.5f - itemOffset);
				float dz = facing.getStepZ() * (0.5f - itemOffset);
				// Move toward the wall (opposite of facing = where the wall is)
				poseStack.translate(-dx, 0, -dz);
				// Rotate to face outward
				poseStack.mulPose(Axis.YP.rotationDegrees(getHorizontalAngle(facing)));
			}
		}

		if (isBlockItem) {
			poseStack.scale(0.5f, 0.5f, 0.5f);
		} else {
			if (face == AttachFace.FLOOR || face == AttachFace.CEILING) {
				// Flat items lay flat on floor/ceiling plate
				poseStack.mulPose(Axis.XP.rotationDegrees(90));
			}
			poseStack.scale(0.5f, 0.5f, 0.5f);
		}

		itemRenderer.render(heldItem, ItemDisplayContext.FIXED, false, poseStack, buffer, packedLight, packedOverlay, model);

		poseStack.popPose();
	}

	private float getHorizontalAngle(Direction facing) {
		return switch (facing) {
			case SOUTH -> 180f;
			case WEST -> 90f;
			case EAST -> -90f;
			default -> 0f; // NORTH
		};
	}
}
