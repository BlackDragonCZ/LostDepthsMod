package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedHangingSignBlockEntity extends SignBlockEntity {
    public InfusedHangingSignBlockEntity(BlockPos pos, BlockState state) {
        super(LostdepthsModBlockEntities.INFUSED_HANGING_SIGN.get(), pos, state);
    }

    public int getTextLineHeight() {
        return 9;
    }

    public int getMaxTextLineWidth() {
        return 60;
    }
}
