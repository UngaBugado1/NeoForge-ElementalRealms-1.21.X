
package net.matos.elementalrealms.entity.ai.mosshroom;

import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.matos.elementalrealms.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class MosshroomDefense {

    public static final int DURATION = 60;
    private static final double RADIUS = 2D;

    private MosshroomDefense() {
    }

    public static boolean tryActivate(MosshroomEntity mosshroom) {
        if (mosshroom.level().isClientSide
                || mosshroom.isDefending()
                || mosshroom.getRandom().nextFloat() >= 0.5F) {
            return false;
        }

        mosshroom.startDefense();
        return true;
    }

    public static void releaseCloud(MosshroomEntity mosshroom) {
        if (!(mosshroom.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                ModParticles.MOSS_POISON.get(),
                mosshroom.getX(),
                mosshroom.getY() + 0.4D,
                mosshroom.getZ(),
                35,
                0.5D, 0D, 0.5D,
                0.02D
        );

        mosshroom.playSound(SoundEvents.BREEZE_LAND);



        AABB area = mosshroom.getBoundingBox().inflate(RADIUS);

        for (LivingEntity target : serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity != mosshroom
                        && entity.isAlive()
                        && entity.distanceToSqr(mosshroom)
                        <= RADIUS * RADIUS)) {

            target.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    600,
                    1
            ));
        }
    }
}
