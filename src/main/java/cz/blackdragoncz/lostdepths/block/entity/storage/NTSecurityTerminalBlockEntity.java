package cz.blackdragoncz.lostdepths.block.entity.storage;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetwork;
import cz.blackdragoncz.lostdepths.storage.network.StorageNetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

/**
 * Security terminal — manages per-player permissions for the NT network.
 * The player who places this block becomes the network owner.
 * Permissions: insert, extract, autocraft.
 * Integrates with Lost Depths security pass system.
 */
public class NTSecurityTerminalBlockEntity extends BlockEntity implements StorageNetworkNode {

	private UUID ownerUUID;
	private String ownerName = "";
	private final Map<UUID, PlayerPermissions> permissions = new HashMap<>();
	private boolean defaultInsert = true;
	private boolean defaultExtract = true;
	private boolean defaultCraft = false;

	public NTSecurityTerminalBlockEntity(BlockPos pos, BlockState state) {
		super(LostdepthsModBlockEntities.NT_SECURITY_TERMINAL.get(), pos, state);
	}

	@Override
	public int getEnergyPerTick() {
		return 1;
	}

	// --- Owner ---

	public void setOwner(Player player) {
		this.ownerUUID = player.getUUID();
		this.ownerName = player.getName().getString();
		setChanged();
	}

	public UUID getOwnerUUID() {
		return ownerUUID;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public boolean isOwner(Player player) {
		return ownerUUID != null && ownerUUID.equals(player.getUUID());
	}

	// --- Permissions ---

	public boolean canInsert(Player player) {
		if (isOwner(player)) return true;
		PlayerPermissions perms = permissions.get(player.getUUID());
		return perms != null ? perms.insert : defaultInsert;
	}

	public boolean canExtract(Player player) {
		if (isOwner(player)) return true;
		PlayerPermissions perms = permissions.get(player.getUUID());
		return perms != null ? perms.extract : defaultExtract;
	}

	public boolean canCraft(Player player) {
		if (isOwner(player)) return true;
		PlayerPermissions perms = permissions.get(player.getUUID());
		return perms != null ? perms.craft : defaultCraft;
	}

	public void setPlayerPermissions(UUID playerUUID, boolean insert, boolean extract, boolean craft) {
		permissions.put(playerUUID, new PlayerPermissions(insert, extract, craft));
		setChanged();
	}

	public void removePlayerPermissions(UUID playerUUID) {
		permissions.remove(playerUUID);
		setChanged();
	}

	public PlayerPermissions getPlayerPermissions(UUID playerUUID) {
		return permissions.get(playerUUID);
	}

	public Map<UUID, PlayerPermissions> getAllPermissions() {
		return Collections.unmodifiableMap(permissions);
	}

	public void setDefaultInsert(boolean value) { defaultInsert = value; setChanged(); }
	public void setDefaultExtract(boolean value) { defaultExtract = value; setChanged(); }
	public void setDefaultCraft(boolean value) { defaultCraft = value; setChanged(); }
	public boolean getDefaultInsert() { return defaultInsert; }
	public boolean getDefaultExtract() { return defaultExtract; }
	public boolean getDefaultCraft() { return defaultCraft; }

	// --- NBT ---

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.hasUUID("Owner")) {
			ownerUUID = tag.getUUID("Owner");
			ownerName = tag.getString("OwnerName");
		}
		defaultInsert = tag.getBoolean("DefaultInsert");
		defaultExtract = tag.getBoolean("DefaultExtract");
		defaultCraft = tag.getBoolean("DefaultCraft");

		permissions.clear();
		ListTag permList = tag.getList("Permissions", Tag.TAG_COMPOUND);
		for (int i = 0; i < permList.size(); i++) {
			CompoundTag entry = permList.getCompound(i);
			UUID uuid = entry.getUUID("UUID");
			boolean insert = entry.getBoolean("Insert");
			boolean extract = entry.getBoolean("Extract");
			boolean craft = entry.getBoolean("Craft");
			permissions.put(uuid, new PlayerPermissions(insert, extract, craft));
		}
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		if (ownerUUID != null) {
			tag.putUUID("Owner", ownerUUID);
			tag.putString("OwnerName", ownerName);
		}
		tag.putBoolean("DefaultInsert", defaultInsert);
		tag.putBoolean("DefaultExtract", defaultExtract);
		tag.putBoolean("DefaultCraft", defaultCraft);

		ListTag permList = new ListTag();
		for (Map.Entry<UUID, PlayerPermissions> entry : permissions.entrySet()) {
			CompoundTag permTag = new CompoundTag();
			permTag.putUUID("UUID", entry.getKey());
			permTag.putBoolean("Insert", entry.getValue().insert);
			permTag.putBoolean("Extract", entry.getValue().extract);
			permTag.putBoolean("Craft", entry.getValue().craft);
			permList.add(permTag);
		}
		tag.put("Permissions", permList);
	}

	public static class PlayerPermissions {
		public final boolean insert;
		public final boolean extract;
		public final boolean craft;

		public PlayerPermissions(boolean insert, boolean extract, boolean craft) {
			this.insert = insert;
			this.extract = extract;
			this.craft = craft;
		}
	}
}
