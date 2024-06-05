package com.a1qs.the_vault_extras.integration.jei;

import com.a1qs.the_vault_extras.init.ModItems;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import java.util.Collections;
import java.util.List;

public class DummyRecipeProvider {
    public static List<Object> getAnvilRecipes(IVanillaRecipeFactory factory) {
        return Collections.singletonList(factory
                .createAnvilRecipe(new ItemStack((IItemProvider)iskallia.vault.init.ModItems.VAULT_CRYSTAL),

                Collections.singletonList(new ItemStack((IItemProvider)ModItems.CAKE_SEAL.get())),
                Collections.singletonList(new ItemStack((IItemProvider)iskallia.vault.init.ModItems.VAULT_CRYSTAL)))
        );
    }
}
