package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.ability.DodgeAbility;
import cz.blackdragoncz.lostdepths.ability.SpecialAbility;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** Plain map, not a Forge registry - abilities are code-only singletons and never referenced from data. */
public class LostdepthsModAbilities {

    private static final Map<ResourceLocation, SpecialAbility> REGISTRY = new LinkedHashMap<>();

    public static final DodgeAbility SOUL_DODGE = register(new DodgeAbility(LostdepthsMod.rl("soul_dodge")));

    public static <T extends SpecialAbility> T register(T ability) {
        REGISTRY.put(ability.id(), ability);
        return ability;
    }

    @Nullable
    public static SpecialAbility get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Collection<SpecialAbility> all() {
        return Collections.unmodifiableCollection(REGISTRY.values());
    }
}
