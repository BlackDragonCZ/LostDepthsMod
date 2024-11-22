package cz.blackdragoncz.lostdepths.block.machine;

import cz.blackdragoncz.lostdepths.block.base.BaseFacingBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class WormholeDisruptorBlock extends BaseFacingBlock {

    public WormholeDisruptorBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(4f, 16f)
                .pushReaction(PushReaction.BLOCK)
        );
    }

    //@OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {
        if (event.getMessage().contains(Component.literal("/home")))
        {
            event.setCanceled(true);
        }
    }

    @NotNull
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> dropsOriginal = super.getDrops(state, builder);
        if (!dropsOriginal.isEmpty())
            return dropsOriginal;
        return Collections.singletonList(new ItemStack(this, 1));
    }
}
