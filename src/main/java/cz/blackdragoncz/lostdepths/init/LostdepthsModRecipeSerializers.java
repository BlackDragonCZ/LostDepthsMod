package cz.blackdragoncz.lostdepths.init;

import cz.blackdragoncz.lostdepths.LostdepthsMod;
import cz.blackdragoncz.lostdepths.recipe.*;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class LostdepthsModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> REGISTRY_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LostdepthsMod.MODID);
    public static final RegistryObject<CompressingRecipeSerializer> V1_COMPRESSING = REGISTRY_SERIALIZER.register("v1_compressing", () -> new CompressingRecipeSerializer((ResourceLocation id, ItemStack input, ItemStack output) -> new CompressingRecipe(id, input, output) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.V1_COMPRESSING.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.V1_COMPRESSING.get();
        }
    }));

    public static final RegistryObject<CompressingRecipeSerializer> V2_COMPRESSING = REGISTRY_SERIALIZER.register("v2_compressing", () -> new CompressingRecipeSerializer((ResourceLocation id, ItemStack input, ItemStack output) -> new CompressingRecipe(id, input, output) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.V2_COMPRESSING.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.V2_COMPRESSING.get();
        }
    }));

    public static final RegistryObject<CompressingRecipeSerializer> V3_COMPRESSING = REGISTRY_SERIALIZER.register("v3_compressing", () -> new CompressingRecipeSerializer((ResourceLocation id, ItemStack input, ItemStack output) -> new CompressingRecipe(id, input, output) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.V3_COMPRESSING.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.V3_COMPRESSING.get();
        }
    }));

    public static final RegistryObject<LDShapedRecipeSerializer> GALACTIC_WORKSTATION = REGISTRY_SERIALIZER.register("galactic_workstation", () -> new LDShapedRecipeSerializer((ResourceLocation id, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) -> new LDShapedRecipe(id, width, height, recipeItems, result) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.GALACTIC_WORKSTATION.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.GALACTIC_WORKSTATION.get();
        }
    }));

    public static final RegistryObject<LDShapedRecipeSerializer> ALLOY_WORKSTATION = REGISTRY_SERIALIZER.register("alloy_workstation", () -> new LDShapedRecipeSerializer((ResourceLocation id, int width, int height, NonNullList<Ingredient> recipeItems, ItemStack result) -> new LDShapedRecipe(id, width, height, recipeItems, result) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.ALLOY_WORKSTATION.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.ALLOY_WORKSTATION.get();
        }
    }));

    public static final RegistryObject<ModuleRecipe.Serializer> MODULE_CREATOR = REGISTRY_SERIALIZER.register("module_creator", () -> new ModuleRecipe.Serializer((ResourceLocation id, NonNullList<Ingredient> recipeItems, ItemStack result) -> new ModuleRecipe(id, recipeItems, result) {
        @Override
        public @NotNull RecipeType<?> getType() {
            return LostDepthsModRecipeType.MODULE_CREATOR.get();
        }

        @Override
        public @NotNull RecipeSerializer<?> getSerializer() {
            return LostdepthsModRecipeSerializers.MODULE_CREATOR.get();
        }
    }));

    public static final RegistryObject<ItemUseRecipe.Serializer> ITEM_USE = REGISTRY_SERIALIZER.register("item_use", ItemUseRecipe.Serializer::new);
    public static final RegistryObject<MetaMaterializerRecipe.Serializer> META_MATERIALIZER = REGISTRY_SERIALIZER.register("meta_materializer", MetaMaterializerRecipe.Serializer::new);
}
