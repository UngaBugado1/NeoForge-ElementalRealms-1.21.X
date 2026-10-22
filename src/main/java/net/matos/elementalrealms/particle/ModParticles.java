package net.matos.elementalrealms.particle;

import net.matos.elementalrealms.ElementalRealms;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, ElementalRealms.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MOSS_POISON =
            PARTICLE_TYPES.register("moss_poison", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> GOLDEN_BLOOM =
            PARTICLE_TYPES.register("golden_bloom", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SLEEPING =
            PARTICLE_TYPES.register("sleeping", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {PARTICLE_TYPES.register(eventBus);
    }
}