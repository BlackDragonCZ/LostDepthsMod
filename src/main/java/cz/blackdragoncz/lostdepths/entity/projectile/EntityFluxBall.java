package cz.blackdragoncz.lostdepths.entity.projectile;

import cz.blackdragoncz.lostdepths.util.BasicTeleporter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityFluxBall extends ThrowableProjectile {
    private ResourceKey<Level> dim;
    private double teleportX;
    private double teleportY;
    private double teleportZ;

    public EntityFluxBall(EntityType<? extends EntityFluxBall> type, Level world){
        super(type, world);
    }

    public EntityFluxBall(Level world, LivingEntity entityLiving){
        super(EntityType.SNOWBALL, entityLiving, world);
    }

    public EntityFluxBall(Level world, double x, double y, double z){
        super(EntityType.SNOWBALL, x, y, z, world);
    }

    public void setFlux(ResourceKey<Level> dim, double x, double y, double z){
        this.dim = dim;
        this.teleportX = x;
        this.teleportY = y;
        this.teleportZ = z;
    }

    @Override
    protected void onHitEntity(EntityHitResult result){
        if (!this.level().isClientSide){
            if (getOwner() != null && result.getEntity() != getOwner()/* && result.getEntity() instanceof Player*/) {
                Entity hitEntity = result.getEntity();
                if (hitEntity.level().dimension() != this.dim){
                    ServerLevel level = hitEntity.getServer().getLevel(this.dim);

                    if (level == null) {
                        this.remove(RemovalReason.KILLED);
                        return;
                    }

                    BasicTeleporter basicTeleporter = new BasicTeleporter(level, this.teleportX, this.teleportY, this.teleportZ);
                    hitEntity.changeDimension(level, basicTeleporter);
                } else {
                    hitEntity.teleportTo(this.teleportX, this.teleportY, this.teleportZ);
                }

                this.playSound(SoundEvents.WITCH_THROW, 3.0f, 1.0f);
            }

            this.remove(RemovalReason.KILLED);
        }
    }



    @Override
    protected float getGravity(){
        return 0.05f;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void tick(){
        super.tick();
        if (this.level().isClientSide){
            this.level().addParticle(ParticleTypes.WITCH,
                    this.getX() + (this.random.nextDouble() - 0.5d) * this.getBbWidth(),
                    this.getY() + this.random.nextDouble() * this.getBbHeight() - 0.25D,
                    this.getZ() + (this.random.nextDouble() - 0.5D) * this.getBbWidth(),
                    (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
                    (this.random.nextDouble() - 0.5D) * 2.0D);
        }
    }

}
