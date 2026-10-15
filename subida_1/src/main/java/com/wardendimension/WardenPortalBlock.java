package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WardenPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    private static final VoxelShape SHAPE_X = Block.box(0, 0, 6, 16, 16, 10);
    private static final VoxelShape SHAPE_Z = Block.box(6, 0, 0, 10, 16, 16);
    private static final int WAIT_TICKS = 60; // ~3 segundos dentro del portal

    public WardenPortalBlock(Properties props) {
        super(props);
        registerDefaultState(stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(AXIS); }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
        return state.getValue(AXIS) == Direction.Axis.X ? SHAPE_X : SHAPE_Z;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (rot == Rotation.COUNTERCLOCKWISE_90 || rot == Rotation.CLOCKWISE_90)
            return state.setValue(AXIS, state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
        return state;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state) { return ItemStack.EMPTY; }

    /** Si se rompe el marco o el portal vecino, el portal desaparece. */
    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis perpendicular = state.getValue(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
        if (dir.getAxis() != perpendicular) {
            boolean samePortal = neighbor.is(this) && neighbor.getValue(AXIS) == state.getValue(AXIS);
            if (!samePortal && !neighbor.is(ModBlocks.WARDEN_FRAME.get())) return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) return;
        if (!(entity instanceof ServerPlayer) && entity.getPersistentData().getInt("wd_cd") > 0) return;

        var tag = entity.getPersistentData();
        long now = level.getGameTime();
        int ticks = (now - tag.getLong("wd_last") > 4) ? 0 : tag.getInt("wd_t");
        ticks++;
        tag.putLong("wd_last", now);
        tag.putInt("wd_t", ticks);

        int needed = (entity instanceof ServerPlayer sp && sp.getAbilities().instabuild) ? 1 : WAIT_TICKS;
        if (ticks < needed) return;
        tag.putInt("wd_t", 0);

        var server = serverLevel.getServer();
        var target = serverLevel.dimension() == WardenDimension.WARDEN_LEVEL ? Level.OVERWORLD : WardenDimension.WARDEN_LEVEL;
        ServerLevel dest = server.getLevel(target);
        if (dest == null) return;
        entity.changeDimension(dest, new WardenTeleporter(pos, state.getValue(AXIS)));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        if (rand.nextInt(100) == 0)
            level.playLocalSound(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, SoundEvents.SCULK_CLICKING, SoundSource.BLOCKS, 0.5F, rand.nextFloat() * 0.4F + 0.8F, false);
        for (int i = 0; i < 2; i++) {
            double x = pos.getX() + rand.nextDouble(), y = pos.getY() + rand.nextDouble(), z = pos.getZ() + rand.nextDouble();
            level.addParticle(ParticleTypes.SCULK_SOUL, x, y, z, 0, 0.03, 0);
        }
    }
}
