package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

/** Busca y rellena un marco rectangular de Marco del Warden (interior 2x3 hasta 21x21). */
public final class PortalFrame {
    private PortalFrame() {}

    private static boolean frame(LevelAccessor l, BlockPos p) { return l.getBlockState(p).is(ModBlocks.WARDEN_FRAME.get()); }
    private static boolean free(LevelAccessor l, BlockPos p) { return l.getBlockState(p).isAir() || l.getBlockState(p).is(ModBlocks.WARDEN_PORTAL.get()); }

    public static boolean tryLight(LevelAccessor level, BlockPos start, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction left = right.getOpposite();

        // bajar hasta el marco inferior
        BlockPos bottom = start;
        int guard = 0;
        while (free(level, bottom.below()) && guard++ < 22) bottom = bottom.below();
        if (!frame(level, bottom.below())) return false;

        // esquina izquierda inferior del interior
        BlockPos origin = bottom;
        guard = 0;
        while (free(level, origin.relative(left)) && guard++ < 22) origin = origin.relative(left);
        if (!frame(level, origin.relative(left))) return false;

        // ancho
        int width = 0;
        while (width < 22 && free(level, origin.relative(right, width))) width++;
        if (width < 2 || width > 21 || !frame(level, origin.relative(right, width))) return false;

        // alto
        int height = 0;
        outer:
        while (height < 22) {
            for (int w = 0; w < width; w++) if (!free(level, origin.above(height).relative(right, w))) break outer;
            height++;
        }
        if (height < 3 || height > 21) return false;

        // comprobar marco completo
        for (int w = 0; w < width; w++) {
            if (!frame(level, origin.below().relative(right, w))) return false;
            if (!frame(level, origin.above(height).relative(right, w))) return false;
        }
        for (int h = 0; h < height; h++) {
            if (!frame(level, origin.above(h).relative(left))) return false;
            if (!frame(level, origin.above(h).relative(right, width))) return false;
        }

        BlockState portal = ModBlocks.WARDEN_PORTAL.get().defaultBlockState().setValue(WardenPortalBlock.AXIS, axis);
        for (int w = 0; w < width; w++)
            for (int h = 0; h < height; h++)
                level.setBlock(origin.above(h).relative(right, w), portal, 18);
        return true;
    }

    /** Construye un portal 4x5 (interior 2x3) con plataforma en destino. */
    public static void build(LevelAccessor level, BlockPos base, Direction.Axis axis) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockState f = ModBlocks.WARDEN_FRAME.get().defaultBlockState();
        BlockState air = net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        BlockState brick = ModBlocks.SCULK_BRICKS.get().defaultBlockState();
        Direction fwd = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        // plataforma 6x5 y despeje
        for (int a = -2; a <= 3; a++)
            for (int b = -2; b <= 2; b++) {
                BlockPos floor = base.relative(right, a).relative(fwd, b).below();
                level.setBlock(floor, brick, 18);
                for (int h = 0; h <= 5; h++) level.setBlock(floor.above(h + 1), air, 18);
            }
        // marco: x=-1..2 , y=-1..3 (relativo a base = esquina inferior-izq del interior)
        for (int a = -1; a <= 2; a++)
            for (int h = -1; h <= 3; h++) {
                boolean border = a == -1 || a == 2 || h == -1 || h == 3;
                BlockPos p = base.relative(right, a).above(h);
                level.setBlock(p, border ? f : ModBlocks.WARDEN_PORTAL.get().defaultBlockState().setValue(WardenPortalBlock.AXIS, axis), 18);
            }
    }
}
