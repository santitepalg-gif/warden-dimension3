package com.wardendimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WardenDimension.MODID)
public class WardenDimension {
    public static final String MODID = "warden_dimension";
    public static final ResourceKey<Level> WARDEN_LEVEL =
            ResourceKey.create(Registries.DIMENSION, new ResourceLocation(MODID, "warden_dimension"));

    public WardenDimension() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(bus);
        ModBlocks.ITEMS.register(bus);
        ModBlocks.TABS.register(bus);
        ModFeatures.FEATURES.register(bus);
        MinecraftForge.EVENT_BUS.register(new PortalEvents());
    }
}
