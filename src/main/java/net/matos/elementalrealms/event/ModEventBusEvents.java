package net.matos.elementalrealms.event;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.entity.client.*;
import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.matos.elementalrealms.entity.custom.TectoraxEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = ElementalRealms.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MOSSHROOM, MosshroomModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MOSSBACK_TOAD, MossbackToadModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TECTORAX, TectoraxModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ROCKSPINE, RockspineModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.MOSSHROOM.get(), MosshroomEntity.createAttributes().build());
        event.put(ModEntities.MOSSBACK_TOAD.get(), MossbackToadEntity.createAttributes().build());
        event.put(ModEntities.TECTORAX.get(), TectoraxEntity.createAttributes().build());
        event.put(ModEntities.ROCKSPINE.get(), RockspineEntity.createAttributes().build());
    }
}
