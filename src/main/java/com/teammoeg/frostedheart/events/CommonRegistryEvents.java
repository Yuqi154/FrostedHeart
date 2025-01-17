/*
 * Copyright (c) 2021-2024 TeamMoeg
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

import com.teammoeg.frostedheart.FHAttributes;
import com.teammoeg.frostedheart.FHMain;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FHMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonRegistryEvents {

    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, FHAttributes.ENV_TEMPERATURE.get());
		event.add(EntityType.PLAYER, FHAttributes.EFFECTIVE_TEMPERATURE.get());
		event.add(EntityType.PLAYER, FHAttributes.INSULATION.get());
		event.add(EntityType.PLAYER, FHAttributes.WIND_PROOF.get());
		event.add(EntityType.PLAYER, FHAttributes.HEAT_PROOF.get());
	}/*
    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();
        registry.register(new RemoveLootModifier.Serializer().setRegistryName(new ResourceLocation(FHMain.MODID, "remove_loot")));
        registry.register(new ReplaceLootModifier.Serializer().setRegistryName(new ResourceLocation(FHMain.MODID, "replace_loot")));
        registry.register(new DechantLootModifier.Serializer().setRegistryName(new ResourceLocation(FHMain.MODID, "dechant")));
        registry.register(new ApplyDamageLootModifier.Serializer().setRegistryName(new ResourceLocation(FHMain.MODID, "damage")));
        registry.register(new AddLootModifier.Serializer().setRegistryName(new ResourceLocation(FHMain.MODID, "add_loot")));
        TemperatureLootCondition.TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(FHMain.MODID, "temperature"), new LootItemConditionType(new TemperatureLootCondition.Serializer()));
        TagLootCondition.TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(FHMain.MODID, "block_tag"), new LootItemConditionType(new TagLootCondition.Serializer()));
        TreasureLootCondition.TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(FHMain.MODID, "treasure"), new LootItemConditionType(new TreasureLootCondition.Serializer()));
        ModLootCondition.TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(FHMain.MODID, "modids"), new LootItemConditionType(new ModLootCondition.Serializer()));
        BlizzardDamageCondition.TYPE = Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(FHMain.MODID, "blizzard_damage"), new LootItemConditionType(new BlizzardDamageCondition.Serializer()));
    }*/
}
