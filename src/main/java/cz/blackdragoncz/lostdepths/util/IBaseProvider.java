package cz.blackdragoncz.lostdepths.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IBaseProvider extends IHasTextComponent, IHasTranslationKey {

    ResourceLocation getRegistryName();

    /**
     * Gets the "name" or "path" of the registry name.
     */
    default String getName() {
        return getRegistryName().getPath();
    }

    @Override
    default Component getTextComponent() {
        return Component.translatable(getTranslationKey());
    }

}
