package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;

import cz.blackdragoncz.lostdepths.init.LostdepthsModMobEffects;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;

public class SecurityClearanceGate1OnBlockRightClickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_1.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_2.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_3.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_4.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_5.get()
				|| (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == LostdepthsModItems.SECURITY_PASS_6.get()) {
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS_2.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS_3.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS_4.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS_5.get());
			if (entity instanceof LivingEntity _entity)
				_entity.removeEffect(LostdepthsModMobEffects.SECURITY_BYPASS_6.get());
			if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITY_CLEARANCE_GATE_1.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_1")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS.get(), 600, 0, true, false));
				}
			} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITYCLEARANCEGATE_2.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_2")))) {
					if (!(entity instanceof ServerPlayer _plr28 && _plr28.level() instanceof ServerLevel
							&& _plr28.getAdvancements().getOrStartProgress(_plr28.server.getAdvancements().getAdvancement(new ResourceLocation("lostdepths:ruins_2_adv"))).isDone())) {
						if (entity instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("lostdepths:ruins_2_adv"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								for (String criteria : _ap.getRemainingCriteria())
									_player.getAdvancements().award(_adv, criteria);
							}
						}
					}
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS_2.get(), 600, 0, true, false));
				}
			} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITY_CLEARANCE_3.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_3")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS_3.get(), 600, 0, true, false));
				}
			} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITY_CLEARANCE_4.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_4")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS_4.get(), 400, 0, true, false));
				}
			} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITY_CLEARANCE_5.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_5")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS_5.get(), 400, 0, true, false));
				}
			} else if ((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == LostdepthsModBlocks.SECURITY_CLEARANCE_6.get()) {
				if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("lostdepths:security_6")))) {
					if (world instanceof Level _level) {
						if (!_level.isClientSide()) {
							_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1);
						} else {
							_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:security_gate_clear")), SoundSource.MASTER, 1, 1, false);
						}
					}
					if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
						_entity.addEffect(new MobEffectInstance(LostdepthsModMobEffects.SECURITY_BYPASS_6.get(), 400, 0, true, false));
				}
			}
		} else {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:gate_error")), SoundSource.MASTER, 1, 1);
				} else {
					_level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("lostdepths:gate_error")), SoundSource.MASTER, 1, 1, false);
				}
			}
		}
	}
}
