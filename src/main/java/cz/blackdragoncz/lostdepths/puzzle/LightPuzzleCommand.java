package cz.blackdragoncz.lostdepths.puzzle;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import cz.blackdragoncz.lostdepths.block.entity.LightPuzzleControllerBlockEntity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class LightPuzzleCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal("ld_lightpuzzle")
				.requires(source -> source.hasPermission(2))
				.then(Commands.argument("controller", BlockPosArgument.blockPos())
						.then(Commands.argument("corner1", BlockPosArgument.blockPos())
								.then(Commands.argument("corner2", BlockPosArgument.blockPos())
										// Without maxLit — uses default (-1 = auto)
										.executes(ctx -> execute(ctx.getSource(),
												BlockPosArgument.getLoadedBlockPos(ctx, "controller"),
												BlockPosArgument.getLoadedBlockPos(ctx, "corner1"),
												BlockPosArgument.getLoadedBlockPos(ctx, "corner2"),
												-1))
										// With maxLit
										.then(Commands.argument("maxLit", IntegerArgumentType.integer(0))
												.executes(ctx -> execute(ctx.getSource(),
														BlockPosArgument.getLoadedBlockPos(ctx, "controller"),
														BlockPosArgument.getLoadedBlockPos(ctx, "corner1"),
														BlockPosArgument.getLoadedBlockPos(ctx, "corner2"),
														IntegerArgumentType.getInteger(ctx, "maxLit")))
										)
								)
						)
				)
		);
	}

	private static int execute(CommandSourceStack source, BlockPos controllerPos, BlockPos corner1, BlockPos corner2, int maxLit) {
		ServerLevel level = source.getLevel();

		if (!(level.getBlockEntity(controllerPos) instanceof LightPuzzleControllerBlockEntity controller)) {
			source.sendFailure(Component.literal("No Light Puzzle Controller at " + controllerPos.toShortString()));
			return 0;
		}

		// Validate plane size (must be at least 3x3)
		int width = Math.abs(corner2.getX() - corner1.getX()) + 1;
		int depth = Math.abs(corner2.getZ() - corner1.getZ()) + 1;

		if (width < 3 || depth < 3) {
			source.sendFailure(Component.literal("Puzzle plane too small! Minimum is 3x3. Current: " + width + "x" + depth));
			return 0;
		}

		// Validate corners are on the same Y level (puzzle is a flat plane)
		if (corner1.getY() != corner2.getY()) {
			source.sendFailure(Component.literal("Puzzle corners must be on the same Y level! Got Y=" + corner1.getY() + " and Y=" + corner2.getY()));
			return 0;
		}

		controller.configure(corner1, corner2, maxLit);

		// Sync to client
		BlockPos cp = controllerPos;
		level.sendBlockUpdated(cp, level.getBlockState(cp), level.getBlockState(cp), 3);

		int totalBlocks = width * depth;
		String maxLitStr = maxLit >= 0 ? String.valueOf(maxLit) : "auto";

		source.sendSuccess(() -> Component.literal(
				"Light Puzzle configured at " + controllerPos.toShortString() +
						"\n  Plane: " + corner1.toShortString() + " to " + corner2.toShortString() +
						" (" + width + "x" + depth + " = " + totalBlocks + " blocks)" +
						"\n  Max lit blocks: " + maxLitStr
		), true);

		return 1;
	}
}
