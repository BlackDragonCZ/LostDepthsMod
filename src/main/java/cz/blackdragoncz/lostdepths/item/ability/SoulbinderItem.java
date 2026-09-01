package cz.blackdragoncz.lostdepths.item.ability;

import cz.blackdragoncz.lostdepths.ability.SoulBinding;
import cz.blackdragoncz.lostdepths.ability.SpecialAbility;
import cz.blackdragoncz.lostdepths.ability.SpecialAbilityProvider;
import cz.blackdragoncz.lostdepths.init.LostdepthsModAbilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// Bind by using it, shift-use to unbind. Protects its owner from anyone who signed their contract book.
public class SoulbinderItem extends Item implements SpecialAbilityProvider {

    public SoulbinderItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).fireResistant());
    }

    @Override
    public List<SpecialAbility> getAbilities(ItemStack stack) {
        return List.of(LostdepthsModAbilities.SOUL_DODGE);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                // Only the owner may wipe it - otherwise anyone could clear a stolen one and claim it.
                if (SoulBinding.isOwner(stack, player)) {
                    SoulBinding.reset(stack);
                    player.displayClientMessage(Component.literal("§7The Soulbinder releases its bond."), true);
                } else if (SoulBinding.isBound(stack)) {
                    player.displayClientMessage(Component.literal("§cBound to " + SoulBinding.ownerName(stack) + "."), true);
                }
            } else if (!SoulBinding.isBound(stack)) {
                SoulBinding.bind(stack, player);
                player.displayClientMessage(Component.literal("§bThe Soulbinder binds to your soul."), true);
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return SoulBinding.isBound(stack);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!SoulBinding.isBound(stack)) {
            tooltip.add(Component.literal("§7Use to bind it to your soul."));
            return;
        }

        tooltip.add(Component.literal("Bound to: ").withStyle(ChatFormatting.GRAY)
                .append(Component.literal(SoulBinding.ownerName(stack)).withStyle(ChatFormatting.AQUA)));

        List<String> signers = SoulBinding.signerNames(stack);
        if (signers.isEmpty()) {
            tooltip.add(Component.literal("§8No contracts chained to it."));
        } else {
            tooltip.add(Component.literal("Chained: ").withStyle(ChatFormatting.GRAY)
                    .append(Component.literal(String.join(", ", signers)).withStyle(ChatFormatting.GOLD)));
        }
        tooltip.add(Component.literal("§8Shift-use to release."));
    }
}
