package cz.blackdragoncz.lostdepths.block.creative.entity;

import cz.blackdragoncz.lostdepths.disruptor.RealityDisruptorManager;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.Locale;

// Creative-only RP block: takes control of what a nearby player can see. Every setting is per block so several can be
// placed as a puzzle, each with its own radius and effect.
public class RealityDisruptorBlockEntity extends BlockEntity {

    public enum Camera {
        FREE, FIRST, THIRD_BACK, THIRD_FRONT;

        public static Camera byName(String name) {
            for (Camera c : values())
                if (c.name().toLowerCase(Locale.ROOT).equals(name))
                    return c;
            return FREE;
        }
    }

    public enum Debug {
        NORMAL, FIXED, OBFUSCATED;

        public static Debug byName(String name) {
            for (Debug d : values())
                if (d.name().toLowerCase(Locale.ROOT).equals(name))
                    return d;
            return NORMAL;
        }
    }

    private int range = 5;
    private boolean hideGui = true;
    private Debug debug = Debug.FIXED;
    private Camera camera = Camera.FREE;
    private String shader = "";
    private int tickCounter;

    public RealityDisruptorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.REALITY_DISRUPTOR.get(), pos, state);
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = Math.max(1, Math.min(256, range));
        setChanged();
    }

    public boolean isHideGui() {
        return hideGui;
    }

    public void setHideGui(boolean hideGui) {
        this.hideGui = hideGui;
        setChanged();
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
        setChanged();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
        setChanged();
    }

    public String getShader() {
        return shader;
    }

    public void setShader(String shader) {
        this.shader = shader == null ? "" : shader;
        setChanged();
    }

    public void serverTick() {
        if (level == null || level.isClientSide)
            return;
        if (++tickCounter < 5)
            return;
        tickCounter = 0;

        BlockPos pos = getBlockPos();
        for (ServerPlayer player : ((ServerLevel) level).getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(range))) {
            double distSq = player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
            if (distSq <= (double) range * range)
                RealityDisruptorManager.offer(player, pos, distSq, hideGui, debug, camera, shader);
        }
    }

    public void onRemoved() {
        if (level != null && !level.isClientSide)
            RealityDisruptorManager.removeDisruptor(getBlockPos());
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("reality_range", range);
        tag.putBoolean("reality_hide_gui", hideGui);
        tag.putString("reality_debug", debug.name());
        tag.putString("reality_camera", camera.name());
        tag.putString("reality_shader", shader);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("reality_range"))
            range = tag.getInt("reality_range");
        if (tag.contains("reality_hide_gui"))
            hideGui = tag.getBoolean("reality_hide_gui");
        if (tag.contains("reality_debug"))
            debug = Debug.byName(tag.getString("reality_debug").toLowerCase(Locale.ROOT));
        if (tag.contains("reality_camera"))
            camera = Camera.byName(tag.getString("reality_camera").toLowerCase(Locale.ROOT));
        if (tag.contains("reality_shader"))
            shader = tag.getString("reality_shader");
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithFullMetadata();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
