
package net.matos.elementalrealms.entity.ai.mossback_toad;

import net.matos.elementalrealms.effect.ModEffects;
import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.matos.elementalrealms.particle.ModParticles;
import net.matos.elementalrealms.sound.ModSounds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

public class MossbackToadPuff {

    public static final int PHASE_NONE = 0;
    public static final int PHASE_PUFF = 1;
    public static final int PHASE_SLEEPING = 2;
    public static final int PHASE_AWAKE = 3;

    private static final int PUFF_DURATION = 126;
    private static final int SLEEP_DURATION = 6000;
    private static final int AWAKE_DURATION = 35;

    private static final int EATING_PARTICLE_DURATION = 30;
    private static final int PARTICLE_RELEASE_TICK = 70;
    private static final int SLEEPING_PARTICLE_INTERVAL = 20;
    private static final int EATING_SOUND_INTERVAL = 5;

    private static final double RADIUS = 5.0D;

    private final MossbackToadEntity toad;
    private int animationTicks;
    private int phaseElapsedTicks;
    private boolean particlesReleased;

    public MossbackToadPuff(MossbackToadEntity toad) {
        this.toad = toad;
    }

    public void start() {
        if (toad.getAnimationPhase() != PHASE_NONE) {
            return;
        }

        animationTicks = PUFF_DURATION;
        phaseElapsedTicks = 0;
        particlesReleased = false;

        toad.setPuffPhase(PHASE_PUFF);

        toad.level().playSound(
                null,
                toad.getX(),
                toad.getY(),
                toad.getZ(),
                SoundEvents.GENERIC_EAT,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );

        toad.getNavigation().stop();
    }

    public void tick() {
        int phase = toad.getAnimationPhase();

        if (phase == PHASE_NONE) {
            return;
        }

        toad.getNavigation().stop();

        toad.setDeltaMovement(
                0.0D,
                toad.getDeltaMovement().y,
                0.0D
        );

        if (phase == PHASE_PUFF) {
            if (phaseElapsedTicks < EATING_PARTICLE_DURATION) {
                spawnEatingParticles();

                if (phaseElapsedTicks % EATING_SOUND_INTERVAL == 0) {
                    toad.playSound(
                            SoundEvents.GENERIC_EAT,
                            0.8F,
                            0.9F + toad.getRandom().nextFloat() * 0.2F
                    );
                }
            }

            if (!particlesReleased
                    && phaseElapsedTicks >= PARTICLE_RELEASE_TICK) {
                releaseCloud();
                particlesReleased = true;
            }
        }

        if (phase == PHASE_SLEEPING) {
            playRandomSleepingSound();

            if (phaseElapsedTicks % SLEEPING_PARTICLE_INTERVAL == 0) {
                spawnSleepingParticles();
            }
        }

        phaseElapsedTicks++;
        animationTicks--;

        if (animationTicks > 0) {
            return;
        }

        switch (phase) {
            case PHASE_PUFF -> {
                animationTicks = SLEEP_DURATION;
                phaseElapsedTicks = 0;
                toad.setPuffPhase(PHASE_SLEEPING);
            }

            case PHASE_SLEEPING -> {
                animationTicks = AWAKE_DURATION;
                phaseElapsedTicks = 0;
                toad.setPuffPhase(PHASE_AWAKE);
            }

            case PHASE_AWAKE -> {
                animationTicks = 0;
                phaseElapsedTicks = 0;
                toad.setPuffPhase(PHASE_NONE);
            }
        }
    }

    public void save(CompoundTag tag) {
        tag.putInt("MossbackToadAnimationTicks", animationTicks);
        tag.putInt("MossbackToadPhaseElapsedTicks", phaseElapsedTicks);
        tag.putBoolean("MossbackToadParticlesReleased", particlesReleased);
    }

    public void load(CompoundTag tag) {
        animationTicks = tag.getInt("MossbackToadAnimationTicks");
        phaseElapsedTicks = tag.getInt("MossbackToadPhaseElapsedTicks");
        particlesReleased = tag.getBoolean("MossbackToadParticlesReleased");
    }

    private void spawnEatingParticles() {
        if (!(toad.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        double yaw = Math.toRadians(toad.getYRot());
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);

        double mouthX = toad.getX() + forwardX * 0.9D;
        double mouthY = toad.getY() + toad.getBbHeight() * 0.8D + 0.4D - 0.45D;
        double mouthZ = toad.getZ() + forwardZ * 0.9D;

        serverLevel.sendParticles(
                new ItemParticleOption(
                        ParticleTypes.ITEM,
                        new ItemStack(Items.GLOW_BERRIES)
                ),
                mouthX, mouthY, mouthZ,
                3,
                0.04D, 0.04D, 0.04D,
                0.05D
        );
    }

    private void spawnSleepingParticles() {
        if (!(toad.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        double yaw = Math.toRadians(toad.getYRot());
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);

        double particleX = toad.getX() + forwardX * 1.0D;
        double particleY = toad.getY() + toad.getBbHeight() - 0.1D;
        double particleZ = toad.getZ() + forwardZ * 1.0D;

        serverLevel.sendParticles(
                ModParticles.SLEEPING.get(),
                particleX, particleY, particleZ,
                1,
                0.08D, 0.025D, 0.08D,
                0.04D
        );
    }

    private void releaseCloud() {
        if (!(toad.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        double yaw = Math.toRadians(toad.getYRot());
        double forwardX = -Math.sin(yaw);
        double forwardZ = Math.cos(yaw);

        double cloudX = toad.getX() + forwardX * 0.5D;
        double cloudY = toad.getY() + toad.getBbHeight() * 0.5D;
        double cloudZ = toad.getZ() + forwardZ * 0.5D;

        serverLevel.sendParticles(
                ModParticles.GOLDEN_BLOOM.get(),
                cloudX, cloudY, cloudZ,
                150,
                1.0D, 0.1D, 1.0D,
                0.025D
        );

        toad.playSound(SoundEvents.BREEZE_LAND);

        AABB area = toad.getBoundingBox().inflate(RADIUS);

        for (LivingEntity target : serverLevel.getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity != toad
                        && entity.isAlive()
                        && entity.distanceToSqr(toad) <= RADIUS * RADIUS
        )) {
            target.addEffect(new MobEffectInstance(
                    ModEffects.GOLDEN_BLOOM_EFFECT,
                    600,
                    0
            ));
        }
    }

    private void playRandomSleepingSound() {
        if (toad.getRandom().nextInt(100) != 0) {
            return;
        }

        SoundEvent sound = switch (toad.getRandom().nextInt(3)) {
            case 0 -> ModSounds.MOSSBACK_TOAD_SLEEP1.get();
            case 1 -> ModSounds.MOSSBACK_TOAD_SLEEP2.get();
            default -> ModSounds.MOSSBACK_TOAD_SLEEP3.get();
        };

        toad.playSound(
                sound,
                1F,
                0.9F + toad.getRandom().nextFloat() * 0.2F
        );
    }
}
