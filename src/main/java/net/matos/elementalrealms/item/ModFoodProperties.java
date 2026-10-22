package net.matos.elementalrealms.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoodProperties {
    public static final FoodProperties MOSSHROOM_CAP = new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 600), 1f)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 600), 0.65f)
            .build();


    public static final FoodProperties RAW_ANCIENT_CHOP = new FoodProperties.Builder()
            .nutrition(2).
            saturationModifier(0.25f)
            .build();

    public static final FoodProperties COOKED_ANCIENT_CHOP = new FoodProperties.Builder()
            .nutrition(4).
            saturationModifier(0.5f)
            .build();
}