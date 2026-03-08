package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.recipe.FusionTableRecipe;
import cz.blackdragoncz.lostdepths.world.inventory.FusionTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
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
import java.util.concurrent.ThreadLocalRandom;

public class FusionTableBlockEntity extends BlockEntity implements MenuProvider {

    public static final int BOARD_ROWS = 4;
    public static final int BOARD_COLUMNS = 6;
    public static final int BOARD_SIZE = BOARD_ROWS * BOARD_COLUMNS;
    public static final int SHAPE_COUNT = 5;
    public static final int REQUIRED_PROGRESS = 6;

    // DATA_COUNT: 24 board + 2 upcoming + 1 progress + 1 active flag = 28
    public static final int DATA_COUNT = 28;

    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 2) return false; // output slot
            return true;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            // Only allow extracting from output slot (2) and input slots (0, 1)
            return super.extractItem(slot, amount, simulate);
        }
    };

    private final LazyOptional<IItemHandler> inventoryCapability = LazyOptional.of(() -> inventory);

    private final int[] board = new int[BOARD_SIZE];
    private final int[] upcoming = new int[2]; // [0] = current, [1] = next
    private int progress = 0;
    private boolean puzzleActive = false;

    private final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int index) {
            if (index >= 0 && index < BOARD_SIZE) return board[index];
            if (index == 24) return upcoming[0];
            if (index == 25) return upcoming[1];
            if (index == 26) return progress;
            if (index == 27) return puzzleActive ? 1 : 0;
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index >= 0 && index < BOARD_SIZE) board[index] = value;
            else if (index == 24) upcoming[0] = value;
            else if (index == 25) upcoming[1] = value;
            else if (index == 26) progress = value;
            else if (index == 27) puzzleActive = value != 0;
        }

        @Override
        public int getCount() {
            return DATA_COUNT;
        }
    };

    public FusionTableBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.FUSION_TABLE.get(), pos, state);
    }

    public ContainerData getContainerData() {
        return containerData;
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    // --- Puzzle Logic (server-side) ---

    public static void serverTick(Level level, BlockPos pos, BlockState state, FusionTableBlockEntity be) {
        be.tick();
    }

    private void tick() {
        if (level == null || level.isClientSide) return;

        ItemStack input1 = inventory.getStackInSlot(0);
        ItemStack input2 = inventory.getStackInSlot(1);
        ItemStack output = inventory.getStackInSlot(2);

        // If puzzle completed (progress reached target), craft
        if (progress >= REQUIRED_PROGRESS && puzzleActive) {
            craftResult();
            return;
        }

        // Activate puzzle if: both inputs valid for recipe, output empty, puzzle not active
        if (!puzzleActive && !output.isEmpty()) {
            return; // Output occupied, stay inactive
        }

        if (!puzzleActive && !input1.isEmpty() && !input2.isEmpty() && output.isEmpty()) {
            FusionTableRecipe recipe = findRecipe(input1, input2);
            if (recipe != null) {
                activatePuzzle();
            }
        }

        // Deactivate if inputs become invalid while puzzle is active
        if (puzzleActive && progress < REQUIRED_PROGRESS) {
            if (input1.isEmpty() || input2.isEmpty()) {
                deactivatePuzzle();
                return;
            }
            FusionTableRecipe recipe = findRecipe(input1, input2);
            if (recipe == null) {
                deactivatePuzzle();
            }
        }
    }

    private void activatePuzzle() {
        puzzleActive = true;
        progress = 0;
        generateBoard();
        generateUpcoming();
        setChanged();
    }

    private void deactivatePuzzle() {
        puzzleActive = false;
        progress = 0;
        clearBoard();
        upcoming[0] = 0;
        upcoming[1] = 0;
        setChanged();
    }

    private void generateBoard() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        int attempts = 0;
        do {
            int filledCount = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                int shapeId = rand.nextInt(1, 13); // 1-12
                if (shapeId >= 1 && shapeId <= SHAPE_COUNT) {
                    board[i] = shapeId;
                    filledCount++;
                } else {
                    board[i] = 0; // empty cell
                }
            }
            // Ensure roughly half the board is pre-filled (weight check from original)
            // Original checked weight == -4, meaning filled - empty = -4
            // With 24 cells: filled - (24-filled) = -4 => filled = 10
            int emptyCount = BOARD_SIZE - filledCount;
            if (filledCount - emptyCount == -4) break;
            attempts++;
        } while (attempts < 100);

        // Ensure no 2x2 match exists at start
        if (has2x2Match()) {
            generateBoard(); // regenerate
        }
    }

    private void generateUpcoming() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        upcoming[0] = rand.nextInt(1, SHAPE_COUNT + 1);
        upcoming[1] = rand.nextInt(1, SHAPE_COUNT + 1);
    }

    private void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = 0;
        }
    }

    public boolean tryPlaceShape(int cellIndex) {
        if (!puzzleActive) return false;
        if (cellIndex < 0 || cellIndex >= BOARD_SIZE) return false;
        if (board[cellIndex] >= 1 && board[cellIndex] <= SHAPE_COUNT) return false; // cell already occupied

        // Place the current upcoming shape
        board[cellIndex] = upcoming[0];

        // Shift queue
        upcoming[0] = upcoming[1];
        upcoming[1] = ThreadLocalRandom.current().nextInt(1, SHAPE_COUNT + 1);

        // Check for 2x2 match
        if (has2x2Match()) {
            progress++;
            if (progress >= REQUIRED_PROGRESS) {
                // Will be handled in next tick
                setChanged();
                return true;
            }
            // Reset board and queue for next round
            generateBoard();
            generateUpcoming();
            setChanged();
            return true;
        }

        // Check if board is completely full (no empty cells)
        if (isBoardFull()) {
            // Board full without match — reset progress to 0
            progress = 0;
            generateBoard();
            generateUpcoming();
            setChanged();
            return true;
        }

        setChanged();
        return true;
    }

    private boolean has2x2Match() {
        for (int row = 0; row < BOARD_ROWS - 1; row++) {
            for (int col = 0; col < BOARD_COLUMNS - 1; col++) {
                int idx = row * BOARD_COLUMNS + col;
                int current = board[idx];
                if (current >= 1 && current <= SHAPE_COUNT) {
                    int right = board[idx + 1];
                    int below = board[idx + BOARD_COLUMNS];
                    int belowRight = board[idx + BOARD_COLUMNS + 1];
                    if (current == right && current == below && current == belowRight) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i] < 1 || board[i] > SHAPE_COUNT) {
                return false;
            }
        }
        return true;
    }

    private void craftResult() {
        ItemStack input1 = inventory.getStackInSlot(0);
        ItemStack input2 = inventory.getStackInSlot(1);
        ItemStack outputSlot = inventory.getStackInSlot(2);

        FusionTableRecipe recipe = findRecipe(input1, input2);
        if (recipe == null) {
            deactivatePuzzle();
            return;
        }

        ItemStack result = recipe.getOutput();
        if (outputSlot.isEmpty()) {
            inventory.setStackInSlot(2, result.copy());
        } else if (ItemStack.isSameItemSameTags(outputSlot, result)
                && outputSlot.getCount() + result.getCount() <= outputSlot.getMaxStackSize()) {
            outputSlot.grow(result.getCount());
        } else {
            // Can't place output, wait
            return;
        }

        input1.shrink(1);
        input2.shrink(1);

        // Deactivate puzzle after crafting
        puzzleActive = false;
        progress = 0;
        clearBoard();
        upcoming[0] = 0;
        upcoming[1] = 0;
        setChanged();
    }

    @Nullable
    public FusionTableRecipe findRecipe(ItemStack input1, ItemStack input2) {
        if (level == null) return null;
        List<FusionTableRecipe> recipes = LostDepthsModRecipeType.FUSION_TABLE.get().getRecipes(level);
        for (FusionTableRecipe recipe : recipes) {
            if ((recipe.getInput1().getItem() == input1.getItem() && recipe.getInput2().getItem() == input2.getItem())
                    || (recipe.getInput1().getItem() == input2.getItem() && recipe.getInput2().getItem() == input1.getItem())) {
                return recipe;
            }
        }
        return null;
    }

    // --- NBT ---

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Inventory", inventory.serializeNBT());
        tag.putInt("Progress", progress);
        tag.putBoolean("PuzzleActive", puzzleActive);
        for (int i = 0; i < BOARD_SIZE; i++) {
            tag.putInt("Board" + i, board[i]);
        }
        tag.putInt("Upcoming0", upcoming[0]);
        tag.putInt("Upcoming1", upcoming[1]);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        inventory.deserializeNBT(tag.getCompound("Inventory"));
        progress = tag.getInt("Progress");
        puzzleActive = tag.getBoolean("PuzzleActive");
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = tag.getInt("Board" + i);
        }
        upcoming[0] = tag.getInt("Upcoming0");
        upcoming[1] = tag.getInt("Upcoming1");
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
        return Component.translatable("block.lostdepths.fusion_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new FusionTableMenu(containerId, playerInventory, this);
    }
}
