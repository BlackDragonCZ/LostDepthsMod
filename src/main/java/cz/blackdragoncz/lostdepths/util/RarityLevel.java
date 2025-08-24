package cz.blackdragoncz.lostdepths.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.function.UnaryOperator;
//TODO: extend colors for Cosmic, Supreme with colors of aqua-dark_aqua for Cosmic & red-dark_red for Supreme
public enum RarityLevel implements IExtensibleEnum {

    COMMON(ChatFormatting.WHITE),
    UNCOMMON(ChatFormatting.YELLOW),
    RARE(ChatFormatting.AQUA),
    EPIC(ChatFormatting.LIGHT_PURPLE),
    LEGENDARY(ChatFormatting.GOLD),
    COSMIC(ChatFormatting.DARK_AQUA), //TODO: must be animated
    SUPREME(ChatFormatting.DARK_RED); //TODO: must be animated

    public final ChatFormatting color;
    private final UnaryOperator<Style> styleModifier;

    private RarityLevel(ChatFormatting pcolor) {
        this.color = pcolor;
        this.styleModifier = style -> style.withColor(pcolor);
    }

    RarityLevel(UnaryOperator<Style> styleModifier) {
        this.color = ChatFormatting.BLACK;
        this.styleModifier = styleModifier;
    }

    public UnaryOperator<Style> getStyleModifier() {
        return this.styleModifier;
    }

    public static RarityLevel create(String name, ChatFormatting pcolor) {
        throw new IllegalStateException("Enum not extended");
    }

    public static RarityLevel create(String name, UnaryOperator<Style> styleModifier) {
        throw new IllegalStateException("Enum not extended");
    }
}
