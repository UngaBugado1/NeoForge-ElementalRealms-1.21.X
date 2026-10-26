
package net.matos.elementalrealms.entity.ai.rockspine;

import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public final class RockspineSmash {
    public static final int DURATION = 52;
    public static final int IMPACT_TICK = 20;
    public static final int COOLDOWN = 100;

    private static final double RADIUS = 4.5D;
    private static final double JUMP_STRENGTH = 0.65D;
    private static final double KNOCKBACK_STRENGTH = 1.6D;
    private static final int PARTICLE_COUNT = 32;

    private RockspineSmash() {}

    public static boolean tryStart(RockspineEntity rockspine) {
        if (rockspine.level().isClientSide()
                || rockspine.isSmashing()
                || rockspine.isDefending()
                || rockspine.getSmashCooldown() > 0) {
            return false;
        }

        rockspine.beginSmash(JUMP_STRENGTH, COOLDOWN);
        return true;
    }

    public static void tick(RockspineEntity rockspine) {
        if (rockspine.level().isClientSide()
                || !rockspine.isSmashing()) {
            return;
        }

        int ticks = rockspine.getSmashTicks() + 1;
        rockspine.setSmashTicks(ticks);

        rockspine.getNavigation().stop();
        rockspine.setDeltaMovement(
                0.0D,
                rockspine.getDeltaMovement().y,
                0.0D
        );

        if (ticks == IMPACT_TICK) {
            performImpact(rockspine);
        }

        if (ticks >= DURATION) {
            rockspine.endSmash();
        }
    }

    private static void performImpact(RockspineEntity rockspine) {
        if (!(rockspine.level() instanceof ServerLevel level)) {
            return;
        }

        double centerX = rockspine.getX();
        double centerZ = rockspine.getZ();
        double particleY = rockspine.getY() + 0.15D;

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double angle = Math.PI * 2.0D * i / PARTICLE_COUNT;
            double dx = Math.cos(angle);
            double dz = Math.sin(angle);

            level.sendParticles(
                    ParticleTypes.POOF,
                    centerX + dx * 0.5D,
                    particleY,
                    centerZ + dz * 0.5D,
                    0,
                    dx * 0.45D,
                    0.06D,
                    dz * 0.45D,
                    1.0D
            );

            rockspine.playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE);
        }

        AABB area = rockspine.getBoundingBox().inflate(RADIUS);

        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity != rockspine
                        && entity.isAlive()
                        && !(entity instanceof RockspineEntity)
        )) {
            double dx = target.getX() - centerX;
            double dz = target.getZ() - centerZ;

            if (dx * dx + dz * dz > RADIUS * RADIUS) {
                continue;
            }

            target.hurt(
                    rockspine.damageSources().mobAttack(rockspine),
                    5.0F
            );

            target.knockback(
                    KNOCKBACK_STRENGTH,
                    centerX - target.getX(),
                    centerZ - target.getZ()
            );

            target.hurtMarked = true;
        }
    }
}
