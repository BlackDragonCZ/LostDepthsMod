
package cz.blackdragoncz.lostdepths.world.inventory;

import com.mojang.logging.LogUtils;
import cz.blackdragoncz.lostdepths.block.entity.GalacticWorkstationBlockEntity;
import cz.blackdragoncz.lostdepths.client.gui.ContainerWrapper;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.recipe.LDShapedRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;

public class WorkstationMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public final static HashMap<String, Object> guistate = new HashMap<>();
	public final Level world;
	public final Player entity;
	public int x, y, z;
	private ContainerLevelAccess access = ContainerLevelAccess.NULL;
	private IItemHandler itemHandler;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private boolean bound = false;
	private Supplier<Boolean> boundItemMatcher = null;
	private Entity boundEntity = null;
	private GalacticWorkstationBlockEntity boundBlockEntity = null;

	private ContainerWrapper containerWrapper;
	private ResultContainer resultContainer = new ResultContainer();

	public WorkstationMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(LostdepthsModMenus.WSGUI_1.get(), id);
		this.entity = inv.player;
		this.world = inv.player.level();
		this.itemHandler = new ItemStackHandler(7);
		BlockPos pos = null;
		if (extraData != null) {
			pos = extraData.readBlockPos();
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			access = ContainerLevelAccess.create(world, pos);
		}

		if (pos != null) {
			boundBlockEntity = (GalacticWorkstationBlockEntity)this.world.getBlockEntity(pos);
			if (boundBlockEntity != null) {
				boundBlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
					this.itemHandler = capability;
					this.bound = true;
					this.containerWrapper = new ContainerWrapper(boundBlockEntity) {
						@Override
						public void setChanged() {
							super.setChanged();
							WorkstationMenu.this.slotsChanged(this);
						}
					};
				});
			}
		}

		this.addSlot(new ResultSlot(inv.player, this.boundBlockEntity, resultContainer, 0, 142, 35) {

			@Override
			public void onTake(Player pPlayer, ItemStack pStack) {
				this.checkTakeAchievements(pStack);
				net.minecraftforge.common.ForgeHooks.setCraftingPlayer(pPlayer);
				NonNullList<ItemStack> nonnulllist = pPlayer.level().getRecipeManager().getRemainingItemsFor(LostDepthsModRecipeType.GALACTIC_WORKSTATION.get(), boundBlockEntity, pPlayer.level());
				net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
				for(int i = 0; i < nonnulllist.size(); ++i) {
					ItemStack itemstack = boundBlockEntity.getItem(i);
					ItemStack itemstack1 = nonnulllist.get(i);
					if (!itemstack.isEmpty()) {
						boundBlockEntity.removeItem(i, 1);
						itemstack = boundBlockEntity.getItem(i);
					}

					if (!itemstack1.isEmpty()) {
						if (itemstack.isEmpty()) {
							boundBlockEntity.setItem(i, itemstack1);
						} else if (ItemStack.isSameItemSameTags(itemstack, itemstack1)) {
							itemstack1.grow(itemstack.getCount());
							boundBlockEntity.setItem(i, itemstack1);
						} else if (!entity.getInventory().add(itemstack1)) {
							entity.drop(itemstack1, false);
						}
					}
				}

				WorkstationMenu.this.slotsChanged(containerWrapper);
			}
		});

		int uiIndex = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 3; x++) {
				int index = x + y * 3;

				if (index == 4 || index == 6 || index == 8)
					continue;

				this.customSlots.put(uiIndex++, this.addSlot(new Slot(containerWrapper, index, 16 + x * 18, 17 + y * 18)));
			}
		}

		// Player inventory slots
		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 0 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, 0 + 142));

		slotsChanged(this.boundBlockEntity);
	}

	@Override
	public void slotsChanged(Container pContainer) {
		this.access.execute(((level, blockPos) -> {
			if (!level.isClientSide) {
				ServerPlayer serverplayer = (ServerPlayer)this.entity;

				Optional<LDShapedRecipe> recipe = level.getRecipeManager().getRecipeFor(LostDepthsModRecipeType.GALACTIC_WORKSTATION.get(), this.boundBlockEntity, level);

				ItemStack stack = ItemStack.EMPTY;

				if (recipe.isPresent()) {
					ItemStack resultItem = recipe.get().assemble(this.boundBlockEntity, level.registryAccess());

					if (resultItem.isItemEnabled(level.enabledFeatures())) {
						stack = resultItem;
					}
				}

				resultContainer.setItem(0, stack);
				setRemoteSlot(0, stack);

				serverplayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 0, stack));
			}
		}));
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.bound) {
			if (this.boundItemMatcher != null)
				return this.boundItemMatcher.get();
			else if (this.boundBlockEntity != null)
				return AbstractContainerMenu.stillValid(this.access, player, this.boundBlockEntity.getBlockState().getBlock());
			else if (this.boundEntity != null)
				return this.boundEntity.isAlive();
		}
		return true;
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = (Slot) this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < 7) {
				if (!this.moveItemStackTo(itemstack1, 7, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, 7, false)) {
				if (index < 7 + 27) {
					if (!this.moveItemStackTo(itemstack1, 7 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemstack1, 7, 7 + 27, false))
						return ItemStack.EMPTY;
				}
				return ItemStack.EMPTY;
			}
			if (itemstack1.getCount() == 0)
				slot.set(ItemStack.EMPTY);
			else
				slot.setChanged();
			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;
			slot.onTake(playerIn, itemstack1);
		}
		return itemstack;
	}

	@Override
	protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;
		if (reverseDirection) {
			i = endIndex - 1;
		}
		if (stack.isStackable()) {
			while (!stack.isEmpty()) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}
				Slot slot = this.slots.get(i);
				ItemStack itemstack = slot.getItem();
				if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
					int j = itemstack.getCount() + stack.getCount();
					int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
					if (j <= maxSize) {
						stack.setCount(0);
						itemstack.setCount(j);
						slot.set(itemstack);
						flag = true;
					} else if (itemstack.getCount() < maxSize) {
						stack.shrink(maxSize - itemstack.getCount());
						itemstack.setCount(maxSize);
						slot.set(itemstack);
						flag = true;
					}
				}
				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}
		if (!stack.isEmpty()) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}
			while (true) {
				if (reverseDirection) {
					if (i < startIndex) {
						break;
					}
				} else if (i >= endIndex) {
					break;
				}
				Slot slot1 = this.slots.get(i);
				ItemStack itemstack1 = slot1.getItem();
				if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
					if (stack.getCount() > slot1.getMaxStackSize()) {
						slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
					} else {
						slot1.setByPlayer(stack.split(stack.getCount()));
					}
					slot1.setChanged();
					flag = true;
					break;
				}
				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}
		return flag;
	}

	@Override
	public void removed(Player playerIn) {
		super.removed(playerIn);
		if (!bound && playerIn instanceof ServerPlayer serverPlayer) {
			if (!serverPlayer.isAlive() || serverPlayer.hasDisconnected()) {
				for (int j = 0; j < itemHandler.getSlots(); ++j) {
					playerIn.drop(itemHandler.extractItem(j, itemHandler.getStackInSlot(j).getCount(), false), false);
				}
			} else {
				for (int i = 0; i < itemHandler.getSlots(); ++i) {
					playerIn.getInventory().placeItemBackInInventory(itemHandler.extractItem(i, itemHandler.getStackInSlot(i).getCount(), false));
				}
			}
		}
	}

	public Map<Integer, Slot> get() {
		return customSlots;
	}
}
