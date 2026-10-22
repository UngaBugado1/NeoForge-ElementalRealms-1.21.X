
package net.matos.elementalrealms.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RawAncientMeatBlock extends Block {

    public static final DirectionProperty FACING =
            BlockStateProperties.FACING;

    public static final IntegerProperty BITES =
            IntegerProperty.create("bites", 0, 3);

    public RawAncientMeatBlock(BlockBehaviour.Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(BITES, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(FACING, BITES);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(
                FACING,
                context.getNearestLookingDirection().getOpposite()
        );
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(
                FACING,
                rotation.rotate(state.getValue(FACING))
        );
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(
                mirror.getRotation(state.getValue(FACING))
        );
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!player.canEat(false)) {
            return InteractionResult.CONSUME;
        }

        if (!level.isClientSide) {
            player.getFoodData().eat(2, 0.6F);

            level.playSound(null, pos, SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 1.0F, 1.0F);

            int bites = state.getValue(BITES);

            if (bites < 3) {
                level.setBlock(
                        pos,
                        state.setValue(BITES, bites + 1),
                        3
                );
            } else {
                level.removeBlock(pos, false);
            }

            level.gameEvent(player, GameEvent.EAT, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level,
                                  BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        int bites = state.getValue(BITES);

        double size = (16.0 - bites * 4.0) / 16.0;

        return switch (facing) {
            case EAST -> Shapes.box(1.0 - size, 0, 0, 1, 1, 1);
            case WEST -> Shapes.box(0, 0, 0, size, 1, 1);

            case UP -> Shapes.box(0, 0, 0, 1, size, 1);
            case DOWN -> Shapes.box(0, 1.0 - size, 0, 1, 1, 1);

            case SOUTH -> Shapes.box(0, 0, 1.0 - size, 1, 1, 1);
            case NORTH -> Shapes.box(0, 0, 0, 1, 1, size);
        };
    }
}
