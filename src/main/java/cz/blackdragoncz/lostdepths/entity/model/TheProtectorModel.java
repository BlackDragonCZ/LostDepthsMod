package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.TheProtectorEntity;

public class TheProtectorModel extends GeoModel<TheProtectorEntity> {
	@Override
	public ResourceLocation getAnimationResource(TheProtectorEntity entity) {
		return new ResourceLocation("lostdepths", "animations/the_protector.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(TheProtectorEntity entity) {
		return new ResourceLocation("lostdepths", "geo/the_protector.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TheProtectorEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
