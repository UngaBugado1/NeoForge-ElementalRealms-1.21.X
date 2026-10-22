
package net.matos.elementalrealms.entity.custom;

import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.entity.ai.mossback_toad.MossbackToadPuff;
import net.matos.elementalrealms.sound.ModSounds;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;

public class MossbackToadEntity extends Animal {

    public static final int PHASE_NONE = MossbackToadPuff.PHASE_NONE;
    public static final int PHASE_PUFF = MossbackToadPuff.PHASE_PUFF;
    public static final int PHASE_SLEEPING = MossbackToadPuff.PHASE_SLEEPING;
    public static final int PHASE_AWAKE = MossbackToadPuff.PHASE_AWAKE;

    private static final EntityDataAccessor<Integer> ANIMATION_PHASE =
            SynchedEntityData.defineId(
                    MossbackToadEntity.class,
                    EntityDataSerializers.INT
            );

    private static final int PUFF_COOLDOWN_DURATION = 6000;

    private int puffCooldownTicks = 0;

    private final MossbackToadPuff puff = new MossbackToadPuff(this);

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState walkAnimationState = new AnimationState();

    public final AnimationState puffAnimationState = new AnimationState();
    public final AnimationState sleepingAnimationState = new AnimationState();
    public final AnimationState awakeAnimationState = new AnimationState();

    private float walkAnimationBlend = 0.0F;

    // Used only on the client to detect phase transitions.
    private int previousClientPhase = -1;

