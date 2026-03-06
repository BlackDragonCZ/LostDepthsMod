package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.power.entity.NurostarBatteryBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class NurostarBatteryMenu extends AbstractContainerMenu {

    private static final int MACHINE_SLOTS = 2;
    private final Player player;
    private final Level level;
    private NurostarBatteryBlockEntity blockEntity;
    private final ContainerLevelAccess access;

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

        IItemHandler[] itemHandler = new IItemHandler[1];
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> itemHandler[0] = cap);

        if (itemHandler[0] != null) {
            int leftSideWidth = 176 / 2;
            // Slot 0: charge input (left side)
            this.addSlot(new SlotItemHandler(itemHandler[0], 0, leftSideWidth / 2 - 19 - 2, 55));
            // Slot 1: drain output (right side — but still on left half)
            this.addSlot(new SlotItemHandler(itemHandler[0], 1, leftSideWidth / 2 + 3, 55));
        }

        // Player inventory
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(inv, col + (row + 1) * 9, 8 + col * 18, 126 + row * 18));
        // Player hotbar
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(inv, col, 8 + col * 18, 184));
    }

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
