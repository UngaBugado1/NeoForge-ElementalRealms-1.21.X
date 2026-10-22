
package net.matos.elementalrealms.entity.custom;

import net.matos.elementalrealms.effect.ModEffects;
import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.entity.ai.mosshroom.MosshroomDefense;
import net.matos.elementalrealms.entity.ai.mosshroom.MosshroomFollowToadGoal;
import net.matos.elementalrealms.item.ModItems;
import net.matos.elementalrealms.sound.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

public class MosshroomEntity extends Animal implements Bucketable {

    private static final EntityDataAccessor<Boolean> GOLDEN_BLOOM =
            SynchedEntityData.defineId(
                    MosshroomEntity.class,
                    EntityDataSerializers.BOOLEAN
            );

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState defenseAnimationState = new AnimationState();

    private int idleAnimationTimeout = 0;
    private int defenseTicks = 0;
    private int cloudDelayTicks = -1;

    private boolean fromBucket;

    public MosshroomEntity(
            EntityType<? extends Animal> entityType,
            Level level
    ) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GOLDEN_BLOOM, false);
    }

    public boolean hasGoldenBloom() {
        return this.entityData.get(GOLDEN_BLOOM);
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.35D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));

        this.goalSelector.addGoal(3, new TemptGoal(
                this,
                1.25D,
                stack -> stack.is(Items.GLOW_BERRIES),
                false
        ));

        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));

        // Best friends with Mossback Toads!
        this.goalSelector.addGoal(5, new MosshroomFollowToadGoal(this));

        this.goalSelector.addGoal(
                6,
                new WaterAvoidingRandomStrollGoal(this, 1.0D)
        );

        this.goalSelector.addGoal(
                7,
                new LookAtPlayerGoal(this, Player.class, 6.0F)
        );

        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Items.GLOW_BERRIES);
    }

    @Override
    public AgeableMob getBreedOffspring(
            ServerLevel serverLevel,
            AgeableMob ageableMob
    ) {
        return ModEntities.MOSSHROOM.get().create(serverLevel);
    }

    public boolean isDefending() {
        return this.defenseTicks > 0;
    }

    public void startDefense() {
        if (this.level().isClientSide || this.isDefending()) {
            return;
        }

        this.defenseTicks = MosshroomDefense.DURATION;

        this.getNavigation().stop();
        this.setSprinting(false);

        this.level().broadcastEntityEvent(this, (byte) 60);

        this.cloudDelayTicks = 42;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);

        if (hurt && !this.level().isClientSide && !this.isDefending()) {
            MosshroomDefense.tryActivate(this);
        }

        return hurt;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isDefending()) {
            super.travel(Vec3.ZERO);
            return;
        }

        super.travel(travelVector);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 60) {
            this.idleAnimationState.stop();
            this.defenseAnimationState.start(this.tickCount);
        } else if (id == 61) {
            this.defenseAnimationState.stop();
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void setupAnimationStates() {
        if (this.isDefending()) {
            this.idleAnimationState.stop();
            return;
        }

        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
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

        // Dismount if the Mossback Toad starts an animation.
        if (!this.level().isClientSide && this.isPassenger()) {
            if (this.getVehicle() instanceof MossbackToadEntity toad
                    && toad.getAnimationPhase() != MossbackToadEntity.PHASE_NONE) {
                this.stopRiding();
            }
        }

        spawnToadFriendshipParticles();

        if (!this.level().isClientSide) {
            tryMountMossbackToad();
        }

        // Synchronize Golden Bloom from the server to tracking clients.
        if (!this.level().isClientSide) {
            boolean hasEffect = this.hasEffect(
                    ModEffects.GOLDEN_BLOOM_EFFECT.getDelegate()
            );

            if (this.entityData.get(GOLDEN_BLOOM) != hasEffect) {
                this.entityData.set(GOLDEN_BLOOM, hasEffect);
            }
        }

        if (!this.level().isClientSide && this.cloudDelayTicks > 0) {
            this.cloudDelayTicks--;

            if (this.cloudDelayTicks == 0) {
                MosshroomDefense.releaseCloud(this);
                this.cloudDelayTicks = -1;
            }
        }

        if (this.level().isClientSide) {
            this.setupAnimationStates();
            return;
        }

        if (this.defenseTicks > 0) {
            this.defenseTicks--;

            if (this.defenseTicks <= 0) {
                this.getNavigation().stop();
                this.level().broadcastEntityEvent(this, (byte) 61);
            }
        }
    }

    private void tryMountMossbackToad() {
        if (this.level().isClientSide
                || this.isPassenger()
                || this.isDefending()) {
            return;
        }

        // Find a nearby Mossback Toad.
        MossbackToadEntity toad = this.level()
                .getEntitiesOfClass(
                        MossbackToadEntity.class,
                        this.getBoundingBox().inflate(0.2D),
                        entity -> entity.isAlive()
                                && !entity.isPassenger()
                                && entity.getAnimationPhase()
                                == MossbackToadEntity.PHASE_NONE
                                && entity.getPassengers().isEmpty()
                )
                .stream()
                .filter(entity ->
                        this.getBoundingBox()
                                .inflate(0.15D)
                                .intersects(entity.getBoundingBox())
                )
                .min(Comparator.comparingDouble(this::distanceToSqr))
                .orElse(null);

        if (toad != null) {
            this.getNavigation().stop();
            this.setSprinting(false);
            this.startRiding(toad);
        }
    }

    // SOUNDS
    @Override
    protected SoundEvent getAmbientSound() {
        return switch (this.random.nextInt(6)) {
            case 0 -> ModSounds.MOSSHROOM_AMBIENT1.get();
            case 1 -> ModSounds.MOSSHROOM_AMBIENT2.get();
            case 2 -> ModSounds.MOSSHROOM_AMBIENT3.get();
            case 3 -> ModSounds.MOSSHROOM_AMBIENT4.get();
            default -> ModSounds.MOSSHROOM_AMBIENT5.get();
        };
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return switch (this.random.nextInt(4)) {
            case 0 -> ModSounds.MOSSHROOM_HURT1.get();
            case 1 -> ModSounds.MOSSHROOM_HURT2.get();
            case 2 -> ModSounds.MOSSHROOM_HURT3.get();
            default -> ModSounds.MOSSHROOM_HURT4.get();
        };
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.random.nextBoolean()
                ? ModSounds.MOSSHROOM_DEATH1.get()
                : ModSounds.MOSSHROOM_DEATH2.get();
    }

    // BUCKET

    @Override
    public boolean fromBucket() {
        return this.fromBucket;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.fromBucket = fromBucket;
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);

        stack.update(
                DataComponents.BUCKET_ENTITY_DATA,
                CustomData.EMPTY,
                data -> data.update(tag ->
                        tag.putInt("MosshroomAge", this.getAge())
                )
        );
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);

        if (tag.contains("MosshroomAge")) {
            this.setAge(tag.getInt("MosshroomAge"));
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.BUCKET_OF_MOSSHROOM.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // BREED WITH GLOW BERRIES
        if (this.isFood(heldItem)
                && this.getAge() == 0
                && this.canFallInLove()) {

            this.usePlayerItem(player, hand, heldItem);
            this.setInLove(player);

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // DISMOUNT WITH EMPTY HAND
        if (this.isPassenger() && heldItem.isEmpty()) {
            if (!this.level().isClientSide) {
                this.stopRiding();
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // PICK UP WITH BUCKET
        if (heldItem.is(Items.BUCKET) && this.isAlive()) {
            if (!this.level().isClientSide) {
                ItemStack mosshroomBucket = this.getBucketItemStack();

                this.setFromBucket(true);
                this.saveToBucketTag(mosshroomBucket);
                this.playSound(this.getPickupSound(), 1.0F, 1.0F);

                if (!player.getAbilities().instabuild) {
                    heldItem.shrink(1);

                    if (heldItem.isEmpty()) {
                        player.setItemInHand(hand, mosshroomBucket);
                    } else if (!player.getInventory().add(mosshroomBucket)) {
                        player.drop(mosshroomBucket, false);
                    }
                } else {
                    if (!player.getInventory().add(mosshroomBucket)) {
                        player.drop(mosshroomBucket, false);
                    }
                }

                this.discard();
            }

            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // COLLECT GOLDEN ESSENCE
        if (heldItem.is(Items.GLASS_BOTTLE)
                && this.hasEffect(ModEffects.GOLDEN_BLOOM_EFFECT.getDelegate())) {

            if (!this.level().isClientSide) {
                ItemStack goldenEssence =
                        new ItemStack(ModItems.GOLDEN_ESSENCE.get());

                if (heldItem.getCount() == 1) {
                    player.setItemInHand(hand, goldenEssence);
                } else {
                    heldItem.shrink(1);

                    if (!player.getInventory().add(goldenEssence)) {
                        player.drop(goldenEssence, false);
                    }
                }

                this.removeEffect(
                        ModEffects.GOLDEN_BLOOM_EFFECT.getDelegate()
                );

                this.playSound(
                        SoundEvents.HONEYCOMB_WAX_ON,
                        1.0F,
                        1.0F
                );
            }

        }

        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }


    private void spawnToadFriendshipParticles() {
        if (this.level().isClientSide || this.tickCount % 12 != 0) {
            return;
        }

        if (this.hasEffect(ModEffects.GOLDEN_BLOOM_EFFECT)) {
            return;
        }

        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        boolean nearbyToad = !serverLevel.getEntitiesOfClass(
                MossbackToadEntity.class,
                this.getBoundingBox().inflate(4.0D),
                entity -> entity.isAlive()
        ).isEmpty();

        if (nearbyToad && this.random.nextFloat() < 0.5F) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            double distance = 0.8D + this.random.nextDouble() * 1.2D;

            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    this.getX() + Math.cos(angle) * distance,
                    this.getY() + 0.2D + this.random.nextDouble() * 1.2D,
                    this.getZ() + Math.sin(angle) * distance,
                    1,
                    0.05D, 0.12D, 0.05D,
                    0.005D
            );
        }
    }

}
