package cz.blackdragoncz.lostdepths.block.entity;

import com.ibm.icu.util.LocaleMatcher;
import cz.blackdragoncz.lostdepths.block.entity.base.BaseEnergyContainerBlockEntity;
import cz.blackdragoncz.lostdepths.energy.SyncedEnergyStorage;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
/*
public class ResourceExtractorBlockEntity extends BaseEnergyContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN);

    public static final ItemStack MAIN_SLOT = new ItemStack(LostdepthsModItems.FORGEFIRE_PICKAXE.get());
    public static final ItemStack BOOST_SLOT = new ItemStack(Items.ENCHANTED_BOOK);
    private static final ItemStack OUTPUT_SLOT = new ItemStack(LostdepthsModItems.RAW_FIRERITE.get());

    private static final int energy_max = 500000;
    private static final int energy_cost = 25000;
    private static final int max_extract = 800;
    private static final boolean valid_tool = false;
    private static final int processing_time = 1200;
    private static final int consume_tick_time = 2;

    private int ticker = 0;
    private int used_energy = 0;

    public ResourceExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.RESOURCE_EXTRACTOR.get(), pos, state);
    }

    public boolean updateTick() {
        ticker++;

        if (ticker == consume_tick_time) {
            ticker = 0;
            return true;
        }

        return false;
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.items);
    }

    @Override
    protected SyncedEnergyStorage createEnergyStorage() {
        return new SyncedEnergyStorage(this, energy_max, energy_cost, max_extract);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return false;
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 22;
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, ResourceExtractorBlockEntity blockEntity) {
        if (level.isClientSide()) {
            return;
        }

        if (blockEntity.updateTick()) {
            ItemStack mainSlot = blockEntity.getItems().get(0);
            ItemStack boostSlot = blockEntity.getItems().get(1);
            ItemStack outputSlot = blockEntity.getItems().get(2);

            if (!mainSlot.isEmpty() && blockEntity.energyStorage.receiveEnergy(energy_cost, true) == energy_cost) {
                blockEntity.energyStorage.receiveEnergy(energy_cost, false);
            }
        }

        List<BlockEntity> energyEntities = new ArrayList<>();

        energyEntities.removeIf((entity -> entity instanceof ResourceExtractorBlockEntity));

        if (energyEntities.isEmpty())
            return;

        int energyReceiversCount = energyEntities.size();
        int energyAllocated = blockEntity.energyStorage.extractEnergy(max_extract, true);
        int energyPerReceiver = energyAllocated / energyReceiversCount;
        int consumedEnergy = 0;



    }
}


 */