package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.power.entity.NurostarBatteryBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NurostarBatteryMenu extends AbstractContainerMenu {

    private static final int MACHINE_SLOTS = 2;
    private final Player player;
    private final Level level;
    private NurostarBatteryBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private final ContainerData energyData;

    public NurostarBatteryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(LostdepthsModMenus.NUROSTAR_BATTERY_MENU.get(), id);
        this.player = inv.player;
        this.level = inv.player.level();
        BlockPos pos = extraData.readBlockPos();

        // Try small battery first, then large
        this.level.getBlockEntity(pos, LostdepthsModBlockEntities.NUROSTAR_BATTERY.get()).ifPresent(e -> this.blockEntity = e);
        if (this.blockEntity == null) {
            this.level.getBlockEntity(pos, LostdepthsModBlockEntities.NUROSTAR_LARGE_BATTERY.get()).ifPresent(e -> this.blockEntity = e);
        }

        this.access = ContainerLevelAccess.create(this.level, pos);

        // Sync energy data to client: [0]=stored, [1]=capacity
        if (this.blockEntity != null && !level.isClientSide) {
            this.energyData = new ContainerData() {
                @Override
                public int get(int index) {
                    EnergyStorage es = blockEntity.getEnergyStorage();
                    return index == 0 ? es.getEnergyStored() : es.getMaxEnergyStored();
                }
                @Override
                public void set(int index, int value) {}
                @Override
                public int getCount() { return 2; }
            };
        } else {
            this.energyData = new SimpleContainerData(2);
        }
        this.addDataSlots(this.energyData);

        IItemHandler[] itemHandler = new IItemHandler[1];
        if (this.blockEntity != null) {
            this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> itemHandler[0] = cap);
        }

        if (itemHandler[0] != null) {
            this.addSlot(new SlotItemHandler(itemHandler[0], 0, 37, 54)); // charge
            this.addSlot(new SlotItemHandler(itemHandler[0], 1, 91, 55)); // drain
        }

        // Player inventory (y=127)
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(inv, col + (row + 1) * 9, 9 + col * 18, 127 + row * 18));
        // Player hotbar (y=185)
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(inv, col, 9 + col * 18, 185));
    }

    public int getEnergyStored() { return energyData.get(0); }
    public int getMaxEnergy() { return energyData.get(1); }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (index < MACHINE_SLOTS) {
                if (!this.moveItemStackTo(stack, MACHINE_SLOTS, MACHINE_SLOTS + 36, true))
                    return ItemStack.EMPTY;
            } else {
                if (!this.moveItemStackTo(stack, 0, MACHINE_SLOTS, false))
                    return ItemStack.EMPTY;
            }
            if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, this.blockEntity.getBlockState().getBlock());
    }

    public NurostarBatteryBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
