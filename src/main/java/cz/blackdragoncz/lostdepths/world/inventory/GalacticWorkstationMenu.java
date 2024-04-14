
package cz.blackdragoncz.lostdepths.world.inventory;

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

public class GalacticWorkstationMenu extends AbstractWorkstationMenu {

	private CustomResultSlot<LDShapedRecipe> customResultSlot;

	public GalacticWorkstationMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(LostdepthsModMenus.GALACTIC_WORKSTATION_MENU.get(), 8, LostDepthsModRecipeType.GALACTIC_WORKSTATION.get(), id, inv, extraData, 122);

		this.addSlot(customResultSlot = new CustomResultSlot<>(LostDepthsModRecipeType.GALACTIC_WORKSTATION.get(), this, inv.player, this.craftingContainer, resultContainer, 0, 142, 35) {
			@Override
			public boolean mayPickup(Player pPlayer) {
				if (energyAccessor != null) {
					if (energyAccessor.getEnergyStorage().getEnergyStored() < requiredEnergyToCraft) {
						return false;
					}
				}

				return super.mayPickup(pPlayer);
			}

			@Override
			public void onTake(Player pPlayer, ItemStack pStack) {
				super.onTake(pPlayer, pStack);
				if (energyAccessor != null) {
					energyAccessor.getEnergyStorage().extractEnergy(requiredEnergyToCraft, false);
				}
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

		slotsChanged(this.craftingContainer);
	}

	@Override
	public CustomResultSlot<LDShapedRecipe> getResultSlot() {
		return customResultSlot;
	}
}
