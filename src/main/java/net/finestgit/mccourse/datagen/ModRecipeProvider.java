package net.finestgit.mccourse.datagen;

import net.finestgit.mccourse.MCCourseMod;
import net.finestgit.mccourse.block.ModBlocks;
import net.finestgit.mccourse.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        List<ItemLike> BLACK_OPAL_SMELTABLES = List.of(
                ModItems.RAW_BLACK_OPAL,
                ModBlocks.BLACK_OPAL_ORE,
                ModBlocks.BLACK_OPAL_DEEPSLATE_ORE,
                ModBlocks.BLACK_OPAL_NETHER_ORE,
                ModBlocks.BLACK_OPAL_END_ORE
        );
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.BLACK_OPAL_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.BLACK_OPAL.get())
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get())).save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.BLACK_OPAL.get(), 9)
                .requires(ModBlocks.BLACK_OPAL_BLOCK.get())
                .unlockedBy("has_black_opal_block", has(ModBlocks.BLACK_OPAL_BLOCK.get())).save(recipeOutput);

        oreSmelting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC,  ModItems.BLACK_OPAL.get(), 0.25f, 200, "black_opal");
        oreBlasting(recipeOutput, BLACK_OPAL_SMELTABLES, RecipeCategory.MISC,  ModItems.BLACK_OPAL.get(), 0.25f, 100, "black_opal");

        stairBuilder(ModBlocks.BLACK_OPAL_STAIRS.get(), Ingredient.of(ModItems.BLACK_OPAL.get()))
                .group("black_opal")
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get()))
                .save(recipeOutput);
        slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLACK_OPAL_SLAB.get(), ModItems.BLACK_OPAL.get());

        pressurePlate(recipeOutput, ModBlocks.BLACK_OPAL_PRESSURE_PLATE.get(), ModItems.BLACK_OPAL.get());
        buttonBuilder(ModBlocks.BLACK_OPAL_BUTTON.get(), Ingredient.of(ModItems.BLACK_OPAL.get()))
                .group("black_opal")
                .unlockedBy("has_black_opal", has(ModItems.BLACK_OPAL.get()))
                .save(recipeOutput);
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory recipeCategory, ItemLike result,
                                      float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, ingredients, recipeCategory, result,
                experience, cookingTime, group, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> ingredients, RecipeCategory recipeCategory, ItemLike result,
                                      float experience, int cookingTime, String group) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, ingredients, recipeCategory, result,
                experience, cookingTime, group, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> recipe,
                                                                       List<ItemLike> ingredients, RecipeCategory recipeCategory, ItemLike result, float experience,
                                                                       int cookingTime, String group, String suffix) {
        for (ItemLike itemLike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemLike), recipeCategory, result, experience, cookingTime, cookingSerializer, recipe)
                    .group(group)
                    .unlockedBy(getHasName(itemLike), has(itemLike))
                    .save(recipeOutput, MCCourseMod.MODID + ":" + getItemName(result) + suffix + "_" + getItemName(itemLike));
        }
    }
}
