package com.wardendimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Llorón del Crepusculo: tronco inclinado, ramas en arco que caen y cortinas de hebras violetas. */
public class DuskWeeperFeature extends Feature<NoneFeatureConfiguration> {
    public DuskWeeperFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel l = ctx.level();
        BlockPos o = ctx.origin();
        RandomSource r = ctx.random();
        if (!TreeUtil.groundOk(l, o.below())) return false;

        int h = 8 + r.nextInt(4);
        double lean = 3 + r.nextInt(3), a = r.nextDouble() * Math.PI * 2;
        BlockPos top = o;
        for (int y = 0; y < h; y++) {
            double k = (double) y / h; k *= k;
            top = o.offset((int) Math.round(Math.cos(a) * lean * k), y, (int) Math.round(Math.sin(a) * lean * k));
            TreeUtil.put(l, top, TreeUtil.log());
            if (y > 0) TreeUtil.put(l, top.below(), TreeUtil.log());
        }
        // ramas en arco
        int branches = 5 + r.nextInt(2);
        for (int i = 0; i < branches; i++) {
            double b = a + i * (Math.PI * 2 / branches) + r.nextDouble() * 0.4;
            int len = 5 + r.nextInt(3);
            for (int s = 1; s <= len; s++) {
                double u = (double) s / len;
                int by = (int) Math.round(Math.sin(u * Math.PI * 0.8) * 2.5 - (u > 0.6 ? (u - 0.6) * 6 : 0));
                BlockPos p = top.offset((int) Math.round(Math.cos(b) * s), by, (int) Math.round(Math.sin(b) * s));
                if (s <= len / 2) TreeUtil.put(l, p, TreeUtil.log());
                if (s >= 2) TreeUtil.blob(l, p, 1, TreeUtil.violetLeaves(), r);
                if (s >= len / 2 && r.nextInt(5) != 0) TreeUtil.strand(l, p.below(), 3 + r.nextInt(6));
            }
        }
        TreeUtil.blob(l, top.above(), 2, TreeUtil.violetLeaves(), r);
        return true;
    }
}
