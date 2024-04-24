package cz.blackdragoncz.lostdepths.block.entity;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedSignBlockEntity extends SignBlockEntity {

    public InfusedSignBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    @Override
    public BlockEntityType<?> getType() {
        return LostdepthsModBlockEntities.INFUSED_SIGN.get();
    }
}
