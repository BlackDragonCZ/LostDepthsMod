package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.GalaxyDragonEntity;

public class GalaxyDragonModel extends GeoModel<GalaxyDragonEntity> {
	@Override
	public ResourceLocation getAnimationResource(GalaxyDragonEntity entity) {
		return new ResourceLocation("lostdepths", "animations/dragon_1.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(GalaxyDragonEntity entity) {
		return new ResourceLocation("lostdepths", "geo/dragon_1.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(GalaxyDragonEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
