package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.block.power.entity.NurostarGeneratorBlockEntity;
import cz.blackdragoncz.lostdepths.client.gui.NurostarGeneratorScreen;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class NurostarGeneratorMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {

    private static final int SLOT_COUNT = 2;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private final Player player;
    private final Level level;
    private NurostarGeneratorBlockEntity blockEntity;
    private IItemHandler itemHandler;
    private final ContainerLevelAccess access;

    public NurostarGeneratorMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(LostdepthsModMenus.NUROSTAR_GENERATOR_MENU.get(), id);
        this.player = inv.player;
        this.level = inv.player.level();
        BlockPos pos = extraData.readBlockPos();

        this.level.getBlockEntity(pos, LostdepthsModBlockEntities.NUROSTAR_GENRATOR.get()).ifPresent(entity -> {
            this.blockEntity = entity;
        });

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
            this.itemHandler = capability;
        });

        this.access = ContainerLevelAccess.create(this.level, pos);

        int leftSideWidth = 176 / 2;
        int twoSlotsWidth = (NurostarGeneratorScreen.SLOT_SIZE * 2) + 5;
        int twoSlotsY = 85;

        // Waste slot
        this.customSlots.put(0, addSlot(new SlotItemHandler(itemHandler, 0, leftSideWidth / 2 - twoSlotsWidth / 2 + 1, twoSlotsY + 1)));
        this.customSlots.put(1, addSlot(new SlotItemHandler(itemHandler, 1, leftSideWidth / 2 - twoSlotsWidth / 2 + NurostarGeneratorScreen.SLOT_SIZE + 5 + 1, twoSlotsY + 1)));
        this.customSlots.put(2, addSlot(new SlotItemHandler(itemHandler, 2, leftSideWidth / 2 - NurostarGeneratorScreen.SLOT_SIZE / 2 + 1, 21)));

        for (int si = 0; si < 3; ++si)
            for (int sj = 0; sj < 9; ++sj)
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 42 + 84 + si * 18));
        for (int si = 0; si < 9; ++si)
            this.addSlot(new Slot(inv, si, 8 + si * 18, 42 + 142));
    }

    @Override
    public Map<Integer, Slot> get() {
        return customSlots;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < SLOT_COUNT - 1) {
                if (!this.moveItemStackTo(itemstack1, SLOT_COUNT - 1, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, SLOT_COUNT - 1, false)) {
                if (index < SLOT_COUNT - 1 + 27) {
                    if (!this.moveItemStackTo(itemstack1, SLOT_COUNT - 1 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemstack1, SLOT_COUNT - 1, SLOT_COUNT - 1 + 27, false))
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
            slot.onTake(player, itemstack1);
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return AbstractContainerMenu.stillValid(this.access, player, this.blockEntity.getBlockState().getBlock());
    }

    public NurostarGeneratorBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
