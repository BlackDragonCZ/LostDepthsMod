package cz.blackdragoncz.lostdepths.item.tool;

import cz.blackdragoncz.lostdepths.init.LostdepthsModSounds;
import cz.blackdragoncz.lostdepths.client.CustomRayTraceResult;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Fluxocron extends Item {
    private List<LivingEntity> storageList = new ArrayList<>();

    public Fluxocron() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.getItemInHand(handIn);

        if (!stack.hasTag()) {
            stack.getOrCreateTag().putInt("mode_style", 0);
        }

        if (playerIn.isCrouching()) {
            int newMode = stack.getOrCreateTag().getInt("mode_style") + 1;
            if (newMode == 4) newMode = 0;
            stack.getOrCreateTag().putInt("mode_style", newMode);
            if (!worldIn.isClientSide) {
                playerIn.displayClientMessage(Component.literal( "§6Mode Switched: " + modeFromInt(newMode)), true);
            }
        } else {
            switch (stack.getOrCreateTag().getInt("mode_style")) {
                case 0:
                    stack.getOrCreateTag().putDouble("flux_pos_x", playerIn.getX());
                    stack.getOrCreateTag().putDouble("flux_pos_y", playerIn.getY());
                    stack.getOrCreateTag().putDouble("flux_pos_z", playerIn.getZ());

                    if (!worldIn.isClientSide) {
                        playerIn.displayClientMessage(Component.literal("§5Coordinate marked for fluxation."), true);
                        worldIn.playSound(null, playerIn.blockPosition(), LostdepthsModSounds.METAL1.get(), SoundSource.MASTER, 2.0F, 1.0F);
                    }
                    break;

                case 1:
                    if (!worldIn.isClientSide) {
                        CustomRayTraceResult traceResult = entityTrace(worldIn, (LivingEntity) playerIn, 45, LivingEntity.class);
                        if (traceResult != null && traceResult.getResultEntity() != null) {
                            LivingEntity target = (LivingEntity) traceResult.getResultEntity();
                            if (!storageList.contains(target)) {
                                storageList.add(target);
                                playerIn.displayClientMessage(Component.literal("§5" + target.getName().getString() + " added to fluxation."), true);
                            }
                        }
                        worldIn.playSound(null, playerIn.blockPosition(), LostdepthsModSounds.METAL1.get(), SoundSource.MASTER, 2.0F, 1.0F);
                    }
                    break;

                case 2:
                    if (!worldIn.isClientSide && !storageList.isEmpty()) {
                        performTeleport(worldIn, playerIn, playerIn.getX(), playerIn.getY(), playerIn.getZ());
                    }
                    break;

                case 3:
                    if (!worldIn.isClientSide && !storageList.isEmpty() && stack.getOrCreateTag().contains("flux_pos_x")) {
                        double goX = stack.getOrCreateTag().getDouble("flux_pos_x");
                        double goY = stack.getOrCreateTag().getDouble("flux_pos_y");
                        double goZ = stack.getOrCreateTag().getDouble("flux_pos_z");
                        performTeleport(worldIn, playerIn, goX, goY, goZ);
                    }
                    break;
            }
        }
        return super.use(worldIn, playerIn, handIn);
    }

    private void performTeleport(Level worldIn, Player playerIn, double goX, double goY, double goZ) {
        worldIn.playSound(null, playerIn.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.MASTER, 1.0F, 1.0F);
        for (LivingEntity entity : storageList) {
            entity.teleportTo(goX, goY, goZ);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal("§5Mass Teleports Entities To Your/Marked Position"));
        if (stack.hasTag()) {
            int style = stack.getOrCreateTag().getInt("mode_style");
            tooltip.add(Component.literal("§dMode: " + modeFromInt(style)));
        }
    }

    private String modeFromInt(int mode) {
        switch (mode) {
            case 0:
                return "Mark Location";
            case 1:
                return "Mark Entity";
            case 2:
                return "Teleport To You";
            case 3:
                return "Teleport To Marked Location";
        }
        return "";
    }
}
