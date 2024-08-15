package cz.blackdragoncz.lostdepths.advancements;
/*
import cz.blackdragoncz.lostdepths.util.TextComponentUtil;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record LostdepthsAdvancementBuilder(ResourceLocation location, String title, String description, @Nullable LostdepthsAdvancementBuilder parent) {

    public LostdepthsAdvancementBuilder(@Nullable LostdepthsAdvancementBuilder parent, ResourceLocation location) {
        this(parent, location, getSubName(location.getPath()));
    }

    private LostdepthsAdvancementBuilder(@Nullable LostdepthsAdvancementBuilder parent, ResourceLocation location, String subName) {
        this(location, subName, subName, parent);
    }

    public LostdepthsAdvancementBuilder {
        title = Util.makeDescriptionId("advancements", location.withPath(title + ".title"));
        description = Util.makeDescriptionId("advancements", location.withPath(description + ".description"));
    }

    public MutableComponent translateTitle() {
        return TextComponentUtil.translate(title);
    }

    public Component translateDescription() {
        return TextComponentUtil.translate(description);
    }

    private static String getSubName(String path) {
        int separator = path.lastIndexOf('/');
        if (separator == -1) {
            return path;
        } else if (separator + 1 == path.length()) {
            throw new IllegalArgumentException("Unexpected name portion");
        }
        return path.substring(separator + 1);
    }

}
*/