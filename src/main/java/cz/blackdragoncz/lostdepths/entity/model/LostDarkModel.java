package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.LostDarkEntity;

public class LostDarkModel extends GeoModel<LostDarkEntity> {
	@Override
	public ResourceLocation getAnimationResource(LostDarkEntity entity) {
		return new ResourceLocation("lostdepths", "animations/lost_dark.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(LostDarkEntity entity) {
		return new ResourceLocation("lostdepths", "geo/lost_dark.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(LostDarkEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
