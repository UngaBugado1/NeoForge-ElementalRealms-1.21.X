
package net.matos.elementalrealms.event;

import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.matos.elementalrealms.entity.projectile.FungusSporesProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RockspineSoundHandler {

    private static final Map<UUID, PlayerState> STATES = new HashMap<>();

    private RockspineSoundHandler() {}

    private static final class PlayerState {
        Vec3 lastPosition;
        Vec3 lastFootstepPosition;
        boolean wasOnGround;

        PlayerState(Player player) {
            this.lastPosition = player.position();
            this.lastFootstepPosition = player.position();
            this.wasOnGround = player.onGround();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || player.isSpectator()) {
            return;
        }

        PlayerState state = STATES.computeIfAbsent(
                player.getUUID(), id -> new PlayerState(player));

        Vec3 position = player.position();
        ServerLevel level = (ServerLevel) player.level();

        // Detect landing.
        if (!state.wasOnGround && player.onGround()) {
            reportSound(level, position, player, 12.0D);
            state.lastFootstepPosition = position;
        }

        // Detect movement-based footsteps.
        if (player.onGround()) {
            double distance = position.distanceToSqr(state.lastFootstepPosition);

            double threshold = player.isShiftKeyDown() ? 0.9D
                    : player.isSprinting() ? 0.45D : 0.65D;

            if (distance >= threshold * threshold) {
                double range = player.isShiftKeyDown() ? 4.0D
                        : player.isSprinting() ? 18.0D : 10.0D;

                reportSound(level, position, player, range);
                state.lastFootstepPosition = position;
            }
        }

        state.wasOnGround = player.onGround();
        state.lastPosition = position;
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof Player player
                && !player.level().isClientSide()
                && !player.isSpectator()) {

            ServerLevel level = (ServerLevel) player.level();
            reportSound(level, player.position(), player, 20.0D);
        }
    }

    // Detect finishing food or drinks.
    @SubscribeEvent
    public static void onItemConsumed(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof Player player)
                || player.level().isClientSide()
                || player.isSpectator()) {
            return;
        }

        ItemStack item = event.getItem();

        boolean food = item.getFoodProperties(null) != null;
        boolean drink = item.is(Items.POTION)
                || item.is(Items.MILK_BUCKET)
                || item.is(Items.HONEY_BOTTLE);

        if (food || drink) {
            ServerLevel level = (ServerLevel) player.level();
            reportSound(level, player.position(), player, 12.0D);
        }
    }

    // Detect breaking blocks.
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (player.level() instanceof ServerLevel level) {
            reportSound(
                    level,
                    Vec3.atCenterOf(event.getPos()),
                    player,
                    18.0D
            );
        }
    }

    // Detect placing blocks.
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player
                && player.level() instanceof ServerLevel level) {

            reportSound(
                    level,
                    Vec3.atCenterOf(event.getPos()),
                    player,
                    16.0D
            );
        }
    }

    // Detect opening/interacting with chests, barrels and doors.
    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || player.isSpectator()) {
            return;
        }

        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        boolean container = state.is(Blocks.CHEST)
                || state.is(Blocks.TRAPPED_CHEST)
                || state.is(Blocks.ENDER_CHEST)
                || state.is(Blocks.BARREL);

        boolean door = state.is(BlockTags.DOORS)
                || state.is(BlockTags.TRAPDOORS)
                || state.is(BlockTags.FENCE_GATES);

        if (container || door) {
            reportSound(
                    level,
                    Vec3.atCenterOf(pos),
                    player,
                    14.0D
            );
        }
    }

    // Detect player melee attacks.
    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player.level() instanceof ServerLevel level
                && !player.isSpectator()) {

            reportSound(level, player.position(), player, 14.0D);
        }
    }

    // Detect projectile impacts, including snowballs hitting blocks.
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }

        if (!(event.getProjectile() instanceof Snowball
                || event.getProjectile() instanceof ThrownEgg
                || event.getProjectile() instanceof AbstractArrow
                || event.getProjectile() instanceof ThrownTrident
                || event.getProjectile() instanceof FungusSporesProjectile
                || event.getProjectile() instanceof ThrownPotion)) {
            return;
        }

        Vec3 impactPosition = event.getRayTraceResult().getLocation();

        Player source = event.getProjectile().getOwner() instanceof Player player
                ? player : null;

        reportSound(level, impactPosition, source, 14.0D);
    }

    public static void reportSound(
            ServerLevel level,
            Vec3 position,
            Player source,
            double radius
    ) {
        AABB area = new AABB(
                position.x - radius,
                position.y - radius,
                position.z - radius,
                position.x + radius,
                position.y + radius,
                position.z + radius
        );

        for (RockspineEntity rockspine :
                level.getEntitiesOfClass(RockspineEntity.class, area)) {

            rockspine.hearSound(position, source, radius);
        }
    }
}
