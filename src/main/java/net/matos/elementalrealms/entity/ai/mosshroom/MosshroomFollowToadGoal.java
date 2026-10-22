package net.matos.elementalrealms.entity.ai.mosshroom;

import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.Comparator;
import java.util.EnumSet;

public class MosshroomFollowToadGoal extends Goal {

    private static final double SEARCH_RADIUS = 8.0D;
    private static final double FOLLOW_DISTANCE = 6.0D;
    private static final double FORGET_DISTANCE = 10.0D;
    private static final double SPEED = 1.0D;

    private final MosshroomEntity mosshroom;
    private MossbackToadEntity toad;

    public MosshroomFollowToadGoal(MosshroomEntity mosshroom) {
        this.mosshroom = mosshroom;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (mosshroom.isDefending()) {
            return false;
        }

        // Forget the toad if it dies or gets too far away.
        if (toad != null &&
                (!toad.isAlive()
                        || mosshroom.distanceToSqr(toad)
                        > FORGET_DISTANCE * FORGET_DISTANCE)) {
            toad = null;
        }

        // Find the nearest toad if we don't have a friend yet.
        if (toad == null) {
            toad = mosshroom.level()
                    .getEntitiesOfClass(
                            MossbackToadEntity.class,
                            mosshroom.getBoundingBox().inflate(SEARCH_RADIUS),
                            entity -> entity.isAlive()
                    )
                    .stream()
                    .min(Comparator.comparingDouble(
                            mosshroom::distanceToSqr
                    ))
                    .orElse(null);
        }

        // Start following only when farther than 8 blocks.
        return toad != null
                && mosshroom.distanceToSqr(toad)
                > FOLLOW_DISTANCE * FOLLOW_DISTANCE;
    }

    @Override
    public boolean canContinueToUse() {
        if (mosshroom.isDefending() || toad == null || !toad.isAlive()) {
            return false;
        }

        double distanceSqr = mosshroom.distanceToSqr(toad);

        // Stop following when close enough, or forget a distant toad.
        return distanceSqr > FOLLOW_DISTANCE * FOLLOW_DISTANCE
                && distanceSqr <= FORGET_DISTANCE * FORGET_DISTANCE;
    }

    @Override
    public void start() {
        if (toad != null) {
            mosshroom.getLookControl().setLookAt(toad);
        }
    }

    @Override
    public void tick() {
        if (toad == null) {
            return;
        }

        mosshroom.getLookControl().setLookAt(toad);
        mosshroom.getNavigation().moveTo(toad, SPEED);
    }

    @Override
    public void stop() {
        // Stop moving but remember the toad while wandering nearby.
        mosshroom.getNavigation().stop();
    }
}