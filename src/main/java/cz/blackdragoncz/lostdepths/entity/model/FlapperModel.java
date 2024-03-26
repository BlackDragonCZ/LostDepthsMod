package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.FlapperEntity;

public class FlapperModel extends GeoModel<FlapperEntity> {
	@Override
	public ResourceLocation getAnimationResource(FlapperEntity entity) {
		return new ResourceLocation("lostdepths", "animations/flapper.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(FlapperEntity entity) {
		return new ResourceLocation("lostdepths", "geo/flapper.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FlapperEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
