
/*
*	MCreator note: This file will be REGENERATED on each build.
*/
package cz.blackdragoncz.lostdepths.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.common.BasicItemListing;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LostdepthsModTrades {
	@SubscribeEvent
	public static void registerTrades(VillagerTradesEvent event) {
		if (event.getType() == LostdepthsModVillagerProfessions.COSMIC_SCIENTIST.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.INFUSED_IRON.get(), 2),

					new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get()), 1024, 1, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 8),

					new ItemStack(LostdepthsModItems.INFUSED_CRYSTAL.get()), 1024, 1, 0.05f));
			event.getTrades().get(2)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 12), new ItemStack(LostdepthsModItems.CORROSIVE_TEAR.get(), 5), new ItemStack(LostdepthsModItems.KEY_FRAGMENT_3.get()), 1024, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 24), new ItemStack(Items.PRISMARINE_CRYSTALS, 4), new ItemStack(LostdepthsModItems.ENRICHED_PRISMARINE.get()), 1024, 5, 0.05f));
			event.getTrades().get(3)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 18), new ItemStack(LostdepthsModItems.NEOSTEEL_NUGGETS.get(), 4), new ItemStack(LostdepthsModItems.POLISHING_SOLUTION.get()), 1024, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 8), new ItemStack(Blocks.BONE_BLOCK, 2), new ItemStack(LostdepthsModItems.HARDENED_CALCIUM.get(), 2), 1024, 5, 0.05f));
			event.getTrades().get(4)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 20), new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 20), new ItemStack(LostdepthsModItems.REINFORCED_PLATE.get(), 3), 1024, 5, 0.05f));
			event.getTrades().get(4)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 25), new ItemStack(LostdepthsModItems.ENRAGED_IRON.get(), 3), new ItemStack(LostdepthsModItems.CORRUPTED_GEMSTONE.get(), 2), 1024, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 12), new ItemStack(Items.ENDER_EYE, 5), new ItemStack(LostdepthsModItems.HYPNOTIC_EYE.get()), 1024, 5, 0.05f));
		}
		if (event.getType() == LostdepthsModVillagerProfessions.ATMOSPHERIC_SCIENTIST.get()) {
			event.getTrades().get(1)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 2), new ItemStack(LostdepthsModItems.CELESTIAL_DIAMOND.get()), new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 4), 1024, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 2),

					new ItemStack(LostdepthsModItems.GRAVITY_DUST.get()), 1024, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 2), new ItemStack(Items.AMETHYST_SHARD), new ItemStack(LostdepthsModItems.CYCLONE_DUST.get()), 1024, 5, 0.05f));
			event.getTrades().get(2)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 20), new ItemStack(LostdepthsModItems.MAGMA_SOLUTION.get(), 2), new ItemStack(LostdepthsModItems.ENDER_VOLTAIC_SOLUTION.get()), 1024, 5, 0.05f));
			event.getTrades().get(3)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 50), new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 50), new ItemStack(LostdepthsModItems.ASTROMETAL_AMALGAMATION_GLUE.get()), 1024, 5, 0.05f));
			event.getTrades().get(3)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 60), new ItemStack(LostdepthsModItems.IONITE_CRYSTAL.get()), new ItemStack(LostdepthsModItems.IONITE_CRYSTAL.get(), 2), 1024, 5, 0.05f));
			event.getTrades().get(4).add(new BasicItemListing(new ItemStack(LostdepthsModItems.IRON_GOLEM_ESSENCE.get()),

					new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get()), 1024, 5, 0.05f));
			event.getTrades().get(4)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.CELESTIAL_IRON.get(), 22), new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 5), new ItemStack(LostdepthsModItems.LUMINESCENT_CUBES.get()), 1024, 5, 0.05f));
			event.getTrades().get(5).add(new BasicItemListing(new ItemStack(LostdepthsModItems.EXTERNAL_CLOVINITE_BATTERY.get(), 10), new ItemStack(LostdepthsModItems.ELECTRON_RECYCLER.get(), 5),
					new ItemStack(LostdepthsModItems.CLOVINITE_POWER_HUB.get()), 1024, 5, 0.05f));
			event.getTrades().get(5)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 20), new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 20), new ItemStack(LostdepthsModItems.POWER_FUNNEL.get()), 1024, 5, 0.05f));
		}
		if (event.getType() == LostdepthsModVillagerProfessions.BLACK_MARKET_SELLER.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.JAR_OF_SYRUP.get(), 12),

					new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 3), 1024, 5, 0.05f));
			event.getTrades().get(1)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 50), new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 50), new ItemStack(LostdepthsModItems.GEM_INGENUITY.get()), 2, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 50), new ItemStack(LostdepthsModItems.SOLARIUM_CORE.get(), 50), new ItemStack(LostdepthsModItems.GEM_RESOLVE.get()), 2, 5, 0.05f));
			event.getTrades().get(2)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 50), new ItemStack(LostdepthsModItems.SOLARIUM_SKY.get(), 50), new ItemStack(LostdepthsModItems.GEM_IMPOSITION.get()), 2, 5, 0.05f));
			event.getTrades().get(3)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 20), new ItemStack(LostdepthsModItems.UNSTABLE_INGOT.get()), new ItemStack(LostdepthsModItems.RADIOACTIVE_ISOTOPES.get()), 1024, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_HUNTER.get(), 20), new ItemStack(Items.GOLD_INGOT), new ItemStack(LostdepthsModItems.INFUSED_GOLD.get()), 1024, 5, 0.05f));
		}
		if (event.getType() == LostdepthsModVillagerProfessions.CHEMISTRY_OPERATOR.get()) {
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.REINFORCED_BOTTLE.get(), 3),

					new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 2), 1024, 5, 0.05f));
			event.getTrades().get(1).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 25),

					new ItemStack(LostdepthsModItems.EXPLOSIVE_SAC.get()), 1024, 5, 0.05f));
			event.getTrades().get(2)
					.add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 25), new ItemStack(LostdepthsModItems.INFUSED_REDSTONE.get(), 2), new ItemStack(LostdepthsModItems.EMBER_SAC.get()), 1024, 5, 0.05f));
			event.getTrades().get(2).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 25), new ItemStack(LostdepthsModItems.INFUSED_IRON.get(), 2), new ItemStack(LostdepthsModItems.GLOWING_SAC.get()), 1024, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 35),

					new ItemStack(LostdepthsModItems.ACID_TONGUE.get()), 1024, 5, 0.05f));
			event.getTrades().get(3).add(new BasicItemListing(new ItemStack(LostdepthsModItems.SOLARIUM_TOXIC.get(), 15),

					new ItemStack(LostdepthsModItems.REFLECTIVE_SHARD.get()), 1024, 5, 0.05f));
		}
	}
}
