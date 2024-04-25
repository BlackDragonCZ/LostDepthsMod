package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InfusedSignBlock extends StandingSignBlock {

    public InfusedSignBlock() {
        super(Properties.copy(Blocks.OAK_SIGN), LostdepthsModWoodTypes.INFUSED_IRON);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return LostdepthsModBlockEntities.INFUSED_SIGN.get().create(pos, state);
    }
}
