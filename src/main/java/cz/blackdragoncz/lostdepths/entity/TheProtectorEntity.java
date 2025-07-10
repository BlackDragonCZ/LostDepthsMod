package cz.blackdragoncz.lostdepths.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModGameRules;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.util.NothingNullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.LevelReader;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.GeoEntity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

import cz.blackdragoncz.lostdepths.init.LostdepthsModEntities;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@NothingNullByDefault
@SuppressWarnings("deprecation")
public class TheProtectorEntity extends PathfinderMob implements GeoEntity, NeutralMob {
	protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TheProtectorEntity.class, EntityDataSerializers.BYTE);

	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(TheProtectorEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(TheProtectorEntity.class, EntityDataSerializers.STRING);
	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(TheProtectorEntity.class, EntityDataSerializers.STRING);
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	private boolean swinging;
	private boolean lastloop;
	private long lastSwing;
	public String animationProcedure = "empty";

	private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	private int remainingPersistentAngerTime;
	@Nullable
	private UUID persistentAngerTarget;

	public TheProtectorEntity(PlayMessages.SpawnEntity packet, Level world) {
		this(LostdepthsModEntities.THE_PROTECTOR.get(), world);
	}

	public TheProtectorEntity(EntityType<TheProtectorEntity> type, Level world) {
		super(type, world);
		xpReward = 5;
		setNoAi(false);
	}

	public boolean isPlayerCreated() {
		return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
	}

	public void setPlayerCreated(boolean pPlayerCreated) {
		byte b0 = this.entityData.get(DATA_FLAGS_ID);
		if (pPlayerCreated) {
			this.entityData.set(DATA_FLAGS_ID, (byte)(b0 | 1));
		} else {
			this.entityData.set(DATA_FLAGS_ID, (byte)(b0 & -2));
		}

	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(SHOOT, false);
		this.entityData.define(ANIMATION, "undefined");
		this.entityData.define(TEXTURE, "the_protector");
		this.entityData.define(DATA_FLAGS_ID, (byte)0);
	}

	public void setTexture(String texture) {
		this.entityData.set(TEXTURE, texture);
	}

	public String getTexture() {
		return this.entityData.get(TEXTURE);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.2, false) {
			@Override
			protected double getAttackReachSqr(LivingEntity entity) {
				return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
			}
		});
		this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8));
		this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 5, true, true, this::isAngryAt));
	}

	@Override
	public float getWalkTargetValue(BlockPos pPos) {
		return 0.0f;
	}

	@Override
	public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
		return 0.0f;
	}

	@Override
	public MobType getMobType() {
		return MobType.UNDEFINED;
	}

	protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
		super.dropCustomDeathLoot(source, looting, recentlyHitIn);
		this.spawnAtLocation(new ItemStack(Blocks.IRON_BLOCK));
	}

	@Override
	public void playStepSound(BlockPos pos, BlockState blockIn) {
		this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.step")), 0.15f, 1);
	}

	@Override
	public SoundEvent getHurtSound(DamageSource ds) {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.hurt"));
	}

	@Override
	public SoundEvent getDeathSound() {
		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.death"));
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (source.is(DamageTypes.IN_FIRE))
			return false;
		if (source.is(DamageTypes.FALL))
			return false;
		if (source.is(DamageTypes.CACTUS))
			return false;
		if (source.is(DamageTypes.DROWN))
			return false;
		if (source.is(DamageTypes.LIGHTNING_BOLT))
			return false;
		if (source.is(DamageTypes.EXPLOSION))
			return false;
		if (source.is(DamageTypes.DRAGON_BREATH))
			return false;
		return super.hurt(source, amount);
	}

	@Override
	public void die(DamageSource source) {
		super.die(source);

		Entity sourceEntity = source.getEntity();

		if ((sourceEntity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:tools")))) {
			for (int index1 = 0; index1 < Mth.nextInt(RandomSource.create(), 0, 2); index1++) {
				if (level() instanceof ServerLevel _level) {
					ItemEntity entityToSpawn = new ItemEntity(_level, getX(), getY(), getZ(), new ItemStack(LostdepthsModItems.INFUSED_CRYSTAL.get()));
					entityToSpawn.setPickUpDelay(10);
					_level.addFreshEntity(entityToSpawn);
				}
			}
		}
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);

		if (!world.getLevelData().getGameRules().getBoolean(LostdepthsModGameRules.DOLOSTDEPTHSSPAWNING)) {
			if (!level().isClientSide())
				discard();
		}

		return retval;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("Texture", this.getTexture());
		addPersistentAngerSaveData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("Texture"))
			this.setTexture(compound.getString("Texture"));
		this.readPersistentAngerSaveData(this.level(), compound);
	}

	@Override
	public void baseTick() {
		super.baseTick();
		this.refreshDimensions();
	}

	@Override
	public EntityDimensions getDimensions(Pose p_33597_) {
		return super.getDimensions(p_33597_).scale((float) 1);
	}

	public static void init() {
		SpawnPlacements.register(LostdepthsModEntities.THE_PROTECTOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (entityType, world, reason, pos, random) -> {
			if (world.getRawBrightness(pos, 0) <= 8)
				return false;

            return world.getLevelData().getGameRules().getBoolean(LostdepthsModGameRules.DOLOSTDEPTHSSPAWNING) && ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == Level.NETHER
                    || (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("lostdepths:below_bedrock")))
                    || (world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == Level.OVERWORLD);
        });
	}

	public static AttributeSupplier.Builder createAttributes() {
		AttributeSupplier.Builder builder = Mob.createMobAttributes();
		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
		builder = builder.add(Attributes.MAX_HEALTH, 300);
		builder = builder.add(Attributes.ARMOR, 2);
		builder = builder.add(Attributes.ATTACK_DAMAGE, 15);
		builder = builder.add(Attributes.FOLLOW_RANGE, 25);
		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
		builder = builder.add(Attributes.ATTACK_KNOCKBACK, 2.5);
		return builder;
	}

	private PlayState movementPredicate(AnimationState<TheProtectorEntity> event) {
		//animationprocedure = "empty";

		if (isSprinting()) {
			animationProcedure = "animation.the_protector.move";
			return event.setAndContinue(RawAnimation.begin().thenLoop(animationProcedure));
		} else if (this.isAggressive() && event.isMoving()) {
			animationProcedure = "animation.the_protector.attack";
			return event.setAndContinue(RawAnimation.begin().thenLoop(animationProcedure));
		} else if (event.isMoving()) {
			animationProcedure = "animation.the_protector.walk";
			return event.setAndContinue(RawAnimation.begin().thenLoop(animationProcedure));
		} else {
			animationProcedure = "empty";
			event.resetCurrentAnimation();
			return PlayState.STOP;
		}
	}

	private PlayState procedurePredicate(AnimationState<TheProtectorEntity> event) {
		if (animationProcedure.equals("empty")) {
			return PlayState.STOP;
		}

		return PlayState.CONTINUE;
	}

	@Override
	protected void tickDeath() {
		if (isPlayerCreated())
			return;

		++this.deathTime;
		if (this.deathTime == 20) {
			this.remove(TheProtectorEntity.RemovalReason.KILLED);
			this.dropExperience();
			// LogUtils.getLogger().warn("PROTECTOR DEATH TICK DEATH");
		}
	}

	public String getSyncedAnimation() {
		return this.entityData.get(ANIMATION);
	}

	public void setAnimation(String animation) {
		this.entityData.set(ANIMATION, animation);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
		data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return this.remainingPersistentAngerTime;
	}

	@Override
	public void setRemainingPersistentAngerTime(int pTime) {
		this.remainingPersistentAngerTime = pTime;
	}

	@Override
	public @Nullable UUID getPersistentAngerTarget() {
		return this.persistentAngerTarget;
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pTarget) {
		this.persistentAngerTarget = pTarget;
	}

	@Override
	public void startPersistentAngerTimer() {
		this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
	}


}
