package cz.blackdragoncz.lostdepths.item.shield;

import cz.blackdragoncz.lostdepths.ability.DamageOrigin;
import cz.blackdragoncz.lostdepths.init.LostdepthsModDamageTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MirrorShield extends Item {

    private static final String NO_REFLECTION = "lostdepths_no_reflection";

    public MirrorShield(){
        super(new Item.Properties().stacksTo(1));
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, LivingHurtEvent.class, this::onLivingHurt);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level world, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, world, list, flag);
        list.add(Component.literal("§bOffhand: reflects any single hit of at least your full max health back at the attacker."));
        list.add(Component.literal("§7Smaller hits and true damage pass straight through."));
    }

    private boolean isTrueDamage(DamageSource source) {
        Optional<ResourceKey<DamageType>> key = source.typeHolder().unwrapKey();
        return key.isPresent() && key.get().equals(LostdepthsModDamageTypes.TRUE_DAMAGE);
    }

    // Reflected, not a plain thorns source: the Soulbinder must not let its owner dodge this and Spectros must not absorb it.
    private DamageSource createReflectSource(LivingEntity victim) {
        var registry = victim.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        return new DamageOrigin.Reflected(registry.getHolderOrThrow(DamageTypes.THORNS), victim);
    }

    private static void withNoReflect(LivingEntity target, Runnable action) {
        var tag = target.getPersistentData();
        boolean prev = tag.getBoolean(NO_REFLECTION);

        try {
            tag.putBoolean(NO_REFLECTION, true);
            action.run();
        } finally {
            if (!prev) tag.remove(NO_REFLECTION);
            else tag.putBoolean(NO_REFLECTION, true);
        }
    }

    private void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity victim = event.getEntity();
        if (victim.getOffhandItem().getItem() != this) return;
        if (victim.getPersistentData().getBoolean(NO_REFLECTION)) return;

        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingAttacker)) return;

        float incoming = event.getAmount();

        // True damage bypasses this shield entirely
        if (isTrueDamage(event.getSource())) return;
        if (incoming < victim.getMaxHealth()) return;

        DamageSource reflectSource = createReflectSource(victim);
        withNoReflect(livingAttacker, () -> livingAttacker.hurt(reflectSource, incoming));
    }
}
