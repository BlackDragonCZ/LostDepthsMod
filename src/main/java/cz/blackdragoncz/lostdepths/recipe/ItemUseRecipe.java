package cz.blackdragoncz.lostdepths.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import cz.blackdragoncz.lostdepths.init.LostDepthsModRecipeType;
import cz.blackdragoncz.lostdepths.init.LostdepthsModRecipeSerializers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * "Use item X on target Y to get Z". The target and the result may each be either an item/block
 * ({@code {"item": ...}}) or a mob ({@code {"entity": ...}}); the entity forms display in the
 * separate "Use on Entity" JEI category.
 */
public class ItemUseRecipe extends LDRecipe<RecipeWrapper> {

    private ItemStack useItem;
    private ItemStack useOnItem;
    private EntityType<?> useOnEntity;
    private ItemStack result;
    private EntityType<?> resultEntity;
    private String useDescription;

    public ItemUseRecipe(ResourceLocation id, ItemStack useItem, ItemStack useOnItem, @Nullable EntityType<?> useOnEntity, ItemStack result,
            @Nullable EntityType<?> resultEntity, String useDescription) {
        super(id);
        this.useItem = useItem;
        this.useOnItem = useOnItem;
        this.useOnEntity = useOnEntity;
        this.result = result;
        this.resultEntity = resultEntity;
        this.useDescription = useDescription;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return LostdepthsModRecipeSerializers.ITEM_USE.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return LostDepthsModRecipeType.ITEM_USE.get();
    }

    @Override
    public boolean isIncomplete() {
        return false;
    }

    public ItemStack getUseItem() {
        return useItem;
    }

    public ItemStack getUseOnItem() {
        return useOnItem;
    }

    /** Non-null when the target is a mob instead of a block/item. */
    @Nullable
    public EntityType<?> getUseOnEntity() {
        return useOnEntity;
    }

    public ItemStack getResult() {
        return result;
    }

    /** Non-null when the recipe turns the target into a mob instead of giving an item. */
    @Nullable
    public EntityType<?> getResultEntity() {
        return resultEntity;
    }

    public String getUseDescription() {
        return useDescription;
    }

    public static class Serializer implements RecipeSerializer<ItemUseRecipe> {

        @Override
        public ItemUseRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack useItem = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "use_item"));

            JsonObject useOn = GsonHelper.getAsJsonObject(json, "use_on");
            EntityType<?> useOnEntity = entityFromJson(useOn, "use_on");
            ItemStack useOnItem = useOnEntity == null ? ShapedRecipe.itemStackFromJson(useOn) : ItemStack.EMPTY;

            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            EntityType<?> resultEntity = entityFromJson(resultJson, "result");
            ItemStack result = resultEntity == null ? ShapedRecipe.itemStackFromJson(resultJson) : ItemStack.EMPTY;

            return new ItemUseRecipe(id, useItem, useOnItem, useOnEntity, result, resultEntity, GsonHelper.getAsString(json, "use_description"));
        }

        @Nullable
        private static EntityType<?> entityFromJson(JsonObject json, String field) {
            if (!json.has("entity"))
                return null;
            ResourceLocation entityId = new ResourceLocation(GsonHelper.getAsString(json, "entity"));
            // The entity registry defaults to pig, so getValue alone would silently accept a typo.
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(entityId))
                throw new JsonSyntaxException("Unknown entity '" + entityId + "' in '" + field + "'");
            return ForgeRegistries.ENTITY_TYPES.getValue(entityId);
        }

        @Override
        public @Nullable ItemUseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new ItemUseRecipe(id, buf.readItem(), buf.readItem(), readEntity(buf), buf.readItem(), readEntity(buf), buf.readUtf());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, ItemUseRecipe recipe) {
            buf.writeItem(recipe.useItem);
            buf.writeItem(recipe.useOnItem);
            writeEntity(buf, recipe.useOnEntity);
            buf.writeItem(recipe.result);
            writeEntity(buf, recipe.resultEntity);
            buf.writeUtf(recipe.useDescription);
        }

        @Nullable
        private static EntityType<?> readEntity(FriendlyByteBuf buf) {
            if (!buf.readBoolean())
                return null;
            ResourceLocation entityId = buf.readResourceLocation();
            return ForgeRegistries.ENTITY_TYPES.containsKey(entityId) ? ForgeRegistries.ENTITY_TYPES.getValue(entityId) : null;
        }

        private static void writeEntity(FriendlyByteBuf buf, @Nullable EntityType<?> type) {
            ResourceLocation key = type == null ? null : ForgeRegistries.ENTITY_TYPES.getKey(type);
            buf.writeBoolean(key != null);
            if (key != null)
                buf.writeResourceLocation(key);
        }
    }
}
