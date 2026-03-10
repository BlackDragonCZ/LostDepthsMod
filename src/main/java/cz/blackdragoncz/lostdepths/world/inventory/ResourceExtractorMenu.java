package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.entity.ResourceExtractorBlockEntity;
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
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ResourceExtractorMenu extends AbstractContainerMenu {

    private static final int MACHINE_SLOTS = 8;
    private final Player player;
    private final Level level;
    private ResourceExtractorBlockEntity blockEntity;
    private final ContainerLevelAccess access;
    private final ContainerData data;

    public ResourceExtractorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(LostdepthsModMenus.RESOURCE_EXTRACTOR_MENU.get(), id);
        this.player = inv.player;
        this.level = inv.player.level();
        BlockPos pos = extraData.readBlockPos();

        this.level.getBlockEntity(pos, LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get())
                .ifPresent(e -> this.blockEntity = e);

        this.access = ContainerLevelAccess.create(this.level, pos);

        // ContainerData sync: server uses block entity data, client gets SimpleContainerData
        if (this.blockEntity != null) {
            this.data = this.blockEntity.containerData;
        } else {
            this.data = new SimpleContainerData(3);
        }
        this.addDataSlots(this.data);

        IItemHandler[] itemHandler = new IItemHandler[1];
        if (this.blockEntity != null) {
            this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null)
                    .ifPresent(cap -> itemHandler[0] = cap);
        }

        if (itemHandler[0] != null) {
            // Pickaxe slot
            this.addSlot(new SlotItemHandler(itemHandler[0], 0, 27, 83)); // pickaxe
            // Solution slots
            this.addSlot(new SlotItemHandler(itemHandler[0], 1, 8, 56));  // solution_1
            this.addSlot(new SlotItemHandler(itemHandler[0], 2, 26, 56)); // solution_2
            this.addSlot(new SlotItemHandler(itemHandler[0], 3, 44, 56)); // solution_3
            // Output slots (2x2)
            this.addSlot(new SlotItemHandler(itemHandler[0], 4, 85, 28));  // output_1
            this.addSlot(new SlotItemHandler(itemHandler[0], 5, 103, 28)); // output_2
            this.addSlot(new SlotItemHandler(itemHandler[0], 6, 85, 46));  // output_3
            this.addSlot(new SlotItemHandler(itemHandler[0], 7, 103, 46)); // output_4
        }

        // Player inventory (y=127)
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                this.addSlot(new Slot(inv, col + (row + 1) * 9, 9 + col * 18, 127 + row * 18));
        // Hotbar (y=185)
        for (int col = 0; col < 9; col++)
            this.addSlot(new Slot(inv, col, 9 + col * 18, 185));
    }

    public int getMachineStatus() { return data.get(0); }
    public int getProgress() { return data.get(1); }
    public int getMaxProgress() { return data.get(2); }

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

    public ResourceExtractorBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
