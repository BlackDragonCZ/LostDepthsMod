package cz.blackdragoncz.lostdepths.disruptor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import cz.blackdragoncz.lostdepths.block.creative.entity.GravatorDisruptorBlockEntity;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity;
import cz.blackdragoncz.lostdepths.block.creative.entity.RealityDisruptorBlockEntity.Camera;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;

// Per block, targeted by position, so an RP puzzle can give every disruptor its own behaviour.
public final class DisruptorCommands {

    // The 24 post chains that used to sit behind Super Secret Settings; all still ship in the client jar.
    private static final List<String> SHADERS = List.of("none", "notch", "fxaa", "art", "bumpy", "blobs2", "pencil",
            "color_convolve", "deconverge", "flip", "invert", "ntsc", "outline", "phosphor", "scan_pincushion",
            "sobel", "bits", "desaturate", "green", "blur", "wobble", "blobs", "antialias", "creeper", "spider");

    private static final List<String> CAMERAS = List.of("free", "first", "third_back", "third_front");
    private static final List<String> MODES = List.of("normal", "strict");
    private static final List<String> DEBUGS = List.of("normal", "fixed", "obfuscated");

    private DisruptorCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(reality());
        dispatcher.register(gravator());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> reality() {
        return Commands.literal("reality-disruptor")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.literal("range")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1, 256))
                                        .executes(ctx -> applyReality(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), (be, s) -> {
                                            be.setRange(IntegerArgumentType.getInteger(ctx, "value"));
                                            return "range " + be.getRange();
                                        }))))
                        .then(Commands.literal("gui")
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                        .executes(ctx -> applyReality(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), (be, s) -> {
                                            be.setHideGui(BoolArgumentType.getBool(ctx, "value"));
                                            return "gui hidden = " + be.isHideGui();
                                        }))))
                        .then(Commands.literal("debug")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .suggests((c, b) -> SharedSuggestionProvider.suggest(DEBUGS, b))
                                        .executes(ctx -> applyReality(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), (be, s) -> {
                                            String value = StringArgumentType.getString(ctx, "value").toLowerCase(Locale.ROOT);
                                            if (!DEBUGS.contains(value))
                                                return null;
                                            be.setDebug(RealityDisruptorBlockEntity.Debug.byName(value));
                                            return "debug " + value;
                                        }))))
                        .then(Commands.literal("camera")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .suggests((c, b) -> SharedSuggestionProvider.suggest(CAMERAS, b))
                                        .executes(ctx -> applyReality(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), (be, s) -> {
                                            be.setCamera(Camera.byName(StringArgumentType.getString(ctx, "value").toLowerCase(Locale.ROOT)));
                                            return "camera " + be.getCamera().name().toLowerCase(Locale.ROOT);
                                        }))))
                        .then(Commands.literal("shader")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .suggests((c, b) -> SharedSuggestionProvider.suggest(SHADERS, b))
                                        .executes(ctx -> applyReality(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), (be, s) -> {
                                            String value = StringArgumentType.getString(ctx, "value").toLowerCase(Locale.ROOT);
                                            if (!SHADERS.contains(value))
                                                return null;
                                            be.setShader("none".equals(value) ? "" : value);
                                            return "shader " + value;
                                        })))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> gravator() {
        return Commands.literal("gravator-disruptor")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.literal("range")
                                .then(Commands.argument("value", IntegerArgumentType.integer(1, 256))
                                        .executes(ctx -> applyGravator(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), be -> {
                                            be.setRange(IntegerArgumentType.getInteger(ctx, "value"));
                                            return "range " + be.getRange();
                                        }))))
                        .then(Commands.literal("mode")
                                .then(Commands.argument("value", StringArgumentType.word())
                                        .suggests((c, b) -> SharedSuggestionProvider.suggest(MODES, b))
                                        .executes(ctx -> applyGravator(ctx.getSource(), BlockPosArgument.getLoadedBlockPos(ctx, "pos"), be -> {
                                            be.setMode(GravatorDisruptorBlockEntity.Mode.byName(StringArgumentType.getString(ctx, "value").toLowerCase(Locale.ROOT)));
                                            return "mode " + be.getMode().name().toLowerCase(Locale.ROOT);
                                        })))));
    }

    private static int applyReality(CommandSourceStack source, BlockPos pos, BiFunction<RealityDisruptorBlockEntity, Void, String> action) {
        ServerLevel level = source.getLevel();
        if (!(level.getBlockEntity(pos) instanceof RealityDisruptorBlockEntity be)) {
            source.sendFailure(Component.literal("No Reality Disruptor at " + pos.toShortString()));
            return 0;
        }
        String result = action.apply(be, null);
        if (result == null) {
            source.sendFailure(Component.literal("Unknown value"));
            return 0;
        }
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        source.sendSuccess(() -> Component.literal("Reality Disruptor at " + pos.toShortString() + ": " + result), true);
        return 1;
    }

    private static int applyGravator(CommandSourceStack source, BlockPos pos, java.util.function.Function<GravatorDisruptorBlockEntity, String> action) {
        ServerLevel level = source.getLevel();
        if (!(level.getBlockEntity(pos) instanceof GravatorDisruptorBlockEntity be)) {
            source.sendFailure(Component.literal("No Gravator Disruptor at " + pos.toShortString()));
            return 0;
        }
        String result = action.apply(be);
        level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
        source.sendSuccess(() -> Component.literal("Gravator Disruptor at " + pos.toShortString() + ": " + result), true);
        return 1;
    }
}
