package cz.blackdragoncz.lostdepths.block.decor;

import cz.blackdragoncz.lostdepths.init.LostdepthsModBlockEntities;
import cz.blackdragoncz.lostdepths.init.LostdepthsModWoodTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

public class InfusedSignBlock extends StandingSignBlock {

    public InfusedSignBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2).noCollission().explosionResistance(2), LostdepthsModWoodTypes.INFUSED_IRON);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return LostdepthsModBlockEntities.INFUSED_SIGN.get().create(pos, state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
