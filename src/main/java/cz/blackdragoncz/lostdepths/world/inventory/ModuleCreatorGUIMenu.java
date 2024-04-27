
package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.client.gui.ContainerWrapper;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.recipe.ModuleRecipe;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.*;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.block.entity.BlockEntity;
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

public class ModuleCreatorGUIMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
	public final static HashMap<String, Object> guistate = new HashMap<>();
	public final Level world;
	public final Player player;
	private ContainerLevelAccess access;
	private final Map<Integer, Slot> customSlots = new HashMap<>();
	private BlockEntity blockEntity = null;
	protected final int slotCount = 4;

	protected CraftingContainer craftingContainer;
	protected ContainerWrapper containerWrapper;

	protected final ResultContainer resultContainer = new ResultContainer();
	protected CustomResultSlot<ModuleRecipe> customResultSlot;

	public ModuleCreatorGUIMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(LostdepthsModMenus.MODULE_CREATOR_GUI.get(), id);
		this.player = inv.player;
		this.world = inv.player.level();
		BlockPos pos = extraData.readBlockPos();
		access = ContainerLevelAccess.create(world, pos);

		blockEntity = this.world.getBlockEntity(pos);

		if (this.blockEntity instanceof CraftingContainer) {
			this.craftingContainer = (CraftingContainer) this.blockEntity;
		}

		this.containerWrapper = new ContainerWrapper(this.craftingContainer) {
			@Override
			public void setChanged() {
				super.setChanged();
				ModuleCreatorGUIMenu.this.slotsChanged(this);
			}
		};

		//this.customSlots.put(0, this.addSlot(new Slot(containerWrapper, 0, 134, 35)));
		this.addSlot(customResultSlot = new CustomResultSlot<>(LostDepthsModRecipeType.MODULE_CREATOR.get(), this, inv.player, this.craftingContainer, this.resultContainer, 0, 134, 35));

		this.customSlots.put(0, this.addSlot(new Slot(containerWrapper, 0, 17, 17)));
		this.customSlots.put(1, this.addSlot(new Slot(containerWrapper, 1, 43, 17)));
		this.customSlots.put(2, this.addSlot(new Slot(containerWrapper, 2, 69, 17)));
		this.customSlots.put(3, this.addSlot(new Slot(containerWrapper, 3, 43, 54)));

		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 20 + 64 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, 142));

		slotsChanged(this.craftingContainer);
	}

	@Override
	public void slotsChanged(Container pContainer) {
		this.access.execute(((level, blockPos) -> {
			if (!level.isClientSide) {
				ServerPlayer serverplayer = (ServerPlayer)this.player;

				Optional<ModuleRecipe> foundRecipe = level.getRecipeManager().getRecipeFor(LostDepthsModRecipeType.MODULE_CREATOR.get(), this.craftingContainer, level);

				ItemStack stack = ItemStack.EMPTY;

				if (foundRecipe.isPresent()) {
					ItemStack resultItem = foundRecipe.get().assemble(this.craftingContainer, level.registryAccess());

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
		return AbstractContainerMenu.stillValid(this.access, player, this.blockEntity.getBlockState().getBlock());
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if (slot != null && slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();
			if (index < this.slotCount - 1) {
				if (!this.moveItemStackTo(itemstack1, this.slotCount - 1, this.slots.size(), true))
					return ItemStack.EMPTY;
				slot.onQuickCraft(itemstack1, itemstack);
			} else if (!this.moveItemStackTo(itemstack1, 0, this.slotCount - 1, false)) {
				if (index < this.slotCount - 1 + 27) {
					if (!this.moveItemStackTo(itemstack1, this.slotCount - 1 + 27, this.slots.size(), true))
						return ItemStack.EMPTY;
				} else {
					if (!this.moveItemStackTo(itemstack1, this.slotCount - 1, this.slotCount - 1 + 27, false))
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

	public Map<Integer, Slot> get() {
		return customSlots;
	}
}
