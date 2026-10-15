package com.wardendimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Racimo de espigas de cristal inclinadas (una grande y varias pequeñas). */
public class CrystalClusterFeature extends Feature<NoneFeatureConfiguration> {
    public CrystalClusterFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel l = ctx.level();
        BlockPos o = ctx.origin();
        RandomSource r = ctx.random();
        if (!TreeUtil.groundOk(l, o.below())) return false;
        int n = 4 + r.nextInt(4);
        for (int i = 0; i < n; i++) {
            int x = o.getX() + (i == 0 ? 0 : r.nextInt(7) - 3), z = o.getZ() + (i == 0 ? 0 : r.nextInt(7) - 3);
            int y = l.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z);
            BlockPos base = new BlockPos(x, y, z);
            if (!TreeUtil.groundOk(l, base.below())) continue;
            int hgt = i == 0 ? 9 + r.nextInt(6) : 3 + r.nextInt(6);
            double lx = (r.nextDouble() - 0.5) * 0.7, lz = (r.nextDouble() - 0.5) * 0.7;
            for (int s = 0; s < hgt; s++)
                TreeUtil.put(l, base.offset((int) Math.round(lx * s), s, (int) Math.round(lz * s)), TreeUtil.crystal());
        }
        return true;
    }
}
