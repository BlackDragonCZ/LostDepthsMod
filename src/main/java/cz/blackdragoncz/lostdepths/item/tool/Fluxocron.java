package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import cz.blackdragoncz.lostdepths.client.CustomRayTraceResult;
import cz.blackdragoncz.lostdepths.util.BasicTeleporter;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Fluxocron extends Item {

    public enum FluxocronMode
    {
        MarkLocation("Mark Location"),
        MarkEntity("Mark Entity"),
        TeleportToYou("Teleport To You"),
        TeleportToMarkedLocation("Teleport To Marked Location"),
        Clear("Clear everything");

        private final String name;

        FluxocronMode(String name)
        {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public Fluxocron() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    private void teleport(ItemStack stack, Player player, Level level, double x, double y, double z)
    {
        var entityList = stack.getOrCreateTagElement("EntityList");

        if (entityList.contains("MobList"))
        {
            ListTag listTag = (ListTag) entityList.get("MobList");

            for (int i = 0; i < listTag.size(); ++i) {
                UUID entityUUID = NbtUtils.loadUUID(listTag.get(i));

                Entity entity = null;

                for (ServerLevel l : player.getServer().getAllLevels()) {
                    entity = l.getEntity(entityUUID);

                    if (entity != null)
                        break;
                }

                if (entity == null)
                    continue;

                if (entity.level() == level) {
                    entity.teleportTo(x, y, z);
                } else {
                    BasicTeleporter basicTeleporter = new BasicTeleporter((ServerLevel) player.level(), x, y, z);
                    entity.changeDimension((ServerLevel) player.level(), basicTeleporter);
                }
            }
        }

        if (entityList.contains("PlayerList"))
        {
            ListTag listTag = (ListTag) entityList.get("PlayerList");
            for (int i = 0; i < listTag.size(); ++i) {
                UUID playerUUID = NbtUtils.loadUUID(listTag.get(i));
                Player playerToPort = player.getServer().getPlayerList().getPlayer(playerUUID);

                if (playerToPort == null)
                    continue;

                if (playerToPort.level() == level) {
                    playerToPort.teleportTo(x, y, z);
                } else {
                    BasicTeleporter basicTeleporter = new BasicTeleporter((ServerLevel) player.level(), x, y, z);
                    playerToPort.changeDimension((ServerLevel) player.level(), basicTeleporter);
                }

                player.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = InteractionResultHolder.success(player.getItemInHand(hand));
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.hasTag()) {
            stack.getOrCreateTag().putInt("mode_style", 0);
        }

        if (player.isCrouching()) {
            int newMode = stack.getOrCreateTag().getInt("mode_style") + 1;
            if (newMode >= FluxocronMode.values().length) {
                newMode = 0;
            }

            stack.getOrCreateTag().putInt("mode_style", newMode);
            if (!level.isClientSide) {
                player.displayClientMessage(Component.literal( "§6Mode Switched: " + FluxocronMode.values()[newMode].getName()), true);
            }
        } else {
            FluxocronMode mode = FluxocronMode.values()[stack.getOrCreateTag().getInt("mode_style")];

            switch (mode) {
                case MarkLocation:
                    stack.getOrCreateTag().putDouble("flux_pos_x", player.getX());
                    stack.getOrCreateTag().putDouble("flux_pos_y", player.getY());
                    stack.getOrCreateTag().putDouble("flux_pos_z", player.getZ());
                    stack.getOrCreateTag().putString("flux_dimension", level.dimension().location().toString());

                    if (!level.isClientSide) {
                        player.displayClientMessage(Component.literal("§5Coordinate marked for fluxation."), true);
                        level.playSound(null, player.blockPosition(), LostdepthsModSounds.FLUX_MARK.get(), SoundSource.MASTER, 2.0F, 1.0F);
                    }
                    break;

                case MarkEntity:
                    break;

                case TeleportToYou:
                    if (!level.isClientSide) {
                       teleport(stack, player, level, player.getX(), player.getY(), player.getZ());
                    }
                    player.getCooldowns().addCooldown(ar.getObject().getItem(), 10);
                    break;

                case TeleportToMarkedLocation:
                    if (!level.isClientSide) {
                        if (stack.getOrCreateTag().contains("flux_dimension")) {
                            double goX = stack.getOrCreateTag().getDouble("flux_pos_x");
                            double goY = stack.getOrCreateTag().getDouble("flux_pos_y");
                            double goZ = stack.getOrCreateTag().getDouble("flux_pos_z");

                            teleport(stack, player, level, goX, goY, goZ);
                        } else {
                            player.displayClientMessage(Component.literal("§4Position not set!"), true);
                        }
                    }
                    player.getCooldowns().addCooldown(ar.getObject().getItem(), 10);
                    break;
                case Clear:
                    stack.removeTagKey("EntityList");
                    player.displayClientMessage(Component.literal("§5Fluxocron cleared"), true);
                    break;
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand hand) {
        if (!stack.hasTag()) {
            stack.getOrCreateTag().putInt("mode_style", 0);
        }

        FluxocronMode mode = FluxocronMode.values()[stack.getOrCreateTag().getInt("mode_style")];

        if (mode != FluxocronMode.MarkEntity) {
            return InteractionResult.PASS;
        }

        var entitiesTag = stack.getOrCreateTagElement("EntityList");

        if (interactionTarget instanceof Mob interactionMob) {
            interactionMob.setPersistenceRequired();

            if (entitiesTag.contains("MobList")) {
                ListTag listTag = (ListTag) entitiesTag.get("MobList");
                var uuidTag = NbtUtils.createUUID(interactionMob.getUUID());

                if (listTag.contains(uuidTag))
                    return InteractionResult.PASS;

                listTag.add(uuidTag);
            } else {
                ListTag listTag = new ListTag();
                listTag.add(NbtUtils.createUUID(interactionMob.getUUID()));
                entitiesTag.put("MobList", listTag);
            }

            player.displayClientMessage(Component.literal("§5Mob added to Fluxocron"), true);
        }

        if (interactionTarget instanceof Player interactionPlayer) {
            if (entitiesTag.contains("PlayerList")) {
                ListTag listTag = (ListTag) entitiesTag.get("PlayerList");
                var uuidTag = NbtUtils.createUUID(interactionPlayer.getUUID());

                if (!listTag.contains(uuidTag))
                    listTag.add(uuidTag);
            } else {
                ListTag listTag = new ListTag();
                listTag.add(NbtUtils.createUUID(interactionPlayer.getUUID()));
                entitiesTag.put("PlayerList", listTag);
            }

            player.displayClientMessage(Component.literal("§5Player added to Fluxocron"), true);
        }

        player.setItemInHand(hand, stack);

        player.level().playSound(null, player.blockPosition(), LostdepthsModSounds.FLUX_MARK.get(), SoundSource.MASTER, 2.0F, 1.0F);

        return InteractionResult.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> list, TooltipFlag flagIn) {
        list.add(Component.literal("§5Mass Teleports Entities To Your/Marked Position"));
        if (stack.hasTag()) {
            FluxocronMode style = FluxocronMode.values()[stack.getOrCreateTag().getInt("mode_style")];
            list.add(Component.literal("§dMode: " + style.getName()));

            if (stack.getTag().contains("flux_dimension")) {
                list.add(Component.literal("§2Flux Marked:"));
                ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(stack.getTag().getString("flux_dimension")));

                list.add(Component.literal("§2Dimension: ").append(Component.translatable("§2" + dim.location().toLanguageKey())));
                list.add(Component.literal("§2X: " + stack.getTag().getInt("flux_pos_x")));
                list.add(Component.literal("§2Y: " + stack.getTag().getInt("flux_pos_y")));
                list.add(Component.literal("§2Z: " + stack.getTag().getInt("flux_pos_z")));
            } else {
                list.add(Component.literal("§cNO FLUX LOCATION SET"));
            }

            var entitiesTag = stack.getOrCreateTagElement("EntityList");

            int mobCount = entitiesTag.contains("MobList") ? ((ListTag)entitiesTag.get("MobList")).size() : 0;
            int playerCount = entitiesTag.contains("PlayerList") ? ((ListTag)entitiesTag.get("PlayerList")).size() : 0;

            if (mobCount > 0 || playerCount > 0) {
                list.add(Component.literal(" "));
                list.add(Component.literal("§2Entities added:"));

                if (mobCount > 0)
                    list.add(Component.literal(" §2Mobs: " + mobCount));

                if (playerCount > 0)
                    list.add(Component.literal(" §2Players: " + playerCount));
            }
        }
    }
}
