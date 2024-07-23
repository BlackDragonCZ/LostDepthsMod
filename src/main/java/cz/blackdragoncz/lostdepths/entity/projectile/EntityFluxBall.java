package cz.blackdragoncz.lostdepths.entity.projectile;

import cz.blackdragoncz.lostdepths.util.BasicTeleporter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class EntityFluxBall extends ThrowableProjectile {
    private ResourceKey<DimensionType> dim;
    private double teleportx;
    private double teleporty;
    private double teleportz;

    public EntityFluxBall(EntityType<? extends EntityFluxBall> type, Level world){
        super(type, world);
    }

    public EntityFluxBall(Level world, LivingEntity entityLiving){
        super(EntityType.SNOWBALL, entityLiving, world);
    }

    public EntityFluxBall(Level world, double x, double y, double z){
        super(EntityType.SNOWBALL, x, y, z, world);
    }

    public void setFlux(ResourceKey<DimensionType> d, double x, double y, double z){
        this.dim = d;
        this.teleportx = x;
        this.teleporty = y;
        this.teleportz = z;
    }

    @Override
    protected void onHitEntity(EntityHitResult result){
        if (!this.level().isClientSide){
            if (result.getEntity() != null && getOwner() != null && result.getEntity() != getOwner() && result.getEntity() instanceof Player){
                Player player = (Player) result.getEntity();
                if (!player.level().dimension().equals(this.dim)){
                     BasicTeleporter basicTeleporter = new BasicTeleporter(player.getServer().getLevel(this.dim), this.teleportx, this.teleporty, this.teleportz);
                    player.changeDimension(player.getServer().getLevel(this.dim), basicTeleporter);
                } else {
                    player.teleportTo(this.teleportx, this.teleporty, this.teleportz);
                }
            }

            this.remove(RemovalReason.KILLED);
        }
        if (result.getEntity() != null && result.getEntity() instanceof Player){
            this.playSound(SoundEvents.WITCH_THROW, 3.0f, 1.0f);
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
