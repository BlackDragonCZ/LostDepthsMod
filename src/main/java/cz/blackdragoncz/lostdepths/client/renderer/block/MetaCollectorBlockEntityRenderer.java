package cz.blackdragoncz.lostdepths.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import cz.blackdragoncz.lostdepths.block.entity.MetaCollectorBlockEntity;
import cz.blackdragoncz.lostdepths.client.ClientSide;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class MetaCollectorBlockEntityRenderer extends BlockEntityRenderBase<MetaCollectorBlockEntity> {

    public MetaCollectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MetaCollectorBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight, int packedOverlay) {
        final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        ItemStack stack = blockEntity.getItem(0);
        if (stack.isEmpty())
        {
            //rotation = 0;
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5f, 0.5);
        poseStack.pushPose();
        //poseStack.translate(-0.15f + (2 - 0) * 0.15f, 0, 0.05f);
        poseStack.pushPose();
        poseStack.last().pose().rotate((ClientSide.getElapsedTicks() + partialTicks) * 0.15f, 0, 1, 0);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.renderStatic(stack, ItemDisplayContext.NONE, 200, packedOverlay, poseStack, source, blockEntity.getLevel(), 0);
        poseStack.popPose();
        poseStack.popPose();
        poseStack.popPose();
    }
}
