package cz.blackdragoncz.lostdepths.item.shield;

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
        list.add(Component.literal("§bWhen held in the offhand, reflect 100% of max health damage taken."));
    }

    private boolean isTrueDamage(DamageSource source, LivingEntity context) {
        Optional<ResourceKey<DamageType>> key = source.typeHolder().unwrapKey();
        return key.isPresent() && key.get().equals(LostdepthsModDamageTypes.TRUE_DAMAGE);
    }

    private DamageSource createReflectSource(LivingEntity victim, boolean trueDamage) {
        var registry = victim.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        var holder = trueDamage ?  registry.getHolderOrThrow(LostdepthsModDamageTypes.TRUE_DAMAGE) : registry.getHolderOrThrow(DamageTypes.THORNS);
        return new DamageSource(holder, victim);
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

        boolean isTrueDamage = isTrueDamage(event.getSource(), victim);

        float incoming = event.getAmount();
        float victimMax = victim.getMaxHealth();

        boolean shouldReflect = isTrueDamage || incoming >= victimMax;
        if (!shouldReflect) return;

        DamageSource reflectSource = createReflectSource(victim, isTrueDamage);
        withNoReflect(livingAttacker, () -> livingAttacker.hurt(reflectSource, incoming));
    }
}
