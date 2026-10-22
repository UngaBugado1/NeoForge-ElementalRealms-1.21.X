package net.matos.elementalrealms.effect;

import net.matos.elementalrealms.ElementalRealms;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ElementalRealms.MOD_ID);


    public static final Holder<MobEffect> GOLDEN_BLOOM_EFFECT = MOB_EFFECTS.register("golden_bloom",
            () -> new GoldenBloomEffect(MobEffectCategory.BENEFICIAL, 0xffbd2a));



    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
