package com.wardendimension;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/** Activa el portal: clic derecho con un Fragmento de Eco sobre el Marco del Warden. */
public class PortalEvents {

    @SubscribeEvent
    public void onUse(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        if (event.getHand() != InteractionHand.MAIN_HAND) return;
        if (!event.getItemStack().is(Items.ECHO_SHARD)) return;
        BlockPos clicked = event.getPos();
        if (!level.getBlockState(clicked).is(ModBlocks.WARDEN_FRAME.get())) return;

        if (level.isClientSide) return;
        for (Direction d : Direction.values()) {
            BlockPos start = clicked.relative(d);
            if (!level.getBlockState(start).isAir()) continue;
            for (Direction.Axis axis : new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}) {
                if (PortalFrame.tryLight(level, start, axis)) {
                    level.playSound(null, clicked, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 0.6F, 1.4F);
                    if (!event.getEntity().getAbilities().instabuild) event.getItemStack().shrink(1);
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
