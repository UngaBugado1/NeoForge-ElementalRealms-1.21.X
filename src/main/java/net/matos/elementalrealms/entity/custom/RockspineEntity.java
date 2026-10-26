
package net.matos.elementalrealms.entity.custom;

import net.matos.elementalrealms.entity.ai.rockspine.RockspineAttackGoal;
import net.matos.elementalrealms.entity.ai.rockspine.RockspineDefense;
import net.matos.elementalrealms.entity.ai.rockspine.RockspineInvestigateGoal;
import net.matos.elementalrealms.entity.ai.rockspine.RockspineSmash;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class RockspineEntity extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState smashAnimationState = new AnimationState();
    public final AnimationState defenseAnimationState = new AnimationState();

    private int idleAnimationTimeout;
    private int attackAnimationTimeout;
    private int smashAnimationTimeout;
    private int defenseAnimationTimeout;

    private int attackCooldown;
    private int smashCooldown;
    private int smashTicks;
    private int defenseTicks;

    private boolean smashing;
    private boolean defending;

    private Vec3 investigationPosition;
    private long investigationExpiry;

    public RockspineEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RockspineAttackGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RockspineInvestigateGoal(this, 0.85D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 3.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    public void hearSound(Vec3 position, Player source, double radius) {
        if (this.level().isClientSide() || this.getTarget() != null) return;

        if (source != null && (!source.isAlive()
                || source.isSpectator()
                || source.isCreative())) {
            return;
        }

        if (this.distanceToSqr(position) > radius * radius) return;

        this.investigationPosition = position;
        this.investigationExpiry = this.level().getGameTime() + 160L;
    }

    public boolean hasInvestigationTarget() {
        if (this.investigationPosition == null) return false;

        if (this.level().getGameTime() >= this.investigationExpiry) {
            this.clearInvestigation();
            return false;
        }

        return true;
    }

    public Vec3 getInvestigationPosition() {
        return this.hasInvestigationTarget() ? this.investigationPosition : null;
    }

    public void clearInvestigation() {
        this.investigationPosition = null;
        this.investigationExpiry = 0L;
    }

    private void detectNearbyPlayers() {
        if (this.tickCount % 5 != 0) return;

        LivingEntity currentTarget = this.getTarget();

        if (currentTarget != null) {
            if (!currentTarget.isAlive()
                    || (currentTarget instanceof Player player
                    && (player.isCreative() || player.isSpectator()))
                    || this.distanceToSqr(currentTarget) > 16.0D * 16.0D) {
                this.setTarget(null);
            } else {
                return;
            }
        }

        AABB area = this.getBoundingBox().inflate(4D);

        for (Player player : this.level().getEntitiesOfClass(Player.class, area)) {
            if (!player.isAlive() || player.isSpectator() || player.isCreative()) {
                continue;
            }

            if (this.distanceToSqr(player) <= 4D * 4D) {
                this.clearInvestigation();
                this.setTarget(player);
                return;
            }
        }
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.defending) {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
            return false;
        }

        boolean hurt = super.hurt(source, amount);

        if (hurt && !this.level().isClientSide()
                && source.getEntity() instanceof Player player
                && player.isAlive()
                && !player.isCreative()
                && !player.isSpectator()) {
            this.setTarget(player);
            this.clearInvestigation();
        }

        if (hurt && !this.level().isClientSide()
                && this.isAlive()
                && this.getHealth() < 4.0F) {
            RockspineDefense.tryActivate(this);
        }

        return hurt;
    }


    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void setAttackCooldown(int ticks) {
        this.attackCooldown = ticks;
    }

    public boolean isSmashing() {
        return this.smashing;
    }

    public int getSmashTicks() {
        return this.smashTicks;
    }

    public void setSmashTicks(int ticks) {
        this.smashTicks = ticks;
    }

    public int getSmashCooldown() {
        return this.smashCooldown;
    }

    public boolean isDefending() {
        return this.defending;
    }

    public int getDefenseTicks() {
        return this.defenseTicks;
    }

    public void setDefenseTicks(int ticks) {
        this.defenseTicks = ticks;
    }

    public void beginDefense() {
        if (this.level().isClientSide()
                || this.defending
                || this.smashing) {
            return;
        }

        this.defending = true;
        this.defenseTicks = 0;

        this.getNavigation().stop();
        this.setDeltaMovement(
                0.0D,
                this.getDeltaMovement().y,
                0.0D
        );

        this.level().broadcastEntityEvent(this, (byte) 6);
    }

    public void endDefense() {
        this.defending = false;
        this.defenseTicks = 0;
    }

    public void beginSmash(double jumpStrength, int cooldown) {
        if (this.level().isClientSide()
                || this.smashing
                || this.defending) {
            return;
        }

        this.smashing = true;
        this.smashTicks = 0;
        this.smashCooldown = cooldown;

        this.getNavigation().stop();
        this.setDeltaMovement(0.0D, jumpStrength, 0.0D);
        this.hasImpulse = true;

        this.level().broadcastEntityEvent(this, (byte) 5);
    }

    public void endSmash() {
        this.smashing = false;
        this.smashTicks = 0;
    }

    public void playAttackAnimation() {
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte) 4);
        }
    }

    private void setupAnimationStates() {
        if (--this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 80;
            this.idleAnimationState.start(this.tickCount);
        }

        if (this.attackAnimationTimeout > 0
                && --this.attackAnimationTimeout == 0) {
            this.attackAnimationState.stop();
        }

        if (this.smashAnimationTimeout > 0
                && --this.smashAnimationTimeout == 0) {
            this.smashAnimationState.stop();
        }

        if (this.defenseAnimationTimeout > 0
                && --this.defenseAnimationTimeout == 0) {
            this.defenseAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (this.attackCooldown > 0) this.attackCooldown--;
            if (this.smashCooldown > 0) this.smashCooldown--;

            this.detectNearbyPlayers();
            this.hasInvestigationTarget();

            RockspineSmash.tick(this);
            RockspineDefense.tick(this);
        } else {
            this.setupAnimationStates();
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 4) {
            this.attackAnimationState.start(this.tickCount);
            this.attackAnimationTimeout = 13;
        } else if (id == 5) {
            this.attackAnimationState.stop();
            this.defenseAnimationState.stop();
            this.smashAnimationState.start(this.tickCount);
            this.smashAnimationTimeout = RockspineSmash.DURATION;
        } else if (id == 6) {
            this.attackAnimationState.stop();
            this.smashAnimationState.stop();
            this.defenseAnimationState.start(this.tickCount);
            this.defenseAnimationTimeout = RockspineDefense.DURATION;
        } else {
            super.handleEntityEvent(id);
        }
    }
}
