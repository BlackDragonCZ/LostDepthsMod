package cz.blackdragoncz.lostdepths.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TextComponentUtil {

    private TextComponentUtil() {
    }

    public static MutableComponent translate(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent translate(String key, Object... args) {
        return Component.translatable(key, args);
    }
}
