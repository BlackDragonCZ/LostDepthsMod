package cz.blackdragoncz.lostdepths.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import cz.blackdragoncz.lostdepths.block.creative.entity.WormholeDisruptorBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class WormholeRangeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("wormhole-range")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("range", IntegerArgumentType.integer(1, 256))
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(ctx -> {
                                    int range = IntegerArgumentType.getInteger(ctx, "range");
                                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(ctx, "pos");
                                    ServerLevel level = ctx.getSource().getLevel();

                                    if (level.getBlockEntity(pos) instanceof WormholeDisruptorBlockEntity disruptor) {
                                        disruptor.setRange(range);
                                        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
                                        ctx.getSource().sendSuccess(() ->
                                                Component.literal("Wormhole Disruptor at " + pos.toShortString() + " range set to " + range), true);
                                        return 1;
                                    } else {
                                        ctx.getSource().sendFailure(Component.literal("No Wormhole Disruptor at " + pos.toShortString()));
                                        return 0;
                                    }
                                })
                        )
                )
        );
    }
}
