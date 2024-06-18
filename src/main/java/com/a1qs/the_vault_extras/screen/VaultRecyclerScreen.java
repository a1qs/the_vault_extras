package com.a1qs.the_vault_extras.screen;

import com.a1qs.the_vault_extras.VaultExtras;
import com.a1qs.the_vault_extras.block.tileentity.VaultRecyclerTile;
import com.a1qs.the_vault_extras.container.VaultRecyclerContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;


public class VaultRecyclerScreen extends ContainerScreen<VaultRecyclerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(VaultExtras.MOD_ID, "textures/gui/vault_recycler_gui.png");
    private final VaultRecyclerTile tile;
    public VaultRecyclerScreen(VaultRecyclerContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        tile = screenContainer.getRecyclerTile();

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);

        if(tile != null) {
            int smeltingDuration = getContainer().getRecyclerData().get(0);
            int l = smeltingDuration*24/30;
            this.blit(matrixStack, i + 54, j + 35, 176, 0, l + 1, 16);
        }


    }
}
