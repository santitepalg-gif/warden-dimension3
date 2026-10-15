package com.wardendimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Arbol Costilla: columna de hueso del Warden con pares de costillas curvas que caen como una caja toracica. */
public class RibTreeFeature extends Feature<NoneFeatureConfiguration> {
    public RibTreeFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel l = ctx.level();
        BlockPos o = ctx.origin();
        RandomSource r = ctx.random();
        if (!TreeUtil.groundOk(l, o.below())) return false;

        int h = 9 + r.nextInt(4);
        for (int y = 0; y < h; y++) TreeUtil.put(l, o.above(y), TreeUtil.bone());
        for (Direction d : Direction.Plane.HORIZONTAL) TreeUtil.put(l, o.relative(d), TreeUtil.bone());
        TreeUtil.put(l, o.above(h), TreeUtil.crystal());

        boolean alongX = r.nextBoolean();
        int pairs = 3 + r.nextInt(2);
        for (int p = 0; p < pairs; p++) {
            int y0 = h - 2 - p * 2;
            if (y0 < 3) break;
            int rad = 6 - p / 2 + r.nextInt(2);
            int drop = 5 + r.nextInt(2);
            for (int side = -1; side <= 1; side += 2) {
                for (int i = 0; i <= 14; i++) {
                    double th = Math.PI * 0.5 * i / 14.0;
                    int dx = side * (int) Math.round(rad * Math.sin(th));
                    int dy = -(int) Math.round(drop * (1 - Math.cos(th)));
                    BlockPos pos = alongX ? o.offset(dx, y0 + dy, 0) : o.offset(0, y0 + dy, dx);
                    BlockState bone = TreeUtil.bone();
                    TreeUtil.put(l, pos, bone);
                    if (i == 14) TreeUtil.strand(l, pos, 2 + r.nextInt(3));
                }
            }
        }
        return true;
    }
}
