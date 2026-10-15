package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class WardenTeleporter implements ITeleporter {
    private final BlockPos from;
    private final Direction.Axis axis;

    public WardenTeleporter(BlockPos from, Direction.Axis axis) { this.from = from; this.axis = axis; }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel current, ServerLevel dest, float yaw, Function<Boolean, Entity> reposition) {
        Entity e = reposition.apply(false);
        BlockPos spot = findOrCreate(dest);
        e.moveTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5, yaw, e.getXRot());
        e.setDeltaMovement(0, 0, 0);
        e.getPersistentData().putInt("wd_cd", 100);
        return e;
    }

    @Override
    public boolean playTeleportSound(net.minecraft.server.level.ServerPlayer player, ServerLevel src, ServerLevel dst) { return false; }

    private BlockPos findOrCreate(ServerLevel dest) {
        int x = from.getX(), z = from.getZ();
        dest.getChunk(new BlockPos(x, 0, z));
        int surface = dest.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        if (surface <= dest.getMinBuildHeight() + 2) surface = 80;

        // buscar un portal existente cerca
        for (int dx = -10; dx <= 10; dx++)
            for (int dz = -10; dz <= 10; dz++)
                for (int y = surface - 12; y <= surface + 14; y++) {
                    BlockPos p = new BlockPos(x + dx, y, z + dz);
                    BlockState s = dest.getBlockState(p);
                    if (s.is(ModBlocks.WARDEN_PORTAL.get())) {
                        BlockPos low = p;
                        while (dest.getBlockState(low.below()).is(ModBlocks.WARDEN_PORTAL.get())) low = low.below();
                        Direction.Axis ax = s.getValue(WardenPortalBlock.AXIS);
                        return low.relative(ax == Direction.Axis.X ? Direction.SOUTH : Direction.EAST, 1);
                    }
                }
        BlockPos base = new BlockPos(x, surface + 1, z);
        PortalFrame.build(dest, base, axis);
        return base.relative(axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST, 1);
    }
}
