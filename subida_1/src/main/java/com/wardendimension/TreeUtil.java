package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;

/** Utilidades compartidas por los arboles personalizados. */
final class TreeUtil {
    private TreeUtil() {}

    static final int FLAGS = 19;

    static boolean groundOk(WorldGenLevel l, BlockPos below) {
        BlockState s = l.getBlockState(below);
        return s.is(ModBlocks.ECHO_MOSS.get()) || s.is(ModBlocks.TWILIGHT_SCULK.get()) || s.is(ModBlocks.SCULK_STONE.get())
                || s.is(ModBlocks.SCULK_BRICKS.get()) || s.is(Blocks.SCULK) || s.is(Blocks.DEEPSLATE);
    }

    static void put(WorldGenLevel l, BlockPos p, BlockState s) {
        if (TreeFeature.validTreePos(l, p)) l.setBlock(p, s, FLAGS);
    }

    static BlockState log() { return ModBlocks.RESONANT_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y); }
    static BlockState bone() { return ModBlocks.WARDEN_BONE.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y); }
    static BlockState leaves() { return ModBlocks.RESONANT_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true); }
    static BlockState violetLeaves() { return ModBlocks.TWILIGHT_LEAVES.get().defaultBlockState().setValue(LeavesBlock.PERSISTENT, true); }
    static BlockState crystal() { return ModBlocks.GLOW_CRYSTAL.get().defaultBlockState(); }

    /** Cuelga una hebra luminosa de 'len' bloques bajo 'from' (solo si hay aire). */
    static void strand(WorldGenLevel l, BlockPos from, int len) {
        BlockState s = ModBlocks.DANGLING_STRAND.get().defaultBlockState();
        for (int i = 1; i <= len; i++) {
            BlockPos p = from.below(i);
            if (!l.isEmptyBlock(p)) break;
            l.setBlock(p, s, FLAGS);
        }
    }

    static void blob(WorldGenLevel l, BlockPos c, int r, BlockState leaf, net.minecraft.util.RandomSource rand) {
        for (int dx = -r; dx <= r; dx++)
            for (int dy = -r; dy <= r; dy++)
                for (int dz = -r; dz <= r; dz++)
                    if (dx * dx + dy * dy + dz * dz <= r * r + 1 && rand.nextInt(7) != 0) put(l, c.offset(dx, dy, dz), leaf);
    }
}
