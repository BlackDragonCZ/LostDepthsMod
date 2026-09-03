package cz.blackdragoncz.lostdepths.item.armor;

import cz.blackdragoncz.lostdepths.client.model.armor.SpectrosArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

// Recovered from the 1.12.2 jar. Uses a real HumanoidModel, not the vanilla armor-layer system.
// STATS ARE PLACEHOLDERS copied from draconic.
public abstract class SpectrosArmorItem extends ArmorItem {

	public static final String TEXTURE = "lostdepths:textures/models/armor/spectros_armor.png";

	public SpectrosArmorItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			// 0 leaves maxDamage at 0, so canBeDepleted() is false: genuinely undamageable, no bar.
			@Override
			public int getDurabilityForType(ArmorItem.Type type) {
				return 0;
			}

			@Override
			public int getDefenseForType(ArmorItem.Type type) {
				return new int[]{2, 5, 6, 2}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 9;
			}

			@Override
			public SoundEvent getEquipSound() {
				return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("item.armor.equip_netherite"));
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of();
			}

			@Override
			public String getName() {
				return "spectros_armor";
			}

			@Override
			public float getToughness() {
				return 0f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0f;
			}
		}, type, properties);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return TEXTURE;
	}

	// Client-only call, and the anonymous class is a separate file, so the model never loads on a server.
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
				// HumanoidArmorLayer never calls setupAnim, so the wing animation is driven here.
				return SpectrosArmorModel.forEntity(entity);
			}
		});
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.literal("§6Immune to normal hits"));
		tooltip.add(Component.literal("§a2% Max HP Damage Reduction Per Piece"));
		tooltip.add(Component.literal("§7§lSet Bonuses:"));
		tooltip.add(Component.literal("§r§a+7% Max HP Damage Reduction"));
	}

	public static class Helmet extends SpectrosArmorItem {
		public Helmet() {
			super(ArmorItem.Type.HELMET, new Item.Properties());
		}
	}

	public static class Chestplate extends SpectrosArmorItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties());
		}
	}

	public static class Leggings extends SpectrosArmorItem {
		public Leggings() {
			super(ArmorItem.Type.LEGGINGS, new Item.Properties());
		}
	}

	public static class Boots extends SpectrosArmorItem {
		public Boots() {
			super(ArmorItem.Type.BOOTS, new Item.Properties());
		}
	}
}
