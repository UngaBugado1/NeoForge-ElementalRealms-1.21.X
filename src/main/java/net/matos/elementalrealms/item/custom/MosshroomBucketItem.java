
package net.matos.elementalrealms.item.custom;

import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MosshroomBucketItem extends Item {

    public MosshroomBucketItem(Properties properties) {
        super(properties.stacksTo(1));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack bucket = context.getItemInHand();

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        BlockPos pos = context.getClickedPos()
                .relative(context.getClickedFace());

        MosshroomEntity mosshroom =
                ModEntities.MOSSHROOM.get().create(level);

        if (mosshroom == null) {
            return InteractionResult.FAIL;
        }

        mosshroom.moveTo(
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                level.random.nextFloat() * 360.0F,
                0.0F
        );

        CustomData data = bucket.getOrDefault(
                DataComponents.BUCKET_ENTITY_DATA,
                CustomData.EMPTY
        );

        mosshroom.loadFromBucketTag(data.copyTag());
        mosshroom.setFromBucket(true);

        if (!level.noCollision(mosshroom)) {
            return InteractionResult.FAIL;
        }

        level.addFreshEntity(mosshroom);

        level.playSound(
                null,
                pos,
                SoundEvents.BUCKET_EMPTY,
                SoundSource.PLAYERS,
                1.0F,
                1.0F
        );

        if (player == null || !player.getAbilities().instabuild) {
            bucket.shrink(1);

            ItemStack emptyBucket = new ItemStack(Items.BUCKET);

            if (bucket.isEmpty()) {
                if (player != null) {
                    player.setItemInHand(context.getHand(), emptyBucket);
                }
            } else if (player != null &&
                    !player.getInventory().add(emptyBucket)) {
                player.drop(emptyBucket, false);
            }
        }

        return InteractionResult.SUCCESS;
    }
}
