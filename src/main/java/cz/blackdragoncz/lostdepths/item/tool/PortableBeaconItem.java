
package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.procedures.item.PortableBeaconFunctionality;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;

import cz.blackdragoncz.lostdepths.world.inventory.PortableBeaconGUIMenu;
import cz.blackdragoncz.lostdepths.item.inventory.PortableBeaconInventoryCapability;

public class PortableBeaconItem extends Item {

    private record EffectEntry(String id, MobEffect effect, int amplifier) {}

    private static final EffectEntry[] EFFECT_ENTRIES = new EffectEntry[] {
            new EffectEntry("minecraft:speed",           MobEffects.MOVEMENT_SPEED, 1),
            new EffectEntry("minecraft:fire_resistance", MobEffects.FIRE_RESISTANCE, 1),
            new EffectEntry("minecraft:invisibility",    MobEffects.INVISIBILITY, 1),
            new EffectEntry("minecraft:jump_boost",      MobEffects.JUMP, 1),
            new EffectEntry("minecraft:night_vision",    MobEffects.NIGHT_VISION, 1),
            new EffectEntry("minecraft:regeneration",    MobEffects.REGENERATION, 0),
            new EffectEntry("minecraft:slow_falling",    MobEffects.SLOW_FALLING, 1),
            new EffectEntry("minecraft:strength",        MobEffects.DAMAGE_BOOST, 1),
            new EffectEntry("minecraft:water_breathing", MobEffects.WATER_BREATHING, 1),
            new EffectEntry("minecraft:instant_health",  MobEffects.HEAL, 0),
            new EffectEntry("minecraft:glowing",         MobEffects.GLOWING, 1) // include if you allow it
    };

    private static String toRoman(int number) {
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            default -> "I";
        };
    }


	public PortableBeaconItem() {
		super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
	}

	@Override
	public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
		return 0f;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, world, list, flag);
		list.add(Component.literal("§5When in off-hand,"));
        list.add(Component.literal("§5apply potion effect on every entity in §635 §5block radius"));
		list.add(Component.literal("§6Effect Capacity 10"));

        String stored = itemstack.getOrCreateTag().getString("potionListA");
        List<Component> enabled = new ArrayList<>();

        if (!stored.isEmpty()) {
            for (EffectEntry entry : EFFECT_ENTRIES) {
                if (stored.contains(entry.id)) {
                    Component name = Component.translatable(entry.effect.getDescriptionId());
                    String ampRoman = toRoman(entry.amplifier + 1);
                    enabled.add(Component.literal(" • ").append(name).append(Component.literal(" " + ampRoman)));
                }
            }
        }
        if (enabled.isEmpty()) {
            list.add(Component.literal("§7No effects configured"));
        } else {
            list.add(Component.literal("§2Enabled effects:"));
            list.addAll(enabled);
        }
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		if (entity instanceof ServerPlayer serverPlayer) {
			NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return Component.literal("Portable Beacon");
				}

				@Override
				public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
					FriendlyByteBuf packetBuffer = new FriendlyByteBuf(Unpooled.buffer());
					packetBuffer.writeBlockPos(entity.blockPosition());
					packetBuffer.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
					return new PortableBeaconGUIMenu(id, inventory, packetBuffer);
				}
			}, buf -> {
				buf.writeBlockPos(entity.blockPosition());
				buf.writeByte(hand == InteractionHand.MAIN_HAND ? 0 : 1);
			});
		}
		return ar;
	}

	@Override
	public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(itemstack, world, entity, slot, selected);

        if (entity instanceof LivingEntity living && living.getOffhandItem() == itemstack) {
            PortableBeaconFunctionality.execute(entity, itemstack);
        }

		/*if (selected)
			PortableBeaconToolInHandTickProcedure.execute(entity, itemstack);
		PortableBeaconToolInHandTickProcedure.execute(entity, itemstack);*/
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag compound) {
		return new PortableBeaconInventoryCapability();
	}

	@Override
	public CompoundTag getShareTag(ItemStack stack) {
		CompoundTag nbt = stack.getOrCreateTag();
		stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> nbt.put("Inventory", ((ItemStackHandler) capability).serializeNBT()));
		return nbt;
	}

	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
		super.readShareTag(stack, nbt);
		if (nbt != null)
			stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> ((ItemStackHandler) capability).deserializeNBT((CompoundTag) nbt.get("Inventory")));
	}
}
