package cz.blackdragoncz.lostdepths.client.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Draws a mob inside a JEI recipe slot, scaled to fit. */
public final class JeiEntityRenderer {

    private static final Map<EntityType<?>, LivingEntity> CACHE = new HashMap<>();
    private static final Set<EntityType<?>> FAILED = new HashSet<>();

    private JeiEntityRenderer() {
    }

    /** x/y is the bottom centre of the mob; box is the height in pixels it should fit into. */
    public static void render(GuiGraphics graphics, EntityType<?> type, int x, int y, int box) {
        LivingEntity entity = instance(type);
        if (entity == null)
            return;
        float size = Math.max(Math.max(entity.getBbWidth(), entity.getBbHeight()), 0.1f);
        InventoryScreen.renderEntityInInventoryFollowsAngle(graphics, x, y, Mth.clamp((int) (box / size), 3, box), 0, 0, entity);
    }

    @Nullable
    private static LivingEntity instance(EntityType<?> type) {
        Level level = Minecraft.getInstance().level;
        if (level == null || FAILED.contains(type))
            return null;

        LivingEntity cached = CACHE.get(type);
        if (cached != null && cached.level() == level)
            return cached;

        // A dummy that is never added to the world. Some entities touch level state on construction,
        // so a failure is remembered rather than retried every frame.
        try {
            Entity created = type.create(level);
            if (created instanceof LivingEntity living) {
                CACHE.put(type, living);
                return living;
            }
        } catch (Exception ignored) {
        }
        FAILED.add(type);
        return null;
    }
}
