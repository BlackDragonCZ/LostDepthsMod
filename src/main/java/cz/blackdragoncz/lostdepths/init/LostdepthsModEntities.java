package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.world.entity.projectile.ThrownDraconicTrident;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import cz.blackdragoncz.lostdepths.entity.TheProtectorEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.ShooterHProjectileEntity;
import cz.blackdragoncz.lostdepths.entity.NeuroblazeEntity;
import cz.blackdragoncz.lostdepths.entity.MaelstromEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.LostDarkEntityProjectile;
import cz.blackdragoncz.lostdepths.entity.LostDarkEntity;
import cz.blackdragoncz.lostdepths.entity.GuoonEntity;
import cz.blackdragoncz.lostdepths.entity.GalaxyDragonEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.FluxLanternProjectileEntity;
import cz.blackdragoncz.lostdepths.entity.FlapperEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.Dm1bulletEntity;
import cz.blackdragoncz.lostdepths.entity.Dm12Entity;
import cz.blackdragoncz.lostdepths.entity.projectile.CaneOfVenomProjectileEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.BlazeProjectileEntity;
import cz.blackdragoncz.lostdepths.entity.BeamBulletEntity;
import cz.blackdragoncz.lostdepths.entity.AstralclawEntity;
import cz.blackdragoncz.lostdepths.entity.projectile.ArachnotaProjectileEntity;
import cz.blackdragoncz.lostdepths.entity.ArachnotaEntity;
import cz.blackdragoncz.lostdepths.LostdepthsMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LostdepthsModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LostdepthsMod.MODID);
	public static final RegistryObject<EntityType<LostDarkEntity>> LOST_DARK = register("lost_dark",
			EntityType.Builder.<LostDarkEntity>of(LostDarkEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(LostDarkEntity::new).fireImmune().sized(1f, 1f));
	public static final RegistryObject<EntityType<LostDarkEntityProjectile>> LOST_DARK_PROJECTILE = register("projectile_lost_dark", EntityType.Builder.<LostDarkEntityProjectile>of(LostDarkEntityProjectile::new, MobCategory.MISC)
			.setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).setCustomClientFactory(LostDarkEntityProjectile::new).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MaelstromEntity>> MAELSTROM = register("maelstrom", EntityType.Builder.<MaelstromEntity>of(MaelstromEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
			.setUpdateInterval(3).setCustomClientFactory(MaelstromEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<GalaxyDragonEntity>> GALAXY_DRAGON = register("galaxy_dragon", EntityType.Builder.<GalaxyDragonEntity>of(GalaxyDragonEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(GalaxyDragonEntity::new).fireImmune().sized(0.6f, 1.8f));
	public static final RegistryObject<EntityType<GuoonEntity>> GUOON = register("guoon",
			EntityType.Builder.<GuoonEntity>of(GuoonEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(GuoonEntity::new)

					.sized(1.2f, 1.2f));
	public static final RegistryObject<EntityType<FlapperEntity>> FLAPPER = register("flapper",
			EntityType.Builder.<FlapperEntity>of(FlapperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FlapperEntity::new).fireImmune().sized(1.5f, 1.5f));
	public static final RegistryObject<EntityType<TheProtectorEntity>> THE_PROTECTOR = register("the_protector", EntityType.Builder.<TheProtectorEntity>of(TheProtectorEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true)
			.setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(TheProtectorEntity::new).fireImmune().sized(0.8f, 3f));
	public static final RegistryObject<EntityType<CaneOfVenomProjectileEntity>> CANE_OF_VENOM_PROJECTILE = register("projectile_cane_of_venom_projectile",
			EntityType.Builder.<CaneOfVenomProjectileEntity>of(CaneOfVenomProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(CaneOfVenomProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
					.setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ShooterHProjectileEntity>> SHOOTER_H_PROJECTILE = register("projectile_shooter_h_projectile", EntityType.Builder.<ShooterHProjectileEntity>of(ShooterHProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(ShooterHProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<FluxLanternProjectileEntity>> FLUX_LANTERN_PROJECTILE = register("projectile_flux_lantern_projectile",
			EntityType.Builder.<FluxLanternProjectileEntity>of(FluxLanternProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(FluxLanternProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
					.setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<NeuroblazeEntity>> NEUROBLAZE = register("neuroblaze", EntityType.Builder.<NeuroblazeEntity>of(NeuroblazeEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
			.setUpdateInterval(3).setCustomClientFactory(NeuroblazeEntity::new).fireImmune().sized(0.6f, 2f));
	public static final RegistryObject<EntityType<AstralclawEntity>> ASTRALCLAW = register("astralclaw", EntityType.Builder.<AstralclawEntity>of(AstralclawEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64)
			.setUpdateInterval(3).setCustomClientFactory(AstralclawEntity::new).fireImmune().sized(0.7f, 1.9f));
	public static final RegistryObject<EntityType<BlazeProjectileEntity>> BLAZE_PROJECTILE = register("projectile_blaze_projectile", EntityType.Builder.<BlazeProjectileEntity>of(BlazeProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(BlazeProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<Dm1bulletEntity>> DM_1BULLET = register("projectile_dm_1bullet",
			EntityType.Builder.<Dm1bulletEntity>of(Dm1bulletEntity::new, MobCategory.MISC).setCustomClientFactory(Dm1bulletEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<BeamBulletEntity>> BEAM_BULLET = register("projectile_beam_bullet",
			EntityType.Builder.<BeamBulletEntity>of(BeamBulletEntity::new, MobCategory.MISC).setCustomClientFactory(BeamBulletEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<Dm12Entity>> DM_12 = register("dm_12",
			EntityType.Builder.<Dm12Entity>of(Dm12Entity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(Dm12Entity::new)

					.sized(1.2f, 2.5f));
	public static final RegistryObject<EntityType<ArachnotaProjectileEntity>> ARACHNOTA_PROJECTILE = register("projectile_arachnota_projectile", EntityType.Builder.<ArachnotaProjectileEntity>of(ArachnotaProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(ArachnotaProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ArachnotaEntity>> ARACHNOTA = register("arachnota",
			EntityType.Builder.<ArachnotaEntity>of(ArachnotaEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ArachnotaEntity::new)

					.sized(0.6f, 1.8f));

	public static final RegistryObject<EntityType<ThrownDraconicTrident>> THROWN_DRACONIC_TRIDENT = register("projectile_draconic_trident", EntityType.Builder.<ThrownDraconicTrident>of(((pEntityType, pLevel) -> new ThrownDraconicTrident(LostdepthsModItems.DRACONIC_TRIDENT.get(), pEntityType, pLevel)), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));
	public static final RegistryObject<EntityType<ThrownDraconicTrident>> THROWN_PRIME_DRACONIC_TRIDENT = register("projectile_prime_draconic_trident", EntityType.Builder.<ThrownDraconicTrident>of(((pEntityType, pLevel) -> new ThrownDraconicTrident(LostdepthsModItems.PRIME_DRACONIC_TRIDENT.get(), pEntityType, pLevel)), MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			LostDarkEntity.init();
			MaelstromEntity.init();
			GalaxyDragonEntity.init();
			GuoonEntity.init();
			FlapperEntity.init();
			TheProtectorEntity.init();
			NeuroblazeEntity.init();
			AstralclawEntity.init();
			Dm12Entity.init();
			ArachnotaEntity.init();
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		event.put(LOST_DARK.get(), LostDarkEntity.createAttributes().build());
		event.put(MAELSTROM.get(), MaelstromEntity.createAttributes().build());
		event.put(GALAXY_DRAGON.get(), GalaxyDragonEntity.createAttributes().build());
		event.put(GUOON.get(), GuoonEntity.createAttributes().build());
		event.put(FLAPPER.get(), FlapperEntity.createAttributes().build());
		event.put(THE_PROTECTOR.get(), TheProtectorEntity.createAttributes().build());
		event.put(NEUROBLAZE.get(), NeuroblazeEntity.createAttributes().build());
		event.put(ASTRALCLAW.get(), AstralclawEntity.createAttributes().build());
		event.put(DM_12.get(), Dm12Entity.createAttributes().build());
		event.put(ARACHNOTA.get(), ArachnotaEntity.createAttributes().build());
	}
}
