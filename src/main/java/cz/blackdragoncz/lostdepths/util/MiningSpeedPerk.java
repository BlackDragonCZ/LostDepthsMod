
package cz.blackdragoncz.lostdepths.util;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import cz.blackdragoncz.lostdepths.init.LostdepthsModAttributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class MiningSpeedPerk {
    private MiningSpeedPerk() {}

    public static float bonusForLevel(int lvl) {
        return (lvl * lvl) + 1.0f;
    }

    public static Multimap<Attribute, AttributeModifier> withLine(Multimap<Attribute, AttributeModifier> parent,
                                                                  EquipmentSlot slot, Item item, int level) {
        if (slot != EquipmentSlot.MAINHAND) return parent;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> bonus = ImmutableMultimap.builder();
        bonus.putAll(parent);

        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        UUID uuid = UUID.nameUUIDFromBytes(("lostdepths:mining_speed:" + id).getBytes(StandardCharsets.UTF_8));

        bonus.put(LostdepthsModAttributes.MINING_SPEED.get(),
                new AttributeModifier(uuid, "Mining speed perk", level, AttributeModifier.Operation.ADDITION));
        return bonus.build();
    }

}