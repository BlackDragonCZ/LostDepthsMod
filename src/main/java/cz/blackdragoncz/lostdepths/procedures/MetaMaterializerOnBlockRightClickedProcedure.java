package cz.blackdragoncz.lostdepths.procedures;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;

public class MetaMaterializerOnBlockRightClickedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		boolean isThere = false;
		double amount1 = 0;
		double amount2 = 0;
		double amount3 = 0;
		double need1n = 0;
		double need2n = 0;
		double need3n = 0;
		ItemStack item1 = ItemStack.EMPTY;
		ItemStack item2 = ItemStack.EMPTY;
		ItemStack item3 = ItemStack.EMPTY;
		ItemStack need1 = ItemStack.EMPTY;
		ItemStack need2 = ItemStack.EMPTY;
		ItemStack need3 = ItemStack.EMPTY;
		ItemStack handItem = ItemStack.EMPTY;
		ItemStack result = ItemStack.EMPTY;
		isThere = false;
		if (true) {
			handItem = new ItemStack(LostdepthsModItems.MAGMA_SOLUTION.get());
			need1 = new ItemStack(LostdepthsModItems.MELWORITE_INGOT.get());
			need2 = new ItemStack(LostdepthsModItems.MORFARITE_INGOT.get());
			need3 = new ItemStack(LostdepthsModItems.IONITE_CRYSTAL.get());
			result = new ItemStack(LostdepthsModItems.MALICIUM_INGOT.get());
			need1n = 1;
			need2n = 1;
			need3n = 1;
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == handItem.getItem()) {
				item1 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 6, y - 2, z), 0));
				item2 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 12, y - 2, z), 0));
				item3 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 19, y - 2, z), 0));
				if (!(item1.getItem() == ItemStack.EMPTY.getItem()) && !(item2.getItem() == ItemStack.EMPTY.getItem()) && !(item3.getItem() == ItemStack.EMPTY.getItem())) {
					amount1 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 6, y - 2, z), 0);
					amount2 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 12, y - 2, z), 0);
					amount3 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 19, y - 2, z), 0);
					isThere = true;
				} else {
					isThere = false;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] One or more Meta Collectors are empty."), false);
				}
				if (isThere == true && item1.getItem() == need1.getItem() && item2.getItem() == need2.getItem() && item3.getItem() == need3.getItem()) {
					if (amount1 >= need1n && amount2 >= need2n && amount3 >= need3n) {
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 6, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item1;
								_setstack.setCount((int) (amount1 - need1n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 12, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item2;
								_setstack.setCount((int) (amount2 - need2n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 19, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item3;
								_setstack.setCount((int) (amount3 - need3n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = handItem;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						isThere = false;
					} else {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have enough of valid items for this recipe."), false);
					}
				} else {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have valid items for this recipe."), false);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("[LD] You are not holding correct item."), false);
			}
		}
		if (true) {
			handItem = new ItemStack(LostdepthsModItems.WEAK_POLARCRONITE.get());
			need1 = new ItemStack(LostdepthsModItems.HYPERIUM_INGOT.get());
			need2 = new ItemStack(LostdepthsModItems.VOLATILE_BLOOD.get());
			need3 = new ItemStack(LostdepthsModItems.PLUCKED_EYE.get());
			result = new ItemStack(LostdepthsModItems.POLARIUM_INGOT.get());
			need1n = 1;
			need2n = 1;
			need3n = 1;
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == handItem.getItem()) {
				item1 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 6, y - 2, z), 0));
				item2 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 12, y - 2, z), 0));
				item3 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 19, y - 2, z), 0));
				if (!(item1.getItem() == ItemStack.EMPTY.getItem()) && !(item2.getItem() == ItemStack.EMPTY.getItem()) && !(item3.getItem() == ItemStack.EMPTY.getItem())) {
					amount1 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 6, y - 2, z), 0);
					amount2 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 12, y - 2, z), 0);
					amount3 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 19, y - 2, z), 0);
					isThere = true;
				} else {
					isThere = false;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] One or more Meta Collectors are empty."), false);
				}
				if (isThere == true && item1.getItem() == need1.getItem() && item2.getItem() == need2.getItem() && item3.getItem() == need3.getItem()) {
					if (amount1 >= need1n && amount2 >= need2n && amount3 >= need3n) {
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 6, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item1;
								_setstack.setCount((int) (amount1 - need1n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 12, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item2;
								_setstack.setCount((int) (amount2 - need2n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 19, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item3;
								_setstack.setCount((int) (amount3 - need3n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = handItem;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						isThere = false;
					} else {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have enough of valid items for this recipe."), false);
					}
				} else {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have valid items for this recipe."), false);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("[LD] You are not holding correct item."), false);
			}
		}
		if (true) {
			handItem = new ItemStack(LostdepthsModItems.POWER_CORE.get());
			need1 = new ItemStack(LostdepthsModItems.NECROTONITE_INGOT.get());
			need2 = new ItemStack(LostdepthsModItems.RAZOR_FANG.get());
			need3 = new ItemStack(LostdepthsModItems.INFUSED_GOLEM_ESSENCE.get());
			result = new ItemStack(LostdepthsModItems.ETHERIUM_INGOT.get());
			need1n = 1;
			need2n = 1;
			need3n = 1;
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == handItem.getItem()) {
				item1 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 6, y - 2, z), 0));
				item2 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 12, y - 2, z), 0));
				item3 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 19, y - 2, z), 0));
				if (!(item1.getItem() == ItemStack.EMPTY.getItem()) && !(item2.getItem() == ItemStack.EMPTY.getItem()) && !(item3.getItem() == ItemStack.EMPTY.getItem())) {
					amount1 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 6, y - 2, z), 0);
					amount2 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 12, y - 2, z), 0);
					amount3 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 19, y - 2, z), 0);
					isThere = true;
				} else {
					isThere = false;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] One or more Meta Collectors are empty."), false);
				}
				if (isThere == true && item1.getItem() == need1.getItem() && item2.getItem() == need2.getItem() && item3.getItem() == need3.getItem()) {
					if (amount1 >= need1n && amount2 >= need2n && amount3 >= need3n) {
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 6, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item1;
								_setstack.setCount((int) (amount1 - need1n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 12, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item2;
								_setstack.setCount((int) (amount2 - need2n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 19, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item3;
								_setstack.setCount((int) (amount3 - need3n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = handItem;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						isThere = false;
					} else {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have enough of valid items for this recipe."), false);
					}
				} else {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have valid items for this recipe."), false);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("[LD] You are not holding correct item."), false);
			}
		}
		if (true) {
			handItem = new ItemStack(LostdepthsModItems.CLOVINITE_POWER_BANK.get());
			need1 = new ItemStack(LostdepthsModItems.COGNITIUM_INGOT.get());
			need2 = new ItemStack(LostdepthsModItems.GIANT_DUSKER_EGGS.get());
			need3 = new ItemStack(LostdepthsModItems.CRYSTAL_DIODE.get());
			result = new ItemStack(LostdepthsModItems.KYVORIUM_INGOT.get());
			need1n = 1;
			need2n = 1;
			need3n = 1;
			if ((entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == handItem.getItem()) {
				item1 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 6, y - 2, z), 0));
				item2 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 12, y - 2, z), 0));
				item3 = (new Object() {
					public ItemStack getItemStack(LevelAccessor world, BlockPos pos, int slotid) {
						AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
						BlockEntity _ent = world.getBlockEntity(pos);
						if (_ent != null)
							_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).copy()));
						return _retval.get();
					}
				}.getItemStack(world, BlockPos.containing(x + 19, y - 2, z), 0));
				if (!(item1.getItem() == ItemStack.EMPTY.getItem()) && !(item2.getItem() == ItemStack.EMPTY.getItem()) && !(item3.getItem() == ItemStack.EMPTY.getItem())) {
					amount1 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 6, y - 2, z), 0);
					amount2 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 12, y - 2, z), 0);
					amount3 = new Object() {
						public int getAmount(LevelAccessor world, BlockPos pos, int slotid) {
							AtomicInteger _retval = new AtomicInteger(0);
							BlockEntity _ent = world.getBlockEntity(pos);
							if (_ent != null)
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> _retval.set(capability.getStackInSlot(slotid).getCount()));
							return _retval.get();
						}
					}.getAmount(world, BlockPos.containing(x + 19, y - 2, z), 0);
					isThere = true;
				} else {
					isThere = false;
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] One or more Meta Collectors are empty."), false);
				}
				if (isThere == true && item1.getItem() == need1.getItem() && item2.getItem() == need2.getItem() && item3.getItem() == need3.getItem()) {
					if (amount1 >= need1n && amount2 >= need2n && amount3 >= need3n) {
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 6, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item1;
								_setstack.setCount((int) (amount1 - need1n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 12, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item2;
								_setstack.setCount((int) (amount2 - need2n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						{
							BlockEntity _ent = world.getBlockEntity(BlockPos.containing(x + 19, y - 2, z));
							if (_ent != null) {
								final int _slotid = 0;
								final ItemStack _setstack = item3;
								_setstack.setCount((int) (amount3 - need3n));
								_ent.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
									if (capability instanceof IItemHandlerModifiable)
										((IItemHandlerModifiable) capability).setStackInSlot(_slotid, _setstack);
								});
							}
						}
						if (entity instanceof Player _player) {
							ItemStack _stktoremove = handItem;
							_player.getInventory().clearOrCountMatchingItems(p -> _stktoremove.getItem() == p.getItem(), 1, _player.inventoryMenu.getCraftSlots());
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						if (world instanceof ServerLevel _level) {
							ItemEntity entityToSpawn = new ItemEntity(_level, (x + 1), y, z, result);
							entityToSpawn.setPickUpDelay(1);
							entityToSpawn.setUnlimitedLifetime();
							_level.addFreshEntity(entityToSpawn);
						}
						isThere = false;
					} else {
						if (entity instanceof Player _player && !_player.level().isClientSide())
							_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have enough of valid items for this recipe."), false);
					}
				} else {
					if (entity instanceof Player _player && !_player.level().isClientSide())
						_player.displayClientMessage(Component.literal("[LD] Meta Collectors don't have valid items for this recipe."), false);
				}
			} else {
				if (entity instanceof Player _player && !_player.level().isClientSide())
					_player.displayClientMessage(Component.literal("[LD] You are not holding correct item."), false);
			}
		}
	}
}
