package com.wardendimension;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/** Arbol Espira: tronco alto en espiral, copas en discos escalonados (como sombrillas) y hebras luminosas colgando. */
public class SpireTreeFeature extends Feature<NoneFeatureConfiguration> {
    public SpireTreeFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel l = ctx.level();
        BlockPos o = ctx.origin();
        RandomSource r = ctx.random();
        if (!TreeUtil.groundOk(l, o.below())) return false;

        int h = 14 + r.nextInt(8);
        double ph = r.nextDouble() * Math.PI * 2;
        int[] cx = new int[h], cz = new int[h];
        int ox = (int) Math.round(Math.sin(ph) * 1.5), oz = (int) Math.round(Math.cos(ph) * 1.5);
        for (int y = 0; y < h; y++) {
            cx[y] = (int) Math.round(Math.sin(y * 0.30 + ph) * 1.5) - ox;
            cz[y] = (int) Math.round(Math.cos(y * 0.30 + ph) * 1.5) - oz;
            BlockPos c = o.offset(cx[y], y, cz[y]);
            TreeUtil.put(l, c, TreeUtil.log());
            if (y > 0) TreeUtil.put(l, o.offset(cx[y], y - 1, cz[y]), TreeUtil.log()); // mantiene el tronco conectado
            if (y < 4) // raices en la base
                for (Direction d : Direction.Plane.HORIZONTAL)
                    if (y < 2 || r.nextInt(2) == 0) TreeUtil.put(l, c.relative(d), TreeUtil.log());
        }

        // copas: 4 discos tipo sombrilla, cada uno mas pequeño
        for (int t = 0; t < 4; t++) {
            int ty = h - 10 + t * 3;
            if (ty < 5) continue;
            int rad = 6 - t;
            BlockPos c = o.offset(cx[Math.min(ty, h - 1)], ty, cz[Math.min(ty, h - 1)]);
            for (int dx = -rad; dx <= rad; dx++)
                for (int dz = -rad; dz <= rad; dz++) {
                    double d = Math.hypot(dx, dz);
                    if (d > rad + 0.3) continue;
                    TreeUtil.put(l, c.offset(dx, 0, dz), TreeUtil.leaves());
                    if (d > rad - 2.2) TreeUtil.put(l, c.offset(dx, 1, dz), TreeUtil.leaves()); // borde elevado = forma de cuenco
                    if (d > rad - 1.2 && r.nextInt(3) == 0) {
                        TreeUtil.strand(l, c.offset(dx, 0, dz), 2 + r.nextInt(5));
                    } else if (d < rad - 2 && r.nextInt(14) == 0) {
                        TreeUtil.put(l, c.offset(dx, -1, dz), TreeUtil.crystal());
                    }
                }
        }
        // aguja de cristal en la cima
        BlockPos top = o.offset(cx[h - 1], h, cz[h - 1]);
        for (int i = 0; i < 3; i++) TreeUtil.put(l, top.above(i), TreeUtil.crystal());
        return true;
    }
}
