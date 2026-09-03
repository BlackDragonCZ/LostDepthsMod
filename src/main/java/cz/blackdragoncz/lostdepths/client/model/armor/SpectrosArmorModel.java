package cz.blackdragoncz.lostdepths.client.model.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// Geometry recovered verbatim from 1.12.2 ModelArmorSpectros (50 boxes, 128x64).
// HumanoidArmorLayer never calls setupAnim, so animate() is driven from IClientItemExtensions.
public class SpectrosArmorModel extends HumanoidModel<LivingEntity> {

	public static final ModelLayerLocation LAYER =
			new ModelLayerLocation(new ResourceLocation("lostdepths", "spectros_armor"), "main");

	private final ModelPart wingRight;
	private final ModelPart wingLeft;

	private static SpectrosArmorModel cached;

	public SpectrosArmorModel(ModelPart root) {
		super(root);
		this.wingRight = root.getChild("body").getChild("WingR");
		this.wingLeft = root.getChild("body").getChild("WingL");
	}

	// One shared instance; baked lazily so it happens after layer registration.
	public static SpectrosArmorModel forEntity(LivingEntity entity) {
		if (cached == null)
			cached = new SpectrosArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER));
		cached.animate(entity, entity.tickCount + Minecraft.getInstance().getFrameTime());
		return cached;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(64, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.5F))
				.texOffs(97, 11).addBox(-3.0F, -9.0F, -3.0F, 1, 1, 8, new CubeDeformation(0.5F))
				.texOffs(97, 11).addBox(2.0F, -9.0F, -3.0F, 1, 1, 8, new CubeDeformation(0.5F))
				.texOffs(120, 1).addBox(2.0F, -7.0F, 4.0F, 1, 6, 1, new CubeDeformation(0.5F))
				.texOffs(120, 1).addBox(-3.0F, -7.0F, 4.0F, 1, 6, 1, new CubeDeformation(0.5F))
				.texOffs(97, 0).addBox(-4.0F, -8.0F, -6.0F, 8, 8, 1, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition hat = root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, new CubeDeformation(0.5F))
				.texOffs(76, 48).addBox(-1.0F, 1.0F, -1.0F, 2, 10, 4, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(40, 32).addBox(-3.0F, 0.0F, -2.0F, 4, 8, 4, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(40, 45).addBox(-1.0F, 0.0F, -2.0F, 4, 8, 4, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.25F))
				.mirror(true)
				.texOffs(58, 50).addBox(-2.0F, 1.0F, -2.0F, 4, 8, 4, new CubeDeformation(0.4F))
				.mirror(false), PartPose.offset(-1.9F, 12.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.25F))
				.texOffs(58, 50).addBox(-2.0F, 1.0F, -2.0F, 4, 8, 4, new CubeDeformation(0.4F)), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition Rib8_r1 = body.addOrReplaceChild("Rib8_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F))
				.mirror(false), PartPose.offsetAndRotation(3.0F, 4.0F, -2.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Rib7_r1 = body.addOrReplaceChild("Rib7_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F))
				.mirror(false), PartPose.offsetAndRotation(3.0F, 6.0F, -2.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Rib6_r1 = body.addOrReplaceChild("Rib6_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F))
				.mirror(false), PartPose.offsetAndRotation(3.0F, 8.0F, -2.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Rib5_r1 = body.addOrReplaceChild("Rib5_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F))
				.mirror(false), PartPose.offsetAndRotation(3.0F, 10.0F, -2.5F, 0.0F, 0.0F, -0.2618F));

		PartDefinition Rib4_r1 = body.addOrReplaceChild("Rib4_r1", CubeListBuilder.create()
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.0F, 10.0F, -2.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Rib3_r1 = body.addOrReplaceChild("Rib3_r1", CubeListBuilder.create()
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.0F, 8.0F, -2.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Rib2_r1 = body.addOrReplaceChild("Rib2_r1", CubeListBuilder.create()
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.0F, 6.0F, -2.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition Rib1_r1 = body.addOrReplaceChild("Rib1_r1", CubeListBuilder.create()
				.texOffs(21, 51).addBox(-2.0F, -1.0F, -0.5F, 4, 2, 1, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.0F, 4.0F, -2.5F, 0.0F, 0.0F, 0.2618F));

		PartDefinition WingR = body.addOrReplaceChild("WingR", CubeListBuilder.create()
				.texOffs(62, 44).addBox(-5.0F, -0.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F))
				.texOffs(62, 44).addBox(-5.0F, 2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F))
				.texOffs(62, 44).addBox(-5.0F, 5.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-2.0F, 3.5F, 2.5F, 0.0F, 0.3054F, 0.0F));

		PartDefinition wingr6_r1 = WingR.addOrReplaceChild("wingr6_r1", CubeListBuilder.create()
				.texOffs(78, 44).addBox(-10.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition wingr4_r1 = WingR.addOrReplaceChild("wingr4_r1", CubeListBuilder.create()
				.texOffs(78, 44).addBox(-10.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition wingr2_r1 = WingR.addOrReplaceChild("wingr2_r1", CubeListBuilder.create()
				.texOffs(78, 44).addBox(-10.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition WingL = body.addOrReplaceChild("WingL", CubeListBuilder.create()
				.mirror(true)
				.texOffs(62, 44).addBox(-1.0F, -0.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F))
				.texOffs(62, 44).addBox(-1.0F, 2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F))
				.texOffs(62, 44).addBox(-1.0F, 5.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.5F))
				.mirror(false), PartPose.offsetAndRotation(2.0F, 3.5F, 2.5F, 0.0F, -0.3054F, 0.0F));

		PartDefinition wingl6_r1 = WingL.addOrReplaceChild("wingl6_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(78, 44).addBox(4.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F))
				.mirror(false), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition wingl4_r1 = WingL.addOrReplaceChild("wingl4_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(78, 44).addBox(4.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F))
				.mirror(false), PartPose.offsetAndRotation(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition wingl2_r1 = WingL.addOrReplaceChild("wingl2_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(78, 44).addBox(4.0F, -2.5F, -0.5F, 6, 1, 1, new CubeDeformation(0.0F))
				.mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition RPlate = right_arm.addOrReplaceChild("RPlate", CubeListBuilder.create()
				.mirror(true)
				.texOffs(18, 57).addBox(1.7039F, 2.9898F, -0.75F, 5, 2, 5, new CubeDeformation(0.25F))
				.mirror(false), PartPose.offset(-5.7039F, -4.9898F, -1.75F));

		PartDefinition LSpike9_r1 = RPlate.addOrReplaceChild("LSpike9_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(45, 58).addBox(0.5F, 0.0F, 1.75F, 1, 4, 1, new CubeDeformation(-0.25F))
				.texOffs(45, 58).addBox(0.5F, 0.0F, 0.25F, 1, 4, 1, new CubeDeformation(-0.25F))
				.mirror(false), PartPose.offsetAndRotation(0.1877F, 6.9254F, 0.25F, 0.0F, 0.0F, -0.0873F));

		PartDefinition LSpike6_r1 = RPlate.addOrReplaceChild("LSpike6_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(40, 58).addBox(-0.5F, 0.2132F, 2.5F, 1, 4, 1, new CubeDeformation(0.25F))
				.mirror(false), PartPose.offsetAndRotation(2.2943F, 3.2766F, -1.25F, 0.0F, 0.0F, 0.2618F));

		PartDefinition LSpike5_r1 = RPlate.addOrReplaceChild("LSpike5_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(34, 56).addBox(-0.5F, -2.0F, 0.0F, 1, 4, 1, new CubeDeformation(-0.75F))
				.texOffs(34, 56).addBox(-0.5F, -2.0F, 2.5F, 1, 4, 1, new CubeDeformation(-0.75F))
				.mirror(false), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));

		PartDefinition LSpike4_r1 = RPlate.addOrReplaceChild("LSpike4_r1", CubeListBuilder.create()
				.mirror(true)
				.texOffs(34, 49).addBox(-0.5F, -3.0F, 1.25F, 1, 4, 1, new CubeDeformation(0.25F))
				.texOffs(34, 49).addBox(-0.5F, -3.0F, 3.75F, 1, 4, 1, new CubeDeformation(0.25F))
				.mirror(false), PartPose.offsetAndRotation(3.2943F, 3.2766F, -1.25F, 0.0F, 0.0F, -0.6109F));

		PartDefinition LPlate = left_arm.addOrReplaceChild("LPlate", CubeListBuilder.create()
				.texOffs(18, 57).addBox(-6.7039F, 2.9898F, -0.75F, 5, 2, 5, new CubeDeformation(0.25F)), PartPose.offset(5.7039F, -4.9898F, -1.75F));

		PartDefinition LSpike8_r1 = LPlate.addOrReplaceChild("LSpike8_r1", CubeListBuilder.create()
				.texOffs(45, 58).addBox(-1.5F, 0.0F, 1.75F, 1, 4, 1, new CubeDeformation(-0.25F))
				.texOffs(45, 58).addBox(-1.5F, 0.0F, 0.25F, 1, 4, 1, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.1877F, 6.9254F, 0.25F, 0.0F, 0.0F, 0.0873F));

		PartDefinition LSpike5_r2 = LPlate.addOrReplaceChild("LSpike5_r2", CubeListBuilder.create()
				.texOffs(40, 58).addBox(-0.5F, 0.2132F, 2.5F, 1, 4, 1, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-2.2943F, 3.2766F, -1.25F, 0.0F, 0.0F, -0.2618F));

		PartDefinition LSpike4_r2 = LPlate.addOrReplaceChild("LSpike4_r2", CubeListBuilder.create()
				.texOffs(34, 56).addBox(-0.5F, -2.0F, 0.0F, 1, 4, 1, new CubeDeformation(-0.75F))
				.texOffs(34, 56).addBox(-0.5F, -2.0F, 2.5F, 1, 4, 1, new CubeDeformation(-0.75F)), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));

		PartDefinition LSpike3_r1 = LPlate.addOrReplaceChild("LSpike3_r1", CubeListBuilder.create()
				.texOffs(34, 49).addBox(-0.5F, -3.0F, 1.25F, 1, 4, 1, new CubeDeformation(0.25F))
				.texOffs(34, 49).addBox(-0.5F, -3.0F, 3.75F, 1, 4, 1, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-3.2943F, 3.2766F, -1.25F, 0.0F, 0.0F, 0.6109F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	// Same maths as the 1.12.2 original.
	public void animate(LivingEntity entity, float ageInTicks) {
		if (!(entity instanceof Player))
			return;
		this.wingRight.zRot = -0.2F - 0.25F * Mth.sin(ageInTicks * 0.1F);
		this.wingLeft.zRot = -this.wingRight.zRot;
	}
}
