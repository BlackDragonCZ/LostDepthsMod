package cz.blackdragoncz.lostdepths.client.jade;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.init.LostdepthsModBlocks;
import cz.blackdragoncz.lostdepths.init.LostdepthsModItems;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.OreDefinition;
import cz.blackdragoncz.lostdepths.init.LostdepthsModOres.PickaxeTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum JadeOreProvider implements IBlockComponentProvider {
	INSTANCE;

	private record ToolRequirement(Supplier<Item> tool, String name, ChatFormatting color) {}

	private static Map<Block, ToolRequirement> extraOres;

	private static Map<Block, ToolRequirement> getExtraOres() {
		if (extraOres == null) {
			extraOres = new IdentityHashMap<>();
			// Celestial pickaxe tier
			ToolRequirement celestial = new ToolRequirement(LostdepthsModItems.CELESTIAL_PICKAXE::get, "Celestial", ChatFormatting.LIGHT_PURPLE);
			extraOres.put(LostdepthsModBlocks.COGNITIUM_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.NOXHERTIUM_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.NECROTONITE_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.PSYCHERIUM_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.VARLLERIUM_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.BIOLLITERITE_ORE.get(), celestial);
			extraOres.put(LostdepthsModBlocks.HARD_CRYSTAL_B.get(), celestial);
			extraOres.put(LostdepthsModBlocks.HARD_CRYSTAL_R.get(), celestial);
			// Crystalized pickaxe tier
			ToolRequirement crystalized = new ToolRequirement(LostdepthsModItems.CRYSTALIZED_PICKAXE::get, "Crystalized", ChatFormatting.AQUA);
			extraOres.put(LostdepthsModBlocks.CRYZULITE_ORE.get(), crystalized);
			extraOres.put(LostdepthsModBlocks.ZERITHIUM_ORE.get(), crystalized);
			// Forgefire axe tier
			ToolRequirement forgefireAxe = new ToolRequirement(LostdepthsModItems.FORGEFIRE_AXE::get, "Forgefire", ChatFormatting.GOLD);
			extraOres.put(LostdepthsModBlocks.CLOVINITE_ORE.get(), forgefireAxe);
			extraOres.put(LostdepthsModBlocks.SUNDER_WOOD_SAP.get(), forgefireAxe);
		}
		return extraOres;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		Block block = accessor.getBlock();

		// Check centralized ore registry first
		OreDefinition def = LostdepthsModOres.findByBlock(block);
		if (def != null) {
			PickaxeTier tier = def.minTier();
			renderTierIcon(tooltip, new ItemStack(tier.getItem()), formatTierName(tier), getTierColor(tier));
			return;
		}

		// Check extra ores not in the OreDefinition system
		ToolRequirement req = getExtraOres().get(block);
		if (req != null) {
			renderTierIcon(tooltip, new ItemStack(req.tool.get()), req.name, req.color);
		}
	}

	private void renderTierIcon(ITooltip tooltip, ItemStack toolStack, String tierName, ChatFormatting color) {
		IElementHelper elements = tooltip.getElementHelper();
		IElement icon = elements.item(toolStack, 0.5f).size(new Vec2(10, 10)).translate(new Vec2(0, -1));
		icon.message(null);
		tooltip.add(icon);

		MutableComponent text = Component.literal(tierName + "+").withStyle(color);
		tooltip.append(text);
	}

	private static String formatTierName(PickaxeTier tier) {
		String name = tier.name().toLowerCase();
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	private static ChatFormatting getTierColor(PickaxeTier tier) {
		return switch (tier) {
			case FORGEFIRE -> ChatFormatting.GOLD;
			case CRYSTALIZED -> ChatFormatting.AQUA;
			case CELESTIAL -> ChatFormatting.LIGHT_PURPLE;
			case NIGHTMARE -> ChatFormatting.DARK_RED;
		};
	}

	@Override
	public ResourceLocation getUid() {
		return LostdepthsMod.rl("ore_tier");
	}
}
