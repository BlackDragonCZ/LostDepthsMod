
package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.recipe.LDShapedRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;

import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import net.minecraft.world.item.ItemStack;

public class AlloyWorkstationMenu extends AbstractWorkstationMenu {

	private CustomResultSlot<LDShapedRecipe> customResultSlot;

	public AlloyWorkstationMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
		super(LostdepthsModMenus.ALLOY_WORKSTATION_MENU.get(), 9, LostDepthsModRecipeType.ALLOY_WORKSTATION.get(), id, inv, extraData, 257);

		this.addSlot(customResultSlot = new CustomResultSlot<>(LostDepthsModRecipeType.ALLOY_WORKSTATION.get(), this, inv.player, this.craftingContainer, resultContainer, 0, 142, 55) {
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

		this.customSlots.put(0, this.addSlot(new Slot(containerWrapper, 0, 7, 46)));
		this.customSlots.put(1, this.addSlot(new Slot(containerWrapper, 1, 34, 37)));
		this.customSlots.put(2, this.addSlot(new Slot(containerWrapper, 2, 61, 46)));


		this.customSlots.put(3, this.addSlot(new Slot(containerWrapper, 4, 34, 64)));

		this.customSlots.put(4, this.addSlot(new Slot(containerWrapper, 6, 7, 73)));
		this.customSlots.put(5, this.addSlot(new Slot(containerWrapper, 7, 34, 91)));
		this.customSlots.put(6, this.addSlot(new Slot(containerWrapper, 8, 61, 73)));

		for (int si = 0; si < 3; ++si)
			for (int sj = 0; sj < 9; ++sj)
				this.addSlot(new Slot(inv, sj + (si + 1) * 9, 0 + 8 + sj * 18, 42 + 84 + si * 18));
		for (int si = 0; si < 9; ++si)
			this.addSlot(new Slot(inv, si, 0 + 8 + si * 18, 42 + 142));

		slotsChanged(this.craftingContainer);
	}

	@Override
	public CustomResultSlot<LDShapedRecipe> getResultSlot() {
		return customResultSlot;
	}
}
