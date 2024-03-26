package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.GuoonEntity;

public class GuoonModel extends GeoModel<GuoonEntity> {
	@Override
	public ResourceLocation getAnimationResource(GuoonEntity entity) {
		return new ResourceLocation("lostdepths", "animations/slimejs.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GuoonEntity entity) {
		return new ResourceLocation("lostdepths", "geo/slimejs.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GuoonEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
