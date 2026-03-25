package cz.blackdragoncz.lostdepths.network.storage;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.block.entity.storage.NTPatternEncoderBlockEntity;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.item.storage.BlankPatternItem;
import cz.blackdragoncz.lostdepths.item.storage.EncodedPatternItem;
import cz.blackdragoncz.lostdepths.world.inventory.NTPatternEncoderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Client → Server: Encode a pattern from the Pattern Encoder's current grid contents.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class NTPatternEncodePacket {

	public NTPatternEncodePacket() {}

	public NTPatternEncodePacket(FriendlyByteBuf buf) {}

	public static void encode(NTPatternEncodePacket msg, FriendlyByteBuf buf) {}

	public static void handle(NTPatternEncodePacket msg, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			ServerPlayer player = ctx.get().getSender();
			if (player == null) return;
			if (!(player.containerMenu instanceof NTPatternEncoderMenu menu)) return;

			NTPatternEncoderBlockEntity encoder = menu.getEncoder();
			if (encoder == null) return;

			// Check blank pattern slot
			ItemStack blankSlot = encoder.getInventory().getStackInSlot(NTPatternEncoderBlockEntity.BLANK_SLOT);
			if (blankSlot.isEmpty() || !(blankSlot.getItem() instanceof BlankPatternItem)) return;

			// Check output slot is empty
			if (!encoder.getInventory().getStackInSlot(NTPatternEncoderBlockEntity.OUTPUT_SLOT).isEmpty()) return;

			// Collect grid inputs
			List<ItemStack> inputs = new ArrayList<>();
			boolean hasInput = false;
			for (int i = 0; i < NTPatternEncoderBlockEntity.GRID_SIZE; i++) {
				ItemStack gridItem = encoder.getInventory().getStackInSlot(i);
				inputs.add(gridItem.copy());
				if (!gridItem.isEmpty()) hasInput = true;
			}
			if (!hasInput) return;

			ItemStack encoded;

			if (encoder.isProcessingMode()) {
				// Processing pattern — collect outputs from processing output slots
				List<ItemStack> outputs = new ArrayList<>();
				boolean hasOutput = false;
				for (int i = 0; i < encoder.getProcessingOutputs().getSlots(); i++) {
					ItemStack outItem = encoder.getProcessingOutputs().getStackInSlot(i);
					if (!outItem.isEmpty()) {
						outputs.add(outItem.copy());
						hasOutput = true;
					}
				}
				if (!hasOutput) return;

				encoded = EncodedPatternItem.encodeProcessing(inputs, outputs);

			} else {
				// Crafting pattern — validate against recipe manager
				TransientCraftingContainer craftGrid = new TransientCraftingContainer(menu, 3, 3);
				for (int i = 0; i < 9; i++) {
					craftGrid.setItem(i, inputs.get(i).copy());
				}

				Optional<CraftingRecipe> recipe = player.level().getRecipeManager()
						.getRecipeFor(RecipeType.CRAFTING, craftGrid, player.level());

				if (recipe.isEmpty()) return; // Invalid recipe — don't encode

				ItemStack result = recipe.get().assemble(craftGrid, player.level().registryAccess());
				if (result.isEmpty()) return;

				ResourceLocation recipeId = recipe.get().getId();
				encoded = EncodedPatternItem.encodeCrafting(blankSlot, recipeId, inputs, result);
			}

			// Consume blank pattern, place encoded pattern in output
			blankSlot.shrink(1);
			encoder.getInventory().setStackInSlot(NTPatternEncoderBlockEntity.OUTPUT_SLOT, encoded);
			encoder.setChanged();
		});
		ctx.get().setPacketHandled(true);
	}

	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		LostdepthsMod.addNetworkMessage(NTPatternEncodePacket.class, NTPatternEncodePacket::encode, NTPatternEncodePacket::new, NTPatternEncodePacket::handle);
	}
}
