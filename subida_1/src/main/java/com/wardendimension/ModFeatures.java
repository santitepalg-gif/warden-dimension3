package com.wardendimension;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, WardenDimension.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SPIRE_TREE =
            FEATURES.register("spire_tree", () -> new SpireTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> DUSK_WEEPER_TREE =
            FEATURES.register("dusk_weeper_tree", () -> new DuskWeeperFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> RIB_TREE =
            FEATURES.register("rib_tree", () -> new RibTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> CRYSTAL_CLUSTER =
            FEATURES.register("crystal_cluster", () -> new CrystalClusterFeature(NoneFeatureConfiguration.CODEC));
}
