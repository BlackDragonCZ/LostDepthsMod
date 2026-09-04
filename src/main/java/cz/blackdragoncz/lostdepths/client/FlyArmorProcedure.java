package cz.blackdragoncz.lostdepths.client;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Every Lost Depths set grants creative flight, but only worn complete: chest + legs + boots, plus the helmet on any set that has one.
// LivingEquipmentChangeEvent is server only (LivingEntity:2429 sits under the isClientSide guard); the client learns via onUpdateAbilities.
@Mod.EventBusSubscriber
public class FlyArmorProcedure {

    private static final String GRANTED = "LostdepthsArmorFlight";
    private static final Map<String, Boolean> HELMET_SETS = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onEquipChange(LivingEquipmentChangeEvent event) {
        if (event.getSlot().getType() != EquipmentSlot.Type.ARMOR)
            return;
        if (event.getEntity() instanceof Player player)
            apply(player);
    }

    // detectEquipmentUpdates only fires on a change, so logging in with no armor never re-evaluates a stale grant loaded from NBT.
    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        apply(event.getEntity());
    }

    // Leaving creative wipes mayfly (GameType.updatePlayerAbilities via ServerPlayerGameMode:65) and that runs after this event fires
    // (ServerPlayer:1311), so re-derive on the next tick or a full set stops flying until a piece is jiggled.
    @SubscribeEvent
    public static void onGameModeChange(PlayerEvent.PlayerChangeGameModeEvent event) {
        Player player = event.getEntity();
        MinecraftServer server = player.getServer();
        if (server != null)
            server.execute(() -> {
                if (!player.isRemoved())
                    apply(player);
            });
    }

    private static void apply(Player player) {
        if (player.isCreative() || player.isSpectator()) {
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
            return;
        }

        if (wearsFlightSet(player)) {
            player.getPersistentData().putBoolean(GRANTED, true);
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
        } else if (player.getPersistentData().getBoolean(GRANTED)) {
            // Revoke only flight we handed out, or removing a boot would also strip jetpacks, /fly and other mods' gear.
            player.getPersistentData().putBoolean(GRANTED, false);
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    private static boolean wearsFlightSet(Player player) {
        String set = setName(player.getItemBySlot(EquipmentSlot.CHEST));
        if (set == null
                || !set.equals(setName(player.getItemBySlot(EquipmentSlot.LEGS)))
                || !set.equals(setName(player.getItemBySlot(EquipmentSlot.FEET))))
            return false;
        return !setHasHelmet(set) || set.equals(setName(player.getItemBySlot(EquipmentSlot.HEAD)));
    }

    // Material name keyed by namespace, so a new set flies with no change here.
    private static String setName(ItemStack stack) {
        if (!(stack.getItem() instanceof ArmorItem armor))
            return null;
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(armor);
        return key != null && LostdepthsMod.MODID.equals(key.getNamespace()) ? armor.getMaterial().getName() : null;
    }

    // Spectros is the only set with a helmet today; asking the registry keeps that a fact about the set, not a name hardcoded here.
    private static boolean setHasHelmet(String set) {
        return HELMET_SETS.computeIfAbsent(set, name -> {
            for (Item item : ForgeRegistries.ITEMS) {
                if (!(item instanceof ArmorItem armor) || armor.getEquipmentSlot() != EquipmentSlot.HEAD)
                    continue;
                ResourceLocation key = ForgeRegistries.ITEMS.getKey(armor);
                if (key != null && LostdepthsMod.MODID.equals(key.getNamespace()) && name.equals(armor.getMaterial().getName()))
                    return true;
            }
            return false;
        });
    }
}
