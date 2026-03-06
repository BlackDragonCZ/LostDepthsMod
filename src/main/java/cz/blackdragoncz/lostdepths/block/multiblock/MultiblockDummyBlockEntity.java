package cz.blackdragoncz.lostdepths.block.multiblock;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MultiblockDummyBlockEntity extends BlockEntity {

    private BlockPos controllerPos = BlockPos.ZERO;

    public MultiblockDummyBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.MULTIBLOCK_DUMMY.get(), pos, state);
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public void setControllerPos(BlockPos pos) {
        this.controllerPos = pos;
        setChanged();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CtrlX", controllerPos.getX());
        tag.putInt("CtrlY", controllerPos.getY());
        tag.putInt("CtrlZ", controllerPos.getZ());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        controllerPos = new BlockPos(tag.getInt("CtrlX"), tag.getInt("CtrlY"), tag.getInt("CtrlZ"));
    }
}
