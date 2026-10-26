package net.matos.elementalrealms.item;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.entity.ModEntities;
import net.matos.elementalrealms.item.custom.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ElementalRealms.MOD_ID);

    public static final DeferredItem<Item> VERINDITE_CRYSTAL =
            ITEMS.registerItem("verindite_crystal", Item::new, new Item.Properties());

    public static final DeferredItem<Item> MOSSHROOM_CAP =
            ITEMS.registerItem("mosshroom_cap", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.elementalrealms.mosshroom_cap"));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().food(ModFoodProperties.MOSSHROOM_CAP));

    public static final DeferredItem<Item> MOSSHROOM_SPAWN_EGG = ITEMS.register("mosshroom_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MOSSHROOM, 0xc92b29, 0x495e27,
                    new Item.Properties()));

    public static final DeferredItem<Item> MOSSBACK_TOAD_SPAWN_EGG = ITEMS.register("mossback_toad_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MOSSBACK_TOAD, 0xf19645, 0x495e27,
                    new Item.Properties()));

    public static final DeferredItem<Item> BUCKET_OF_MOSSHROOM = ITEMS.register("bucket_of_mosshroom",
                    () -> new MosshroomBucketItem(new Item.Properties()));

    public static final DeferredItem<Item> GOLDEN_ESSENCE = ITEMS.register("golden_essence",
            () -> new GoldenEssenceItem());

    public static final DeferredItem<Item> ANCIENT_STEW = ITEMS.register("ancient_stew",
            () -> new AncientStewItem());

    public static final DeferredItem<Item> RAW_ANCIENT_CHOP =
            ITEMS.registerItem("raw_ancient_chop", properties -> new Item(properties) {
            }, new Item.Properties().food(ModFoodProperties.RAW_ANCIENT_CHOP));

    public static final DeferredItem<Item> COOKED_ANCIENT_CHOP =
            ITEMS.registerItem("cooked_ancient_chop", properties -> new Item(properties) {
            }, new Item.Properties().food(ModFoodProperties.COOKED_ANCIENT_CHOP));

    public static final DeferredItem<Item> FUNGUS_SPORES = ITEMS.register("fungus_spores",
            () -> new FungusSporesItem(new Item.Properties()));

    public static final DeferredItem<Item> FUNGUS_SPORES_PROJECTILE =
            ITEMS.register("fungus_spores_projectile",
                    () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> TECTORAX_SPAWN_EGG = ITEMS.register("tectorax_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TECTORAX, 0x7B5356, 0xB1A095,
                    new Item.Properties()));

    public static final DeferredItem<Item> ROCKSPINE_SPAWN_EGG = ITEMS.register("rockspine_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.ROCKSPINE, 0x786750, 0x6B322F,
                    new Item.Properties()));

    public static final DeferredItem<Item> PRIMAL_CLUB = ITEMS.register(
            "primal_club", PrimalClubItem::new);




    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}