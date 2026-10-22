package net.matos.elementalrealms.entity;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.matos.elementalrealms.entity.custom.TectoraxEntity;
import net.matos.elementalrealms.entity.projectile.FungusSporesProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ElementalRealms.MOD_ID);


    public static final Supplier<EntityType<MosshroomEntity>> MOSSHROOM =
            ENTITY_TYPES.register("mosshroom", () -> EntityType.Builder.of(MosshroomEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 0.85f).build("mosshroom"));

    public static final Supplier<EntityType<MossbackToadEntity>> MOSSBACK_TOAD =
            ENTITY_TYPES.register("mossback_toad", () -> EntityType.Builder.of(MossbackToadEntity::new, MobCategory.CREATURE)
                    .sized(1.45f, 1.2f).build("mossback_toad"));

    public static final Supplier<EntityType<TectoraxEntity>> TECTORAX =
            ENTITY_TYPES.register("tectorax", () -> EntityType.Builder.of(TectoraxEntity::new, MobCategory.CREATURE)
                    .sized(2.8f, 2.1f).build("tectorax"));

    public static final DeferredHolder<EntityType<?>, EntityType<FungusSporesProjectile>> FUNGUS_SPORES =
            ENTITY_TYPES.register("fungus_spores",
                    () -> EntityType.Builder.<FungusSporesProjectile>of(
                                    FungusSporesProjectile::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("fungus_spores"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
