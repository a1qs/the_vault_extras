package com.a1qs.the_vault_extras.util;

import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;

public class VoxelShapeUtil {
    public static VoxelShape mergeVoxelShapes(VoxelShape[] shape) {
        VoxelShape combinedShape = VoxelShapes.empty();
        for(VoxelShape s : shape) {
            combinedShape = VoxelShapes.combineAndSimplify(combinedShape, s, IBooleanFunction.OR);
        }
        return combinedShape;
    }
}
