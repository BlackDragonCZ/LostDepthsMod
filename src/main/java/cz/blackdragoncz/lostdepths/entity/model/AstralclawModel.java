package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.AstralclawEntity;

public class AstralclawModel extends GeoModel<AstralclawEntity> {
	@Override
	public ResourceLocation getAnimationResource(AstralclawEntity entity) {
		return new ResourceLocation("lostdepths", "animations/invertedwolf.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(AstralclawEntity entity) {
		return new ResourceLocation("lostdepths", "geo/invertedwolf.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(AstralclawEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
