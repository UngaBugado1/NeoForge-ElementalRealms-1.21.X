
package net.matos.elementalrealms.entity.projectile;

import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.item.ModItems;
import net.matos.elementalrealms.particle.ModParticles;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class FungusSporesProjectile extends ThrowableItemProjectile {
    private static final double RADIUS = 3.0D;

    public FungusSporesProjectile(EntityType<? extends FungusSporesProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FungusSporesProjectile(Level level, LivingEntity owner, ItemStack itemStack) {
        super(ModEntities.FUNGUS_SPORES.get(), owner, level);
        this.setItem(new ItemStack(ModItems.FUNGUS_SPORES_PROJECTILE.get()));
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.FUNGUS_SPORES_PROJECTILE.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if (level() instanceof ServerLevel serverLevel) {
            releaseCloud(
                    serverLevel,
                    hitResult.getLocation().x,
                    hitResult.getLocation().y,
                    hitResult.getLocation().z
            );
        }

        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        if (level() instanceof ServerLevel serverLevel) {
            releaseCloud(
                    serverLevel,
                    hitResult.getLocation().x,
                    hitResult.getLocation().y,
                    hitResult.getLocation().z
            );
        }

        discard();
    }

    private void releaseCloud(ServerLevel level, double x, double y, double z) {
        level.sendParticles(
                ModParticles.MOSS_POISON.get(),
                x, y + 0.4D, z,
                35,
                0.5D, 0D, 0.5D,
                0.02D
        );

        level.playSound(
                null, x, y, z,
                SoundEvents.BREEZE_LAND,
                SoundSource.NEUTRAL,
                1.0F, 1.0F
        );

        AABB area = new AABB(x, y, z, x, y, z).inflate(RADIUS);

        for (LivingEntity target : level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity.isAlive()
                        && entity.distanceToSqr(x, y, z) <= RADIUS * RADIUS
        )) {
            target.addEffect(new MobEffectInstance(
                    MobEffects.POISON,
                    600,
                    1
            ));
        }
    }
}
