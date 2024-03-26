package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.ArachnotaEntity;

public class ArachnotaModel extends GeoModel<ArachnotaEntity> {
	@Override
	public ResourceLocation getAnimationResource(ArachnotaEntity entity) {
		return new ResourceLocation("lostdepths", "animations/arachnota.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(ArachnotaEntity entity) {
		return new ResourceLocation("lostdepths", "geo/arachnota.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(ArachnotaEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
