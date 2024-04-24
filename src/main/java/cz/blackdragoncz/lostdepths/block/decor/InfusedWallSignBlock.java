package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.block.entity.InfusedSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class InfusedWallSignBlock extends WallSignBlock {
    public InfusedWallSignBlock(Properties properties, WoodType woodType) {
        super(properties, woodType);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusedSignBlockEntity(pos, state);
    }
}
