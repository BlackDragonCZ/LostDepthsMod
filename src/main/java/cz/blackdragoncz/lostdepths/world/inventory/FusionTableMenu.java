package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.FusionTableBlockEntity;
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

public class FusionTableMenu extends AbstractContainerMenu {

    private static final int INPUT_SLOT_1 = 0;
    private static final int INPUT_SLOT_2 = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int MACHINE_SLOT_COUNT = 3;
    private static final int PLAYER_INV_START = MACHINE_SLOT_COUNT;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 36;

    private final FusionTableBlockEntity blockEntity;
    private final ContainerData data;

    // Server-side constructor
    public FusionTableMenu(int containerId, Inventory playerInventory, FusionTableBlockEntity blockEntity) {
        this(containerId, playerInventory, blockEntity, blockEntity.getContainerData());
    }

    // Client-side constructor (from network)
    public FusionTableMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, extraData), new SimpleContainerData(FusionTableBlockEntity.DATA_COUNT));
    }

    private FusionTableMenu(int containerId, Inventory playerInventory, FusionTableBlockEntity blockEntity, ContainerData data) {
        super(LostdepthsModMenus.FUSION_TABLE_MENU.get(), containerId);
        this.blockEntity = blockEntity;
        this.data = data;

        ItemStackHandler inv = blockEntity.getInventory();

        // Input slots (from old container: slot 0 at x=19,y=10 and slot 1 at x=19,y=30)
        addSlot(new SlotItemHandler(inv, INPUT_SLOT_1, 19, 10));
        addSlot(new SlotItemHandler(inv, INPUT_SLOT_2, 19, 30));

        // Output slot (x=142, y=15) — no insertion allowed
        addSlot(new SlotItemHandler(inv, OUTPUT_SLOT, 142, 15) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });

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

    private static FusionTableBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        if (playerInventory.player.level().getBlockEntity(pos) instanceof FusionTableBlockEntity be) {
            return be;
        }
        throw new IllegalStateException("Block entity at " + pos + " is not a FusionTableBlockEntity");
    }

    public FusionTableBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getBoardCell(int index) {
        return data.get(index);
    }

    public int getUpcoming(int index) {
        return data.get(24 + index);
    }

    public int getProgress() {
        return data.get(26);
    }

    public boolean isPuzzleActive() {
        return data.get(27) != 0;
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
            if (!moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_INV_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            // From player to machine input slots only
            if (!moveItemStackTo(slotStack, INPUT_SLOT_1, INPUT_SLOT_2 + 1, false)) {
                return ItemStack.EMPTY;
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
