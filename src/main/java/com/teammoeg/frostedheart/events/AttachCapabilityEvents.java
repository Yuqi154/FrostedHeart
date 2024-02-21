/*
 * Copyright (c) 2022-2024 TeamMoeg
 *
 * This file is part of Frosted Heart.
 *
 * Frosted Heart is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Frosted Heart is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Frosted Heart. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.teammoeg.frostedheart.events;

import com.teammoeg.frostedheart.FHCapabilities;
import com.teammoeg.frostedheart.FHMain;
import com.teammoeg.frostedheart.climate.WorldClimate;
import com.teammoeg.frostedheart.content.foods.DailyKitchen.WantedFoodCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FHMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AttachCapabilityEvents {

    @SubscribeEvent
    public static void attachToChunk(AttachCapabilitiesEvent<Chunk> event) {
        if (!event.getObject().isEmpty()) {
            World world = event.getObject().getWorld();
            if (!world.isRemote) {
                event.addCapability(new ResourceLocation(FHMain.MODID, "chunk_data"), FHCapabilities.CHUNK_HEAT.create());
            }
        }
    }

    @SubscribeEvent
    public static void attachToPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayerEntity) {//server-side only capabilities
            ServerPlayerEntity player = (ServerPlayerEntity) event.getObject();
            if (!(player instanceof FakePlayer)) {
                event.addCapability(new ResourceLocation(FHMain.MODID, "death_inventory"), FHCapabilities.DEATH_INV.create());
                event.addCapability(new ResourceLocation(FHMain.MODID, "scenario"       ), FHCapabilities.SCENARIO.create());
                event.addCapability(new ResourceLocation(FHMain.MODID, "wanted_food"    ), new WantedFoodCapabilityProvider());
            }
        }
        //Common capabilities
        event.addCapability(new ResourceLocation(FHMain.MODID, "temperature"), FHCapabilities.PLAYER_TEMP.create());
        event.addCapability(new ResourceLocation(FHMain.MODID, "rsenergy"   ), FHCapabilities.ENERGY.create());

    }

    @SubscribeEvent
    public static void attachToWorld(AttachCapabilitiesEvent<World> event) {
        // only attach to dimension with skylight (i.e. overworld)
        if (!event.getObject().getDimensionType().doesFixedTimeExist()) {
            event.addCapability(WorldClimate.ID,FHCapabilities.CLIMATE_DATA.create());
        }
    }

}
