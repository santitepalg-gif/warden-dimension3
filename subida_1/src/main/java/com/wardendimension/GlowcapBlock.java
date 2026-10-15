package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.Block;

public class GlowcapBlock extends BushBlock {
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 12, 14);

    public GlowcapBlock(Properties props) { super(props); }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter getter, BlockPos pos) {
        return state.is(ModBlocks.ECHO_MOSS.get()) || state.is(ModBlocks.TWILIGHT_SCULK.get())
                || state.is(ModBlocks.SCULK_STONE.get()) || state.is(net.minecraft.world.level.block.Blocks.SCULK)
                || state.is(ModBlocks.SCULK_BRICKS.get());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) { return SHAPE; }
}
