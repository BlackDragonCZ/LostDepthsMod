package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

// Holds where the hologram sits, not what it looks like: scale and tint stay in the template's definition json, placement is per block. The template
// itself never travels - every client bakes from its own assets, so this syncs a handful of bytes.
public class HologramProjectorBlockEntity extends BlockEntity {

	public static final float MAX_OFFSET = 32.0f;
	private static final double RENDER_RADIUS = 32.0D;

	@Nullable
	private ResourceLocation hologram;
	private float offsetX = 0.0f;
	private float offsetY = 1.0f;
	private float offsetZ = 0.0f;
	private float rotationX = 0.0f;
	private float rotationY = 0.0f;
	private float rotationZ = 0.0f;
	private boolean visible = true;
	private boolean culled = false;

	public HologramProjectorBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.HOLOGRAM_PROJECTOR.get(), pos, state);
	}

	@Nullable
	public ResourceLocation getHologram() {
		return this.hologram;
	}

	public float offsetX() {
		return this.offsetX;
	}

	public float offsetY() {
		return this.offsetY;
	}

	public float offsetZ() {
		return this.offsetZ;
	}

	public float rotationX() {
		return this.rotationX;
	}

	public float rotationY() {
		return this.rotationY;
	}

	public float rotationZ() {
		return this.rotationZ;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public boolean isCulled() {
		return this.culled;
	}

	// Redstone flips whatever the Display/Hide buttons last set, so either control alone works and together they behave like a toggle.
	public boolean shouldRender() {
		if (this.hologram == null || this.level == null)
			return false;
		return this.visible ^ this.level.hasNeighborSignal(this.worldPosition);
	}

	public void configure(@Nullable ResourceLocation hologram, float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ, boolean visible, boolean culled) {
		this.hologram = hologram;
		this.offsetX = clampOffset(offsetX);
		this.offsetY = clampOffset(offsetY);
		this.offsetZ = clampOffset(offsetZ);
		this.rotationX = wrapDegrees(rotationX);
		this.rotationY = wrapDegrees(rotationY);
		this.rotationZ = wrapDegrees(rotationZ);
		this.visible = visible;
		this.culled = culled;
		this.setChanged();
		if (this.level != null && !this.level.isClientSide)
			this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
	}

	private static float clampOffset(float value) {
		if (!Float.isFinite(value))
			return 0.0f;
		return Math.max(-MAX_OFFSET, Math.min(MAX_OFFSET, value));
	}

	private static float wrapDegrees(float value) {
		if (!Float.isFinite(value))
			return 0.0f;
		float wrapped = value % 360.0f;
		return wrapped < 0 ? wrapped + 360.0f : wrapped;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (this.hologram != null)
			tag.putString("Hologram", this.hologram.toString());
		tag.putFloat("OffX", this.offsetX);
		tag.putFloat("OffY", this.offsetY);
		tag.putFloat("OffZ", this.offsetZ);
		tag.putFloat("RotX", this.rotationX);
		tag.putFloat("RotY", this.rotationY);
		tag.putFloat("RotZ", this.rotationZ);
		tag.putBoolean("Visible", this.visible);
		tag.putBoolean("Culled", this.culled);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.hologram = tag.contains("Hologram") ? ResourceLocation.tryParse(tag.getString("Hologram")) : null;
		this.offsetX = tag.getFloat("OffX");
		this.offsetY = tag.contains("OffY") ? tag.getFloat("OffY") : 1.0f;
		this.offsetZ = tag.getFloat("OffZ");
		this.rotationX = tag.getFloat("RotX");
		this.rotationY = tag.getFloat("RotY");
		this.rotationZ = tag.getFloat("RotZ");
		this.visible = !tag.contains("Visible") || tag.getBoolean("Visible");
		this.culled = tag.getBoolean("Culled");
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		this.saveAdditional(tag);
		return tag;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	// A scaled structure reaches well past the projector, and the offset can push it further still.
	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(this.worldPosition).inflate(RENDER_RADIUS);
	}
}
