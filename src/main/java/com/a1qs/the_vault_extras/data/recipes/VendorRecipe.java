package com.a1qs.the_vault_extras.data.recipes;

import com.a1qs.the_vault_extras.init.ModRecipeTypes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import iskallia.vault.init.ModItems;
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

public class VendorRecipe implements IVendorRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItems;
    private final String range;

    public VendorRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItems, String range) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
        this.range = range;
    }

    public ItemStack getIcon () {
        return new ItemStack(ModItems.ETCHING);
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItems;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
    }

    public String getRange() {
        return this.range;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.VENDOR_SERIALIZER.get();
    }

    public static class VendorRecipeType implements IRecipeType<VendorRecipe> {
        @Override
        public String toString() {
            return VendorRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<VendorRecipe> {

        @Override
        public VendorRecipe read(ResourceLocation recipeId, JsonObject json) {
            ItemStack output = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "output"));
            String range = JSONUtils.getString(json, "range");


            JsonArray ingredients = JSONUtils.getJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.deserialize(ingredients.get(i)));
            }

            return new VendorRecipe(recipeId, output, inputs, range);

        }

        @Nullable
        @Override
        public VendorRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buffer.readInt(), Ingredient.EMPTY);

            for(int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.read(buffer));
            }

            ItemStack output = buffer.readItemStack();
            return new VendorRecipe(recipeId, output, inputs, buffer.readString());
        }

        @Override
        public void write(PacketBuffer buffer, VendorRecipe recipe) {
            buffer.writeInt(recipe.getIngredients().size());
            for(Ingredient ing: recipe.getIngredients()) {
               ing.write(buffer);
            }
            buffer.writeItemStack(recipe.getRecipeOutput(), false);
            buffer.writeString(recipe.range);
        }
    }
}
