package com.a1qs.the_vault_extras.data.recipes;

import com.a1qs.the_vault_extras.init.ModBlocks;
import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class RecyclerRecipe implements IRecyclerRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;


    public RecyclerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.VAULT_RECYCLER.get());
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return recipeItems.get(0).test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.RECYCLER_SERIALIZER.get();
    }

    public static class RecyclerRecipeType implements IRecipeType<RecyclerRecipe> {
        @Override
        public String toString() {
            return RecyclerRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<RecyclerRecipe> {

        @Override
        public RecyclerRecipe read(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));


            JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY);
            inputs.set(0, Ingredient.deserialize(ingredients.get(0)));


            return new RecyclerRecipe(recipeId, output, inputs);
        }

        @Nullable
        @Override
        public RecyclerRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);
            inputs.set(0, Ingredient.read(buffer));


            ItemStack output = buffer.readItemStack();
            return new RecyclerRecipe(recipeId, output, inputs);
        }

        @Override
        public void write(PacketBuffer buffer, RecyclerRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for(Ingredient ing: recipe.getIngredients()) {
                ing.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
        }
    }

}
