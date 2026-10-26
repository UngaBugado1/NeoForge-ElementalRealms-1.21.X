
package net.matos.elementalrealms.entity.ai.rockspine;

import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class RockspineAttackGoal extends Goal {
    private static final int ATTACK_WINDUP = 4;
    private static final int ATTACK_DURATION = 13;
    private static final int ATTACK_COOLDOWN = 2;
    private static final int MAX_CHASE_TICKS = 200;

    private static final double ATTACK_START_RANGE = 2.5D;
    private static final double ATTACK_HIT_RANGE = 3.5D;
    private static final double MAX_CHASE_DISTANCE = 16.0D;
    private static final float SMASH_CHANCE = 0.20F;

    private final RockspineEntity mob;
    private final double speed;

    private LivingEntity target;
    private int attackTicks;
    private int chaseTicks;
    private boolean attacking;
    private boolean damageDealt;

    public RockspineAttackGoal(RockspineEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    private boolean isValidTarget(LivingEntity target) {
        return target != null
                && target.isAlive()
                && (!(target instanceof Player player)
                || (!player.isCreative() && !player.isSpectator()));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();

        return !this.mob.isSmashing()
                && !this.mob.isDefending()
                && this.isValidTarget(target)
                && this.mob.distanceToSqr(target)
                <= MAX_CHASE_DISTANCE * MAX_CHASE_DISTANCE
                && this.mob.getAttackCooldown() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isSmashing() || this.mob.isDefending()) {
            return true;
        }

        if (!this.isValidTarget(this.target)
                || this.mob.getTarget() != this.target) {
            return false;
        }

        if (this.mob.distanceToSqr(this.target)
                > MAX_CHASE_DISTANCE * MAX_CHASE_DISTANCE) {
            return false;
        }

        if (this.attacking) {
            return this.attackTicks < ATTACK_DURATION;
        }

        return this.chaseTicks < MAX_CHASE_TICKS;
    }

    @Override
    public void start() {
        this.target = this.mob.getTarget();
        this.attackTicks = 0;
        this.chaseTicks = 0;
        this.attacking = false;
        this.damageDealt = false;
    }

    @Override
    public void tick() {
        if (this.mob.isDefending()) {
            this.mob.getNavigation().stop();
            this.attacking = false;
            this.attackTicks = 0;
            this.damageDealt = false;
            return;
        }

        if (this.mob.isSmashing()) {
            this.mob.getNavigation().stop();
            return;
        }

        if (!this.isValidTarget(this.target)) {
            return;
        }

        if (this.mob.distanceToSqr(this.target)
                > MAX_CHASE_DISTANCE * MAX_CHASE_DISTANCE) {
            this.mob.setTarget(null);
            this.mob.getNavigation().stop();
            return;
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);

        if (!this.attacking) {
            this.chaseTicks++;

            if (this.mob.distanceToSqr(this.target)
                    <= ATTACK_START_RANGE * ATTACK_START_RANGE) {
                this.mob.getNavigation().stop();

                if (this.mob.getRandom().nextFloat() < SMASH_CHANCE
                        && RockspineSmash.tryStart(this.mob)) {
                    return;
                }

                this.attacking = true;
                this.attackTicks = 0;
                this.damageDealt = false;
                this.mob.playAttackAnimation();
            } else {
                this.mob.getNavigation().moveTo(this.target, this.speed);
            }

            return;
        }

        this.mob.getNavigation().stop();
        this.attackTicks++;

        if (this.attackTicks == ATTACK_WINDUP && !this.damageDealt) {
            this.damageDealt = true;

            if (this.isValidTarget(this.target)
                    && this.mob.distanceToSqr(this.target)
                    <= ATTACK_HIT_RANGE * ATTACK_HIT_RANGE) {
                this.mob.doHurtTarget(this.target);
            }
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
        this.mob.setAttackCooldown(ATTACK_COOLDOWN);

        this.target = null;
        this.attacking = false;
        this.attackTicks = 0;
        this.chaseTicks = 0;
        this.damageDealt = false;
    }
}