    public MossbackToadEntity(
            EntityType<? extends Animal> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_PHASE, PHASE_NONE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("MossbackToadAnimationPhase", this.getAnimationPhase());
        tag.putInt("MossbackToadPuffCooldown", this.puffCooldownTicks);

        this.puff.save(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        this.setPuffPhase(tag.getInt("MossbackToadAnimationPhase"));
        this.puffCooldownTicks = Math.max(
                0,
                tag.getInt("MossbackToadPuffCooldown")
        );

        this.puff.load(tag);
    }

    public int getAnimationPhase() {
        return this.entityData.get(ANIMATION_PHASE);
    }

    public void setPuffPhase(int phase) {
        this.entityData.set(ANIMATION_PHASE, phase);
    }

    public boolean hasPuffCooldown() {
        return this.puffCooldownTicks > 0;
    }

    private void startPuffCooldown() {
        this.puffCooldownTicks = PUFF_COOLDOWN_DURATION;
    }

    private void tickPuffCooldown() {
        if (this.puffCooldownTicks > 0) {
            this.puffCooldownTicks--;
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(
                0, new PhaseLockedGoal(this, new FloatGoal(this))
        );
        this.goalSelector.addGoal(
                1, new PhaseLockedGoal(this, new PanicGoal(this, 1.35D))
        );
        this.goalSelector.addGoal(
                2, new PhaseLockedGoal(this, new BreedGoal(this, 1.0D))
        );
        this.goalSelector.addGoal(
                3,
                new PhaseLockedGoal(
                        this,
                        new TemptGoal(
                                this,
                                1.25D,
                                stack -> stack.is(Items.GLOW_BERRIES),
                                false
                        )
                )
        );
        this.goalSelector.addGoal(
                4, new PhaseLockedGoal(this, new FollowParentGoal(this, 1.25D))
        );
        this.goalSelector.addGoal(
                5,
                new PhaseLockedGoal(
                        this,
                        new WaterAvoidingRandomStrollGoal(this, 1.0D)
                )
        );
        this.goalSelector.addGoal(
                6,
                new PhaseLockedGoal(
                        this,
                        new LookAtPlayerGoal(this, Player.class, 6.0F)
                )
        );
        this.goalSelector.addGoal(
                7, new PhaseLockedGoal(this, new RandomLookAroundGoal(this))
        );
    }

    private static class PhaseLockedGoal extends Goal {

        private final MossbackToadEntity toad;
        private final Goal delegate;

        private PhaseLockedGoal(MossbackToadEntity toad, Goal delegate) {
            this.toad = toad;
            this.delegate = delegate;
            this.setFlags(delegate.getFlags());
        }

        @Override
        public boolean canUse() {
            return toad.getAnimationPhase() == PHASE_NONE
                    && delegate.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return toad.getAnimationPhase() == PHASE_NONE
                    && delegate.canContinueToUse();
        }

        @Override
        public boolean isInterruptable() {
            return delegate.isInterruptable();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return delegate.requiresUpdateEveryTick();
        }

        @Override
        public void start() {
            delegate.start();
        }

        @Override
        public void stop() {
            delegate.stop();
        }

        @Override
        public void tick() {
            if (toad.getAnimationPhase() == PHASE_NONE) {
                delegate.tick();
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.POTATO);
    }

    @Override
    public AgeableMob getBreedOffspring(
            ServerLevel serverLevel,
            AgeableMob ageableMob
    ) {
        return ModEntities.MOSSBACK_TOAD.get().create(serverLevel);
    }

    public float getWalkAnimationBlend() {
        return this.walkAnimationBlend;
    }

    private void stopAllAnimationStates() {
        this.idleAnimationState.stop();
        this.walkAnimationState.stop();
        this.puffAnimationState.stop();
        this.sleepingAnimationState.stop();
        this.awakeAnimationState.stop();
    }

    public void ensureAnimationStatesForPhase() {
        if (!this.level().isClientSide()) {
            return;
        }

        int phase = this.getAnimationPhase();

        if (phase == this.previousClientPhase) {
            return;
        }

        stopAllAnimationStates();

        switch (phase) {
            case PHASE_PUFF ->
                    this.puffAnimationState.start(this.tickCount);

            case PHASE_SLEEPING ->
                    this.sleepingAnimationState.start(this.tickCount);

            case PHASE_AWAKE ->
                    this.awakeAnimationState.start(this.tickCount);

            default -> {
                this.idleAnimationState.start(this.tickCount);
                this.walkAnimationState.start(this.tickCount);
            }
        }

        this.previousClientPhase = phase;
    }

    private void updateAnimationStates() {
        ensureAnimationStatesForPhase();

        if (this.getAnimationPhase() != PHASE_NONE) {
            this.walkAnimationBlend = 0.0F;
            return;
        }

        boolean moving =
                this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6;

        float target = moving ? 1.0F : 0.0F;

        if (this.walkAnimationBlend < target) {
            this.walkAnimationBlend =
                    Math.min(target, this.walkAnimationBlend + 0.15F);
        } else if (this.walkAnimationBlend > target) {
            this.walkAnimationBlend =
                    Math.max(target, this.walkAnimationBlend - 0.15F);
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.is(MobEffects.POISON)
                ? false
                : super.canBeAffected(effect);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.updateAnimationStates();
        } else {
            this.tickPuffCooldown();

            int previousPhase = this.getAnimationPhase();
            this.puff.tick();

            // Start the cooldown only when the entire sequence finishes.
            if (previousPhase == PHASE_AWAKE
                    && this.getAnimationPhase() == PHASE_NONE) {
                this.startPuffCooldown();
            }
        }
    }

    @Override
    public Vec3 getPassengerAttachmentPoint(
            Entity passenger,
            EntityDimensions dimensions,
            float scale
    ) {
        return new Vec3(0.0D, this.getBbHeight() * 0.75D, 0.0D);
    }

    // SOUNDS

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getAnimationPhase() != PHASE_NONE) {
            return null;
        }

        return switch (this.random.nextInt(6)) {
            case 0 -> ModSounds.MOSSBACK_TOAD_AMBIENT1.get();
            case 1 -> ModSounds.MOSSBACK_TOAD_AMBIENT2.get();
            case 2 -> ModSounds.MOSSBACK_TOAD_AMBIENT3.get();
            case 3 -> ModSounds.MOSSBACK_TOAD_AMBIENT4.get();
            default -> ModSounds.MOSSBACK_TOAD_AMBIENT5.get();
        };
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (this.random.nextInt(4)) {
            case 0 -> ModSounds.MOSSBACK_TOAD_HURT1.get();
            case 1 -> ModSounds.MOSSBACK_TOAD_HURT2.get();
            case 2 -> ModSounds.MOSSBACK_TOAD_HURT3.get();
            default -> ModSounds.MOSSBACK_TOAD_HURT4.get();
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.random.nextBoolean()
                ? ModSounds.MOSSBACK_TOAD_DEATH1.get()
                : ModSounds.MOSSBACK_TOAD_DEATH2.get();
    }

    @Override
    protected float getSoundVolume() {
        return 1F;
    }


    @Override
    public InteractionResult mobInteract(
            Player player,
            InteractionHand hand
    ) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(Items.GLOW_BERRIES)) {

            // BABIES: consume glowberries to grow faster, like vanilla animals.
            if (this.isBaby()) {
                if (!this.level().isClientSide()) {
                    this.usePlayerItem(player, hand, stack);
                    this.ageUp((int) ((float) (-this.getAge() / 10) * 0.1F), true);
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }

            // ADULTS: refuse puffing during the five-minute cooldown.
            if (this.hasPuffCooldown()) {
                if (!this.level().isClientSide()
                        && this.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SMOKE,
                            this.getX(),
                            this.getY() + this.getBbHeight() * 0.7D,
                            this.getZ(),
                            4,
                            0.3D, 0.3D, 0.3D,
                            0.0D
                    );
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }

            // ADULTS: start the puff sequence.
            if (this.getAnimationPhase() == PHASE_NONE) {
                if (!this.level().isClientSide()) {
                    this.puff.start();

                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide());
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }

        return super.mobInteract(player, hand);
    }

}
