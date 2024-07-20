package com.a1qs.the_vault_extras.init;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.block.DecayedCrystallizer;
import com.a1qs.the_vault_extras.block.SanctifiedPedestalBlock;
import com.a1qs.the_vault_extras.block.VaultRecyclerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;


public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, VaultExtras.MOD_ID);

    public static final RegistryObject<Block> INFUSION_ALTAR = registerBlock("infusion_altar",
            () -> new Block(AbstractBlock.Properties.create(Material.ROCK)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE).setRequiresTool()
                    .hardnessAndResistance(3f)
                    .notSolid()));

    public static final RegistryObject<Block> VAULT_ANVIL = registerBlock("vault_anvil",
            () -> new AnvilBlock(AbstractBlock.Properties.from(Blocks.ANVIL)));


    public static final RegistryObject<Block> VAULT_RECYCLER = registerBlock("vault_recycler",
            () -> new VaultRecyclerBlock(AbstractBlock.Properties.create(Material.ROCK)
                    .harvestLevel(2)
                    .harvestTool(ToolType.PICKAXE).setRequiresTool()
                    .hardnessAndResistance(3f)
                    .notSolid()));

    public static final RegistryObject<Block> SANCTIFIED_PEDESTAL = registerBlock("sanctified_pedestal",
            () -> new SanctifiedPedestalBlock(AbstractBlock.Properties.create(Material.ROCK)
                    .hardnessAndResistance(-1f)
                    .notSolid()));

    public static final RegistryObject<Block> DECAYED_CRYSTALLIZER = registerBlock("decayed_crystallizer",
            () -> new DecayedCrystallizer(AbstractBlock.Properties.create(Material.IRON)
                    .hardnessAndResistance(-1f)
                    .notSolid()));


    private static <T extends Block>
    RegistryObject<T> registerBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);

        registerBlockItem(name, toReturn);

        return toReturn;
    }


    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.VAULT_EXTRAS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
