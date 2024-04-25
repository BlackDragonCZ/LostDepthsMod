package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.block.entity.InfusedSignBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedWallSignBlock extends WallSignBlock {
    public InfusedWallSignBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL), LostdepthsModWoodTypes.INFUSED_IRON);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusedSignBlockEntity(pos, state);
    }
}
