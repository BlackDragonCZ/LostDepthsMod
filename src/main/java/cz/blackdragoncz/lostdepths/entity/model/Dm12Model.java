package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.Dm12Entity;

public class Dm12Model extends GeoModel<Dm12Entity> {
	@Override
	public ResourceLocation getAnimationResource(Dm12Entity entity) {
		return new ResourceLocation("lostdepths", "animations/dm1_2.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(Dm12Entity entity) {
		return new ResourceLocation("lostdepths", "geo/dm1_2.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(Dm12Entity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
