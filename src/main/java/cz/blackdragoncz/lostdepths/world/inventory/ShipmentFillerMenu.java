package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.ShipmentFillerBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ShipmentFillerMenu extends AbstractContainerMenu {

    public static final int INPUT_SLOT = 0;
    public static final int BOX_SLOT = 1;
    private static final int DISPLAY_START = 2;
    private static final int DISPLAY_COUNT = 28;
    private static final int MACHINE_SLOT_COUNT = DISPLAY_START + DISPLAY_COUNT; // 30
    private static final int PLAYER_INV_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 36;

    private final ShipmentFillerBlockEntity blockEntity;
    private final ContainerData data;

    // Server-side constructor
    public ShipmentFillerMenu(int containerId, Inventory playerInventory, ShipmentFillerBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, blockEntity.getContainerData());
    }

    // Client-side constructor (from network)
    public ShipmentFillerMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData), new SimpleContainerData(ShipmentFillerBlockEntity.DATA_COUNT));
    }

    private ShipmentFillerMenu(int containerId, Inventory playerInventory, ShipmentFillerBlockEntity blockEntity, ContainerData data) {
        super(LostdepthsModMenus.SHIPMENT_FILLER_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = data;

        ItemStackHandler inv = blockEntity.getInventory();

        // Input slot (top-left)
        addSlot(new SlotItemHandler(inv, INPUT_SLOT, 8, 6));

        // Box slot (below input)
        addSlot(new SlotItemHandler(inv, BOX_SLOT, 8, 63));

        // Display slots: 7 columns x 4 rows, starting at (38, 6) with 19px spacing
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 7; col++) {
                int slotIndex = DISPLAY_START + row * 7 + col;
                int x = 38 + col * 19;
                int y = 6 + row * 19;
                addSlot(new SlotItemHandler(inv, slotIndex, x, y) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return false;
                    }

                    @Override
                    public boolean mayPickup(@NotNull Player player) {
                        return false;
                    }
                });
            }
        }

        // Player inventory (3 rows)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }

        addDataSlots(data);
    }

    private static ShipmentFillerBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof ShipmentFillerBlockEntity be) {
            return be;
        }
        throw new IllegalStateException("Block entity at " + pos + " is not a ShipmentFillerBlockEntity");
    }

    public ShipmentFillerBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getWeight() {
        return data.get(0);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack slotStack = slot.getItem();
        ItemStack original = slotStack.copy();

        if (index < MACHINE_SLOT_COUNT) {
            // From machine to player inventory
            if (index >= DISPLAY_START) return ItemStack.EMPTY; // can't shift-click display items
            if (!moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // From player to machine: try input slot first, then box slot
            if (!moveItemStackTo(slotStack, INPUT_SLOT, INPUT_SLOT + 1, false)) {
                if (!moveItemStackTo(slotStack, BOX_SLOT, BOX_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (slotStack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (slotStack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, slotStack);
        return original;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return blockEntity.getBlockPos().closerToCenterThan(player.position(), 8.0);
    }
}
