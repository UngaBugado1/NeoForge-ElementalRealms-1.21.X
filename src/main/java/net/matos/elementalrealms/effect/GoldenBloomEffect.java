package net.matos.elementalrealms.effect;

import net.matos.elementalrealms.particle.ModParticles;
import net.matos.elementalrealms.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class GoldenBloomEffect extends MobEffect {

    public static final ResourceLocation GOLDEN_BLOOM_SPEED_BOOST_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "elementalrealms",
                    "golden_bloom_speed_boost"
            );

    public static final AttributeModifier GOLDEN_BLOOM_SPEED_BOOST =
            new AttributeModifier(
                    GOLDEN_BLOOM_SPEED_BOOST_ID,
                    0.2D,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

    public static final ResourceLocation GOLDEN_BLOOM_SCALE_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "elementalrealms",
                    "golden_bloom_scale"
            );

    public static final AttributeModifier GOLDEN_BLOOM_SCALE =
            new AttributeModifier(
                    GOLDEN_BLOOM_SCALE_ID,
                    1D,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

    public static final ResourceLocation GOLDEN_BLOOM_BLOCK_REACH_ID =
            ResourceLocation.fromNamespaceAndPath(
                    "elementalrealms",
                    "golden_bloom_block_reach"
            );

    public static final AttributeModifier GOLDEN_BLOOM_BLOCK_REACH =
            new AttributeModifier(
                    GOLDEN_BLOOM_BLOCK_REACH_ID,
                    2D,
                    AttributeModifier.Operation.ADD_VALUE
            );

    public GoldenBloomEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {

        // Spawn golden particles on the client.
        spawnGoldenParticles(entity);

        // Regeneration every 5 seconds.
        if (entity.tickCount % 100 == 0) {
            if (entity.getHealth() < entity.getMaxHealth()) {
                float regenAmount = 0.5F + amplifier * 0.2F;
                entity.heal(regenAmount);
            }
        }

        // Restore food every 5 seconds.
        if (entity.tickCount % 100 == 0
                && entity instanceof Player player) {
            if (player.getFoodData().needsFood()) {
                player.getFoodData().eat(1, 0.3F);
            }
        }

        // Apply the speed boost and climbing effects.
        applyFungusBoost(entity);
        applyFungusClimbing(entity);
        applyPlayerScale(entity);



        return true;
    }

    private void spawnGoldenParticles(LivingEntity entity) {
        // Spawn particles every 5 ticks.
        if (entity.tickCount % 5 != 0) {
            return;
        }

        if (!(entity.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        serverLevel.sendParticles(
                ModParticles.GOLDEN_BLOOM.get(),
                entity.getX() + (entity.getRandom().nextDouble() - 0.5D) * 0.8D,
                entity.getY() + entity.getRandom().nextDouble() * entity.getBbHeight(),
                entity.getZ() + (entity.getRandom().nextDouble() - 0.5D) * 0.8D,
                1,       // Number of particles
                0.0D,    // X spread
                0.02D,   // Y spread
                0.0D,    // Z spread
                0.01D    // Particle speed
        );
    }

    private void applyFungusClimbing(LivingEntity entity) {
        if (entity.horizontalCollision) {
            Vec3 initialVec = entity.getDeltaMovement();
            Vec3 climbVec = new Vec3(
                    initialVec.x,
                    0.2D,
                    initialVec.z
            );

            entity.setDeltaMovement(climbVec.scale(0.96D));
        }
    }

    private void applyFungusBoost(LivingEntity entity) {
        if (entity.level().isClientSide) {
            return;
        }

        Level level = entity.level();
        BlockPos blockPos = entity.blockPosition().below();
        BlockState blockState = level.getBlockState(blockPos);

        var attributes = entity.getAttribute(Attributes.MOVEMENT_SPEED);

        boolean hasEffect = entity.hasEffect(
                ModEffects.GOLDEN_BLOOM_EFFECT.getDelegate()
        );

        if (blockState.is(ModTags.Blocks.FUNGUS_BLOCKS) && hasEffect) {
            if (attributes != null
                    && !attributes.hasModifier(GOLDEN_BLOOM_SPEED_BOOST_ID)) {
                attributes.addTransientModifier(GOLDEN_BLOOM_SPEED_BOOST);
            }
        } else {
            if (attributes != null
                    && attributes.hasModifier(GOLDEN_BLOOM_SPEED_BOOST_ID)) {
                attributes.removeModifier(GOLDEN_BLOOM_SPEED_BOOST_ID);
            }
        }
    }

    private void applyPlayerScale(LivingEntity entity) {
        if (entity.level().isClientSide || !(entity instanceof Player)) {
            return;
        }

        var scaleAttribute = entity.getAttribute(Attributes.SCALE);
        var blockReachAttribute = entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE);

        if (scaleAttribute != null
                && !scaleAttribute.hasModifier(GOLDEN_BLOOM_SCALE_ID)) {
            scaleAttribute.addTransientModifier(GOLDEN_BLOOM_SCALE);
        }

        if (blockReachAttribute != null
                && !blockReachAttribute.hasModifier(GOLDEN_BLOOM_BLOCK_REACH_ID)) {
            blockReachAttribute.addTransientModifier(GOLDEN_BLOOM_BLOCK_REACH);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(
            int duration,
            int amplifier
    ) {
        return true;
    }

    @Override
    public boolean isInstantenous() {
        return false;
    }

    @Override
    public void removeAttributeModifiers(AttributeMap attributeMap) {
        super.removeAttributeModifiers(attributeMap);

        var speedAttribute = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
        if (speedAttribute != null
                && speedAttribute.hasModifier(GOLDEN_BLOOM_SPEED_BOOST_ID)) {
            speedAttribute.removeModifier(GOLDEN_BLOOM_SPEED_BOOST_ID);
        }

        var scaleAttribute = attributeMap.getInstance(Attributes.SCALE);
        if (scaleAttribute != null
                && scaleAttribute.hasModifier(GOLDEN_BLOOM_SCALE_ID)) {
            scaleAttribute.removeModifier(GOLDEN_BLOOM_SCALE_ID);
        }

        var blockReachAttribute = attributeMap.getInstance(Attributes.BLOCK_INTERACTION_RANGE);
        if (blockReachAttribute != null
                && blockReachAttribute.hasModifier(GOLDEN_BLOOM_BLOCK_REACH_ID)) {
            blockReachAttribute.removeModifier(GOLDEN_BLOOM_BLOCK_REACH_ID);
        }
    }
}