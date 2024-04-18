package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import cz.blackdragoncz.lostdepths.block.machine.GalacticWorkstationBlock;
import cz.blackdragoncz.lostdepths.block.entity.AlloyWorkstationBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class AlloyWorkstationRenderer extends BlockEntityRenderBase<AlloyWorkstationBlockEntity> {

    public AlloyWorkstationRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(AlloyWorkstationBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay) {
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int index = x + y * 3;

                if (index == 3 || index == 5)
                    continue;

                ItemStack stack = blockEntity.getItem(index);
                if (stack.isEmpty())
                    continue;

                float yOffset = 0;

                if ((x == 0 || x == 2) && y == 0) {
                    yOffset = 0.05f;
                } else if ((x == 0 || x == 2) && y == 2) {
                    yOffset = -0.05f;
                }

                poseStack.pushPose();
                poseStack.translate(0.5, 0.76f, 0.5);
                poseStack.last().pose().rotate((float)Math.toRadians(getRotation(blockEntity.getBlockState().getValue(GalacticWorkstationBlock.FACING))), 0, 1, 0);
                poseStack.pushPose();
                poseStack.translate(-0.13f + (2 - x) * 0.15f, 0, 0.07f - yOffset - y * 0.15f);
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
