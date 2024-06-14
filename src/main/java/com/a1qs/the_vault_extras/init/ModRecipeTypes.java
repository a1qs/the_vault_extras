package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.data.recipes.RecyclerRecipe;
import com.a1qs.the_vault_extras.data.recipes.VendorRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipeTypes implements IRecipeType {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VaultExtras.MOD_ID);


    public static final RegistryObject<RecyclerRecipe.Serializer> RECYCLER_SERIALIZER
            = RECIPE_SERIALIZER.register("recycle", RecyclerRecipe.Serializer::new);

    public static IRecipeType<RecyclerRecipe> RECYCLER_RECIPE
            = new RecyclerRecipe.RecyclerRecipeType();


    public static final RegistryObject<VendorRecipe.Serializer> VENDOR_SERIALIZER
            = RECIPE_SERIALIZER.register("vendor", VendorRecipe.Serializer::new);

    public static IRecipeType<VendorRecipe> VENDOR_RECIPE
            = new VendorRecipe.VendorRecipeType();







    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);
        Registry.register(Registry.RECIPE_TYPE, RecyclerRecipe.TYPE_ID, RECYCLER_RECIPE);

        Registry.register(Registry.RECIPE_TYPE, VendorRecipe.TYPE_ID, VENDOR_RECIPE);
    }

}
