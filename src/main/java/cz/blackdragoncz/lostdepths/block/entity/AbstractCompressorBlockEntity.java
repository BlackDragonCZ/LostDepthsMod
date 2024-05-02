package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import cz.blackdragoncz.lostdepths.recipe.CompressingRecipe;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.world.inventory.CompressorGUIMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractCompressorBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {

    private NonNullList<ItemStack> stacks = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    private final List<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> recipeTypes;
    private final int craftTickTime;
    private final int requiredStackSize;
    private final int energyCost;

    private CompressingRecipe currentRecipe;
    private int currentCraftTime = 0;
    private Item lastUsedRecipeItem;
    private boolean canProcess = false;
    public boolean tickRecipeCheck = true;
    private EnergyStorage energyStorage;

    protected AbstractCompressorBlockEntity(List<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> recipeTypes, int craftTickTime, int requiredStackSize, int energyStorageCapacity, int energyCost, int maxEnergyTransfer, BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
        this.recipeTypes = recipeTypes;
        this.requiredStackSize = requiredStackSize;
        this.craftTickTime = craftTickTime;
        this.energyCost = energyCost;

        if (energyStorageCapacity > 0 && energyCost > 0) {
            this.energyStorage = new SyncedEnergyStorage(this, energyStorageCapacity, maxEnergyTransfer);
        }
    }

    protected AbstractCompressorBlockEntity(List<LostDepthsModRecipeType<CraftingContainer, CompressingRecipe>> recipeTypes, int craftTickTime, int requiredStackSize, BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        this(recipeTypes, craftTickTime, requiredStackSize, 0, 0, 0, blockEntityType, blockPos, blockState);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        if (!this.tryLoadLootTable(compound))
            this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);

        if (compound.contains("energyStorage") && energyStorage != null)
            energyStorage.deserializeNBT(compound.get("energyStorage"));

        this.currentCraftTime = compound.getInt("currentTickTime");
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) {
            ContainerHelper.saveAllItems(compound, this.stacks);
        }

        if (energyStorage != null)
            compound.put("energyStorage", energyStorage.serializeNBT());

        compound.putInt("currentTickTime", this.currentCraftTime);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.stacks)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return index != 1 && findRecipe(stack) != null;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return IntStream.range(0, this.getContainerSize()).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index != 0;
    }

    @Override
    protected @NotNull NonNullList<ItemStack> getItems() {
        return stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        stacks = list;
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory) {
        return new CompressorGUIMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getContainerSize() {
        return stacks.size();
    }

    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @javax.annotation.Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
            return handlers[facing.ordinal()].cast();

        if (energyStorage != null && !this.remove && capability == ForgeCapabilities.ENERGY)
            return LazyOptional.of(() -> energyStorage).cast();

        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        for (LazyOptional<? extends IItemHandler> handler : handlers)
            handler.invalidate();
    }

    public CompressingRecipe findRecipe(ItemStack stack) {
        for (LostDepthsModRecipeType<CraftingContainer, CompressingRecipe> recipeType : this.recipeTypes) {
            List<CompressingRecipe> recipes = this.level.getRecipeManager().getAllRecipesFor(recipeType);

            for (CompressingRecipe recipe : recipes) {
                if (recipe.getInput().getItem() == stack.getItem()) {
                    return recipe;
                }
            }
        }
        return null;
    }


    private void tryInitializeRecipe(ItemStack stack) {
        if (stack.getCount() < requiredStackSize) {
            canProcess = false;
            return;
        }

        if (currentRecipe == null || stack.getItem() != lastUsedRecipeItem) {
            currentRecipe = findRecipe(stack);
            lastUsedRecipeItem = stack.getItem();
            currentCraftTime = 0;
        }

        if (currentRecipe != null) {
            canProcess = true;
        }
    }



    @Override
    public void setItem(int index, ItemStack stack) {
        super.setItem(index, stack);

        if (index == 0) {
            tryInitializeRecipe(stack);
        }
    }

    public int getCraftTickTime() {
        return craftTickTime;
    }

    public int getCurrentCraftTime() {
        return currentCraftTime;
    }

    public void setCurrentCraftTime(int currentCraftTime) {
        this.currentCraftTime = currentCraftTime;
    }

    public void incrementCurrentCraftTime() {
        this.currentCraftTime++;
    }

    public int getRequiredStackSize() {
        return requiredStackSize;
    }

    public CompressingRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public void markForRecipeCheck() {
        tickRecipeCheck = true;
    }

    private boolean canFitOutput() {
        ItemStack stack = getItem(1);
        return stack.getCount() + this.requiredStackSize <= stack.getMaxStackSize();
    }

    private boolean checkCompatOutput() {
        ItemStack outputStack = getItem(1);

        if (outputStack.isEmpty())
            return true;

        return currentRecipe.getOutput().getItem() == outputStack.getItem();
    }

    public boolean haveEnergy() {
        return energyStorage == null || energyStorage.getEnergyStored() >= this.energyCost;
    }

    public boolean canProcess() {
        return canProcess && canFitOutput() && checkCompatOutput();
    }

    private void playFinishSound(Level level, BlockPos blockPos) {
        level.playSound(null, blockPos, LostdepthsModSounds.MANUFACTURE_MACHINE.get(), SoundSource.BLOCKS, 0.5f, 1);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, AbstractCompressorBlockEntity blockEntity) {
        if (blockEntity.tickRecipeCheck) {
            blockEntity.tickRecipeCheck = false;
            blockEntity.tryInitializeRecipe(blockEntity.getItem(0));
        }

        boolean changed = false;
        CompressingRecipe recipe = blockEntity.getCurrentRecipe();
        if (recipe != null && blockEntity.canProcess()) {

            if (blockEntity.haveEnergy()) {
                blockEntity.incrementCurrentCraftTime();

                EnergyStorage energyStorage = blockEntity.getEnergyStorage();
                if (energyStorage != null) {
                    energyStorage.extractEnergy(blockEntity.getEnergyCost(), false);
                }
            }

            if (blockEntity.getCurrentCraftTime() >= blockEntity.getCraftTickTime()) {
                blockEntity.setCurrentCraftTime(0);
                blockEntity.playFinishSound(level, blockPos);

                // Shrink input
                {
                    ItemStack itemStack = blockEntity.getItem(0).copy();
                    itemStack.shrink(blockEntity.getRequiredStackSize());
                    blockEntity.setItem(0, itemStack);
                }

                // Add output
                {
                    ItemStack itemStack = new ItemStack(recipe.getOutput().getItem(), blockEntity.getItem(1).getCount() + 1);
                    blockEntity.setItem(1, itemStack);
                }
            }

            changed = true;
        } else if (blockEntity.getCurrentCraftTime() != 0) {
            blockEntity.setCurrentCraftTime(0);
            changed = true;
        }

        if (changed) {
            level.sendBlockUpdated(blockPos, blockState, blockState, 3);
            //setChanged(level, blockPos, blockState); // <- Does not work idk why
        }
    }
}
