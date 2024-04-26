package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.block.entity.InfusedHangingSignBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedWallHangingSignBlock extends WallHangingSignBlock {
    public InfusedWallHangingSignBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL), LostdepthsModWoodTypes.INFUSED_IRON);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InfusedHangingSignBlockEntity(pos, state);
    }
}
