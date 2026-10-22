package net.matos.elementalrealms.particle;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.particle.custom.GoldenBloomParticle;
import net.matos.elementalrealms.particle.custom.MossPoisonParticle;
import net.matos.elementalrealms.particle.custom.SleepingParticle;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(
        modid = ElementalRealms.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
    public class ModParticleProvider {

    @SubscribeEvent
    public static void registerParticleProviders(
            RegisterParticleProvidersEvent event
    ) {
        event.registerSpriteSet(
                ModParticles.MOSS_POISON.get(),
                MossPoisonParticle.Provider::new
        );

        event.registerSpriteSet(
                ModParticles.GOLDEN_BLOOM.get(),
                GoldenBloomParticle.Provider::new
        );

        event.registerSpriteSet(
                ModParticles.SLEEPING.get(),
                SleepingParticle.Provider::new
        );

    }
}
