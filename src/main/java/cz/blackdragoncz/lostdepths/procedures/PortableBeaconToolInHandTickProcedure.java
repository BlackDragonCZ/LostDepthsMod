package cz.blackdragoncz.lostdepths.procedures;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class PortableBeaconToolInHandTickProcedure {
	public static void execute(Entity entity, ItemStack itemstack) {
		if (entity == null)
			return;
		String potionType = "";
		String potionList = "";
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.PORTABLE_BEACON.get()) {
			if (true) {
				potionList = "minecraft:glowing";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:fire_resistance";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:invisibility";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:jump_boost";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains("minecraft:jump_boost")) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:night_vision";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:regeneration";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 0 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:slow_falling";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:strength";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:speed";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
			if (true) {
				potionList = "minecraft:water_breathing";
				if ((itemstack.getOrCreateTag().getString("potionListA")).contains(potionList)) {
					potionType = potionList;
					{
						Entity _ent = entity;
						if (!_ent.level().isClientSide() && _ent.getServer() != null) {
							_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 0,
									_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), ("effect give @e[sort=nearest,distance=0..35] " + potionType + " 10 1 true"));
						}
					}
				}
			}
		}
	}
}
