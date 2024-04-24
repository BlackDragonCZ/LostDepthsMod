package cz.blackdragoncz.lostdepths.client.renderer.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.world.entity.projectile.ThrownDraconicTrident;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;

public class ThrownDraconicTridentRenderer extends EntityRenderer<ThrownDraconicTrident> {

    public static final ResourceLocation TRIDENT_LOCATION = new ResourceLocation("textures/entity/trident.png");

    public ThrownDraconicTridentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public void render(ThrownDraconicTrident pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));

        pPoseStack.pushPose();

        if (pEntity.getPickupItem().getItem() == LostdepthsModItems.DRACONIC_TRIDENT.get()) {
            pPoseStack.translate(0, 0.6f, 0);
        } else {
            pPoseStack.translate(0, 1.15f, 0);
        }

        pPoseStack.mulPose(Axis.XN.rotationDegrees(180.0F));
        pPoseStack.mulPose(Axis.YN.rotationDegrees(-90.0F));
        Minecraft.getInstance().getItemRenderer().renderStatic(pEntity.getPickupItem(), ItemDisplayContext.NONE, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, pEntity.level(), 0);
        pPoseStack.popPose();
        pPoseStack.popPose();

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownDraconicTrident pEntity) {
        return TRIDENT_LOCATION;
    }
}
