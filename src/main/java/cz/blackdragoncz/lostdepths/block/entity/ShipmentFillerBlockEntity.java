package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.item.ShipmentBoxItem;
import cz.blackdragoncz.lostdepths.recipe.ShipmentFillerRecipe;
import cz.blackdragoncz.lostdepths.world.inventory.ShipmentFillerMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ShipmentFillerBlockEntity extends BlockEntity implements MenuProvider {

    public static final int INPUT_SLOT = 0;
    public static final int BOX_SLOT = 1;
    public static final int DISPLAY_START = 2;
    public static final int DISPLAY_COUNT = 28; // 7x4 grid
    public static final int TOTAL_SLOTS = DISPLAY_START + DISPLAY_COUNT; // 30

    // ContainerData: index 0 = weight
    public static final int DATA_COUNT = 1;

    private final ItemStackHandler inventory = new ItemStackHandler(TOTAL_SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot == BOX_SLOT) {
                refreshDisplaySlots();
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT) {
                return isValidComponent(stack);
            }
            if (slot == BOX_SLOT) {
                return stack.getItem() == LostdepthsModItems.SHIPMENT_BOX.get();
            }
            return false; // display slots are read-only
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot >= DISPLAY_START) return ItemStack.EMPTY; // can't extract from display
            return super.extractItem(slot, amount, simulate);
        }
    };

    private final LazyOptional<IItemHandler> inventoryCapability = LazyOptional.of(() -> inventory);

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == 0) {
                ItemStack box = inventory.getStackInSlot(BOX_SLOT);
                if (!box.isEmpty() && box.getItem() instanceof ShipmentBoxItem) {
                    return ShipmentBoxItem.getWeight(box);
                }
                return 0;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            // read-only from server
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public ShipmentFillerBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.SHIPMENT_FILLER.get(), pos, state);
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    // --- Tick Logic ---

    public static void serverTick(Level level, BlockPos pos, BlockState state, ShipmentFillerBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        ItemStack input = inventory.getStackInSlot(INPUT_SLOT);
        ItemStack box = inventory.getStackInSlot(BOX_SLOT);

        if (input.isEmpty() || box.isEmpty()) return;
        if (!(box.getItem() instanceof ShipmentBoxItem)) return;

        ShipmentFillerRecipe recipe = findRecipe(input);
        if (recipe == null) return;

        int weight = recipe.getWeight();
        if (!ShipmentBoxItem.canAddWeight(box, weight)) return;

        // Consume input
        input.shrink(1);

        // Update box NBT
        int currentWeight = ShipmentBoxItem.getWeight(box);
        ShipmentBoxItem.setWeight(box, currentWeight + weight);

        String itemId = BuiltInRegistries.ITEM.getKey(recipe.getItem().getItem()).toString();
        ShipmentBoxItem.addStoredItem(box, itemId, weight);

        // Refresh display
        refreshDisplaySlots();
        setChanged();
    }

    @Nullable
    public ShipmentFillerRecipe findRecipe(ItemStack input) {
        if (level == null) return null;
        List<ShipmentFillerRecipe> recipes = LostDepthsModRecipeType.SHIPMENT_FILLER.get().getRecipes(level);
        for (ShipmentFillerRecipe recipe : recipes) {
            if (recipe.getItem().getItem() == input.getItem()) {
                return recipe;
            }
        }
        return null;
    }

    public boolean isValidComponent(ItemStack stack) {
        if (level == null) return true; // allow on client during menu construction
        return findRecipe(stack) != null;
    }

    public void refreshDisplaySlots() {
        // Clear all display slots
        for (int i = DISPLAY_START; i < TOTAL_SLOTS; i++) {
            inventory.setStackInSlot(i, ItemStack.EMPTY);
        }

        ItemStack box = inventory.getStackInSlot(BOX_SLOT);
        if (box.isEmpty() || !(box.getItem() instanceof ShipmentBoxItem)) return;

        ListTag storedItems = ShipmentBoxItem.getStoredItems(box);
        int displayIndex = DISPLAY_START;
        for (int i = 0; i < storedItems.size() && displayIndex < TOTAL_SLOTS; i++) {
            CompoundTag entry = storedItems.getCompound(i);
            String itemId = entry.getString("Item");
            ResourceLocation rl = ResourceLocation.tryParse(itemId);
            if (rl != null && BuiltInRegistries.ITEM.containsKey(rl)) {
                ItemStack displayStack = new ItemStack(BuiltInRegistries.ITEM.get(rl));
                inventory.setStackInSlot(displayIndex, displayStack);
                displayIndex++;
            }
        }
    }

    // --- NBT ---

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        // Only save input and box slots (display is derived from box NBT)
        CompoundTag invTag = new CompoundTag();
        invTag.put("Input", inventory.getStackInSlot(INPUT_SLOT).serializeNBT());
        invTag.put("Box", inventory.getStackInSlot(BOX_SLOT).serializeNBT());
        tag.put("Inventory", invTag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag invTag = tag.getCompound("Inventory");
        inventory.setStackInSlot(INPUT_SLOT, ItemStack.of(invTag.getCompound("Input")));
        inventory.setStackInSlot(BOX_SLOT, ItemStack.of(invTag.getCompound("Box")));
        refreshDisplaySlots();
    }

    // --- Capabilities ---

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return inventoryCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryCapability.invalidate();
    }

    // --- Menu ---

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.lostdepths.shipment_filler_block");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ShipmentFillerMenu(containerId, playerInventory, this);
    }
}
