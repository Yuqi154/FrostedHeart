package com.teammoeg.frostedheart;

import com.teammoeg.frostedheart.util.FHLogger;
import com.teammoeg.frostedheart.world.FHFeatures;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.teammoeg.frostedheart.content.FHContent.*;

@Mod.EventBusSubscriber(modid = FHMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FHRegistryEvents {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : registeredFHBlocks) {
            try {
                event.getRegistry().register(block);
            } catch (Throwable e) {
                FHLogger.error("Failed to register a block. ({})", block);
                throw e;
            }
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : registeredFHItems) {
            try {
                event.getRegistry().register(item);
            } catch (Throwable e) {
                FHLogger.error("Failed to register an item. ({}, {})", item, item.getRegistryName());
                throw e;
            }
        }
    }

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        for (Fluid fluid : registeredFHFluids) {
            try {
                event.getRegistry().register(fluid);
            } catch (Throwable e) {
                FHLogger.error("Failed to register a fluid. ({}, {})", fluid, fluid.getRegistryName());
                throw e;
            }
        }
    }

    @SubscribeEvent
    public static void onFeatureRegistry(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(FHFeatures.FHORE.setRegistryName(FHMain.MODID, "fhore"));
    }
}