package net.matos.elementalrealms.sound;

import net.matos.elementalrealms.ElementalRealms;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ElementalRealms.MOD_ID);

    // Mosshroom
    public static final Supplier<SoundEvent> MOSSHROOM_AMBIENT1 = registerSoundEvent("mosshroom_ambient1");
    public static final Supplier<SoundEvent> MOSSHROOM_AMBIENT2 = registerSoundEvent("mosshroom_ambient2");
    public static final Supplier<SoundEvent> MOSSHROOM_AMBIENT3 = registerSoundEvent("mosshroom_ambient3");
    public static final Supplier<SoundEvent> MOSSHROOM_AMBIENT4 = registerSoundEvent("mosshroom_ambient4");
    public static final Supplier<SoundEvent> MOSSHROOM_AMBIENT5 = registerSoundEvent("mosshroom_ambient5");
    public static final Supplier<SoundEvent> MOSSHROOM_HURT1 = registerSoundEvent("mosshroom_hurt1");
    public static final Supplier<SoundEvent> MOSSHROOM_HURT2 = registerSoundEvent("mosshroom_hurt2");
    public static final Supplier<SoundEvent> MOSSHROOM_HURT3 = registerSoundEvent("mosshroom_hurt3");
    public static final Supplier<SoundEvent> MOSSHROOM_HURT4 = registerSoundEvent("mosshroom_hurt4");
    public static final Supplier<SoundEvent> MOSSHROOM_DEATH1 = registerSoundEvent("mosshroom_death1");
    public static final Supplier<SoundEvent> MOSSHROOM_DEATH2 = registerSoundEvent("mosshroom_death2");


    // Mossback Toad
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_AMBIENT1 = registerSoundEvent("mossback_toad_ambient1");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_AMBIENT2 = registerSoundEvent("mossback_toad_ambient2");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_AMBIENT3 = registerSoundEvent("mossback_toad_ambient3");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_AMBIENT4 = registerSoundEvent("mossback_toad_ambient4");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_AMBIENT5 = registerSoundEvent("mossback_toad_ambient5");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_HURT1 = registerSoundEvent("mossback_toad_hurt1");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_HURT2 = registerSoundEvent("mossback_toad_hurt2");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_HURT3 = registerSoundEvent("mossback_toad_hurt3");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_HURT4 = registerSoundEvent("mossback_toad_hurt4");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_DEATH1 = registerSoundEvent("mossback_toad_death1");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_DEATH2 = registerSoundEvent("mossback_toad_death2");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_SLEEP1 = registerSoundEvent("mossback_toad_sleep1");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_SLEEP2 = registerSoundEvent("mossback_toad_sleep2");
    public static final Supplier<SoundEvent> MOSSBACK_TOAD_SLEEP3 = registerSoundEvent("mossback_toad_sleep3");



    private static Supplier<SoundEvent> registerSoundEvent(String name)
    {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ElementalRealms.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus)
    {
        SOUND_EVENTS.register(eventBus);
    }
}