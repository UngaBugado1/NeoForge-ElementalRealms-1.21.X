
package net.matos.elementalrealms.entity.ai.rockspine;

import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class RockspineInvestigateGoal extends Goal {
    private final RockspineEntity mob;
    private final double speed;

    public RockspineInvestigateGoal(RockspineEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.getTarget() == null
                && this.mob.hasInvestigationTarget();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.getTarget() == null
                && this.mob.hasInvestigationTarget();
    }

    @Override
    public void tick() {
        Vec3 position = this.mob.getInvestigationPosition();
        if (position == null) return;

        this.mob.getLookControl().setLookAt(
                position.x, position.y, position.z);

        if (this.mob.position().distanceToSqr(position) > 2.25D) {
            this.mob.getNavigation().moveTo(
                    position.x, position.y, position.z, this.speed);
        } else {
            this.mob.getNavigation().stop();
        }
    }

    @Override
    public void stop() {
        this.mob.getNavigation().stop();
    }
}
