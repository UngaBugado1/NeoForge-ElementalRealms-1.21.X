package net.matos.elementalrealms.entity.ai.rockspine;

import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public final class RockspineDefense {
    public static final int DURATION = 95;

    private static final float HEALTH_THRESHOLD = 4.0F;
    private static final float CHANCE = 0.50F;

    private RockspineDefense() {}

    public static boolean tryActivate(RockspineEntity rockspine) {
        if (rockspine.level().isClientSide()
                || !rockspine.isAlive()
                || rockspine.isDefending()
                || rockspine.isSmashing()
                || rockspine.getHealth() >= HEALTH_THRESHOLD) {
            return false;
        }

        if (rockspine.getRandom().nextFloat() >= CHANCE) {
            return false;
        }

        rockspine.beginDefense();

        rockspine.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);

        rockspine.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                DURATION,
                1,
                false,
                true
        ));

        return true;
    }

    public static void tick(RockspineEntity rockspine) {
        if (rockspine.level().isClientSide() || !rockspine.isDefending()) {
            return;
        }

        rockspine.getNavigation().stop();
        rockspine.setDeltaMovement(
                0.0D,
                rockspine.getDeltaMovement().y,
                0.0D
        );

        rockspine.setDefenseTicks(rockspine.getDefenseTicks() + 1);

        if (rockspine.getDefenseTicks() >= DURATION) {
            rockspine.endDefense();
        }
    }
}
