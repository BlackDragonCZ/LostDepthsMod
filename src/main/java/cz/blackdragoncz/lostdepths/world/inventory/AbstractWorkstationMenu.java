package cz.blackdragoncz.lostdepths.world.inventory;

import cz.blackdragoncz.lostdepths.util.IEnergyAccessor;
import cz.blackdragoncz.lostdepths.client.gui.ContainerWrapper;
import cz.blackdragoncz.lostdepths.recipe.LDShapedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractWorkstationMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>>  {

    protected final Player player;
    protected final Level level;
    protected final ContainerLevelAccess access;
    protected final BlockEntity blockEntity;
    protected CraftingContainer craftingContainer;
    protected ContainerWrapper containerWrapper;
    protected final int slotCount;
    protected final RecipeType<LDShapedRecipe> recipeType;
    protected final Map<Integer, Slot> customSlots = new HashMap<>();
    protected final ResultContainer resultContainer = new ResultContainer();
    protected final IEnergyAccessor energyAccessor;
    protected final int requiredEnergyToCraft;

    protected AbstractWorkstationMenu(@Nullable MenuType<?> menuType, int slotCount, RecipeType<LDShapedRecipe> recipeType, int id, Inventory inv, FriendlyByteBuf extraData, int requiredEnergyToCraft) {
        super(menuType, id);
        this.player = inv.player;
        this.requiredEnergyToCraft = requiredEnergyToCraft;
        this.level = this.player.level();
        this.slotCount = slotCount;
        this.recipeType = recipeType;

        BlockPos pos = extraData.readBlockPos();
        this.access = ContainerLevelAccess.create(this.level, pos);
        this.blockEntity = this.level.getBlockEntity(pos);

        if (this.blockEntity instanceof CraftingContainer) {
            this.craftingContainer = (CraftingContainer) this.blockEntity;
        }

        if (this.blockEntity instanceof IEnergyAccessor ea) {
            this.energyAccessor = ea;
        }
        else {
            this.energyAccessor = null;
        }

        this.containerWrapper = new ContainerWrapper(this.craftingContainer) {
            @Override
            public void setChanged() {
                super.setChanged();
                AbstractWorkstationMenu.this.slotsChanged(this);
            }
        };
    }

    public abstract CustomResultSlot<LDShapedRecipe> getResultSlot();

    @Override
    public void slotsChanged(Container container) {
        this.access.execute(((level, blockPos) -> {
            if (!level.isClientSide) {
                ServerPlayer serverplayer = (ServerPlayer)this.player;

                Optional<LDShapedRecipe> foundRecipe = level.getRecipeManager().getRecipeFor(this.recipeType, this.craftingContainer, level);

                ItemStack stack = ItemStack.EMPTY;

                if (foundRecipe.isPresent()) {
                    ItemStack resultItem = foundRecipe.get().assemble(this.craftingContainer, level.registryAccess());

                    if (resultItem.isItemEnabled(level.enabledFeatures())) {
                        stack = resultItem;
                    }
                }

                resultContainer.setItem(0, stack);
                setRemoteSlot(0, stack);

                serverplayer.connection.send(new ClientboundContainerSetSlotPacket(containerId, incrementStateId(), 0, stack));
            }
        }));
    }

    public boolean hasFoundRecipe() {
        return getResultSlot().hasItem();
    }

    @Override
    public Map<Integer, Slot> get() {
        return customSlots;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.slotCount - 1) {
                if (!this.moveItemStackTo(itemstack1, this.slotCount - 1, this.slots.size(), true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (!this.moveItemStackTo(itemstack1, 0, this.slotCount - 1, false)) {
                if (index < this.slotCount - 1 + 27) {
                    if (!this.moveItemStackTo(itemstack1, this.slotCount - 1 + 27, this.slots.size(), true))
                        return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(itemstack1, this.slotCount - 1, this.slotCount - 1 + 27, false))
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
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }
        if (stack.isStackable()) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }
                Slot slot = this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                if (slot.mayPlace(itemstack) && !itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    int maxSize = Math.min(slot.getMaxStackSize(), stack.getMaxStackSize());
                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.set(itemstack);
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot.set(itemstack);
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }
            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }
                Slot slot1 = this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();
                if (itemstack1.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }
                if (reverseDirection) {
                    --i;
                } else {
                    ++i;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean stillValid(Player player) {
        return AbstractContainerMenu.stillValid(this.access, player, this.blockEntity.getBlockState().getBlock());
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public IEnergyAccessor getEnergyAccessor() {
        return energyAccessor;
    }

    public int getRequiredEnergyToCraft() {
        return requiredEnergyToCraft;
    }
}
