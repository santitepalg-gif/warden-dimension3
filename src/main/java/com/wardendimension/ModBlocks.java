package com.wardendimension;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WardenDimension.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WardenDimension.MODID);
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, WardenDimension.MODID);

    private static BlockBehaviour.Properties stone(MapColor c) {
        return BlockBehaviour.Properties.of().mapColor(c).strength(3.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE);
    }

    private static <T extends Block> RegistryObject<T> reg(String name, Supplier<T> sup) {
        RegistryObject<T> ro = BLOCKS.register(name, sup);
        ITEMS.register(name, () -> new BlockItem(ro.get(), new Item.Properties()));
        return ro;
    }

    public static final RegistryObject<Block> SCULK_STONE = reg("sculk_stone", () -> new Block(stone(MapColor.COLOR_BLUE)));
    public static final RegistryObject<Block> ECHO_MOSS = reg("echo_moss_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).strength(1.5F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.MOSS)));
    public static final RegistryObject<Block> TWILIGHT_SCULK = reg("twilight_sculk", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(1.5F, 3.0F).requiresCorrectToolForDrops().sound(SoundType.SCULK)));
    public static final RegistryObject<Block> SCULK_BRICKS = reg("sculk_bricks", () -> new Block(stone(MapColor.COLOR_LIGHT_BLUE)));
    public static final RegistryObject<Block> GLOW_CRYSTAL = reg("glow_crystal_block", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.DIAMOND).strength(1.5F, 3.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.AMETHYST).lightLevel(s -> 12)));
    public static final RegistryObject<RotatedPillarBlock> RESONANT_LOG = reg("resonant_log", () -> new RotatedPillarBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(2.0F).sound(SoundType.WOOD)));
    public static final RegistryObject<LeavesBlock> RESONANT_LEAVES = reg("resonant_leaves", () -> new LeavesBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).strength(0.2F).randomTicks().sound(SoundType.AZALEA_LEAVES)
                    .noOcclusion().lightLevel(s -> 5)
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final RegistryObject<GlowcapBlock> GLOWCAP = BLOCKS.register("glowcap", () -> new GlowcapBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).noCollission().instabreak().sound(SoundType.SPORE_BLOSSOM)
                    .offsetType(BlockBehaviour.OffsetType.XZ).lightLevel(s -> 9).pushReaction(PushReaction.DESTROY)));
    static { ITEMS.register("glowcap", () -> new BlockItem(GLOWCAP.get(), new Item.Properties())); }

    public static final RegistryObject<Block> WARDEN_FRAME = reg("warden_frame", () -> new Block(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).strength(25.0F, 1200.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE_BRICKS).lightLevel(s -> 4)));
    public static final RegistryObject<LeavesBlock> TWILIGHT_LEAVES = reg("twilight_leaves", () -> new LeavesBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).strength(0.2F).randomTicks().sound(SoundType.AZALEA_LEAVES)
                    .noOcclusion().lightLevel(s -> 4)
                    .isValidSpawn((s, g, p, t) -> false).isSuffocating((s, g, p) -> false).isViewBlocking((s, g, p) -> false)));
    public static final RegistryObject<RotatedPillarBlock> WARDEN_BONE = reg("warden_bone", () -> new RotatedPillarBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.QUARTZ).strength(2.0F, 6.0F).requiresCorrectToolForDrops().sound(SoundType.BONE_BLOCK)));
    public static final RegistryObject<DanglingStrandBlock> DANGLING_STRAND = BLOCKS.register("dangling_strand", () -> new DanglingStrandBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).noCollission().instabreak().sound(SoundType.SCULK_VEIN)
                    .lightLevel(s -> 7).pushReaction(PushReaction.DESTROY)));
    static { ITEMS.register("dangling_strand", () -> new BlockItem(DANGLING_STRAND.get(), new Item.Properties())); }

    // El portal no tiene item
    public static final RegistryObject<WardenPortalBlock> WARDEN_PORTAL = BLOCKS.register("warden_portal", () -> new WardenPortalBlock(
            BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).noCollission().strength(-1.0F).sound(SoundType.SCULK_SENSOR)
                    .lightLevel(s -> 11).noLootTable().pushReaction(PushReaction.BLOCK)));

    public static final RegistryObject<CreativeModeTab> TAB = TABS.register("warden_dimension", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + WardenDimension.MODID))
            .icon(() -> new ItemStack(WARDEN_FRAME.get()))
            .displayItems((params, out) -> ITEMS.getEntries().forEach(i -> out.accept(i.get())))
            .build());
}
