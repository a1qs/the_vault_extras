package com.a1qs.the_vault_extras.block.render;

import com.a1qs.the_vault_extras.block.tileentity.DecayedCrystallizerTile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class DecayedCrystallizerTileRender extends TileEntityRenderer<DecayedCrystallizerTile> {
    public DecayedCrystallizerTileRender(TileEntityRendererDispatcher p_i226006_1_) {
        super(p_i226006_1_);
    }

    @Override
    public void render(DecayedCrystallizerTile decayedCrystallizerTile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        ItemStack stack = decayedCrystallizerTile.getDisplayStack();
        if (!stack.isEmpty()) {
            matrixStack.push(); // Save the current transformation state

            // Apply bobbing effect similar to ground item
            long time = decayedCrystallizerTile.getWorld().getGameTime();
            float rotation = (time + partialTicks) * 2; // Rotation speed (adjust as needed)
            float bobbing = MathHelper.sin((time + partialTicks) / 10.0F) * 0.1F + 0.1F; // Bobbing effect

            // Apply translations and rotations
            matrixStack.translate(0.5, 1.0 + bobbing, 0.5); // Center the item and apply bobbing
            matrixStack.rotate(Vector3f.YP.rotationDegrees(rotation)); // Rotate the item
            matrixStack.scale(1.25f, 1.25f, 1.25f);

            // Get the item renderer
            Minecraft.getInstance().getItemRenderer().renderItem(stack, ItemCameraTransforms.TransformType.GROUND, combinedLight, combinedOverlay, matrixStack, buffer);

            matrixStack.pop(); // Restore the transformation state
        }
    }

}
