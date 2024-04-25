package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class InfusedSignBlock extends StandingSignBlock {

    public InfusedSignBlock() {
        super(Properties.copy(Blocks.OAK_SIGN), WoodType.OAK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      //  return LostdepthsModBlockEntities.INFUSED_SIGN.get().create(pos, state);
        return null;
    }
}
