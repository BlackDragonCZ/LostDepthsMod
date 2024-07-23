package cz.blackdragoncz.lostdepths.block.plant;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;

import java.util.Collections;
import java.util.List;

public class PlantsBase extends FlowerBlock {
    public PlantsBase() {
        super(() -> MobEffects.DARKNESS, 5000, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).lightLevel(s -> 3)
                .noCollission().offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, BlockGetter world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, list, flag);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropOriginal = super.getDrops(state, builder);
        if (!dropOriginal.isEmpty())
            return dropOriginal;
        return Collections.singletonList(new ItemStack(this));
    }
}
