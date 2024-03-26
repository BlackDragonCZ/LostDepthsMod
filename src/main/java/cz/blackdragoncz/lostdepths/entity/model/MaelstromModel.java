package cz.blackdragoncz.lostdepths.entity.model;

import software.bernie.geckolib.model.GeoModel;

import net.minecraft.resources.ResourceLocation;

import cz.blackdragoncz.lostdepths.entity.MaelstromEntity;

public class MaelstromModel extends GeoModel<MaelstromEntity> {
	@Override
	public ResourceLocation getAnimationResource(MaelstromEntity entity) {
		return new ResourceLocation("lostdepths", "animations/maelstrom.animation.json");
	}

	@Override
	public ResourceLocation getModelResource(MaelstromEntity entity) {
		return new ResourceLocation("lostdepths", "geo/maelstrom.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(MaelstromEntity entity) {
		return new ResourceLocation("lostdepths", "textures/entities/" + entity.getTexture() + ".png");
	}

}
