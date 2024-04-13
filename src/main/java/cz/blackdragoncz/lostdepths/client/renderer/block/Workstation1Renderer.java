package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import cz.blackdragoncz.lostdepths.block.Workstation1Block;
import cz.blackdragoncz.lostdepths.block.entity.Workstation1BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class Workstation1Renderer extends BlockEntityRenderBase<Workstation1BlockEntity> {

    public Workstation1Renderer(BlockEntityRendererProvider.Context context) {
    }

    private float getRotation(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 270;

            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        };
    }

    @Override
    public void render(Workstation1BlockEntity blockEntity, float pPartialTick, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay) {
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        int index = 1;

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (y == 1 && x == 1)
                    continue;
                if (y == 2 && (x == 0 || x == 2))
                    continue;

                ItemStack stack = blockEntity.getItem(index++);
                if (stack.isEmpty())
                    continue;

                poseStack.pushPose();
                poseStack.translate(0.5, 0.62f, 0.5);
                poseStack.last().pose().rotate((float)Math.toRadians(getRotation(blockEntity.getBlockState().getValue(Workstation1Block.FACING))), 0, 1, 0);
                poseStack.pushPose();
                poseStack.translate(-0.15f + (2 - x) * 0.15f, 0, 0.05f - y * 0.15f);
                poseStack.pushPose();
                poseStack.last().pose().rotate((float)Math.toRadians(90), 1, 0, 0);
                poseStack.scale(0.15f, 0.15f, 0.15f);
                itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, packedLight, packedOverlay, poseStack, source, blockEntity.getLevel(), 0);
                poseStack.popPose();
                poseStack.popPose();
                poseStack.popPose();
            }
        }
    }
}
