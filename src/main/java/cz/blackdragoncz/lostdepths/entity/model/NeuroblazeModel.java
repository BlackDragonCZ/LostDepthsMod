package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.NeuroblazeEntity;

public class NeuroblazeModel extends GeoModel<NeuroblazeEntity> {
	@Override
	public ResourceLocation getAnimationResource(NeuroblazeEntity entity) {
		return new ResourceLocation("lostdepths", "animations/invertedblaze.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(NeuroblazeEntity entity) {
		return new ResourceLocation("lostdepths", "geo/invertedblaze.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(NeuroblazeEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
