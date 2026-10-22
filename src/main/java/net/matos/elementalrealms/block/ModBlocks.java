package net.matos.elementalrealms.block;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.block.custom.CookedAncientMeatBlock;
import net.matos.elementalrealms.block.custom.RawAncientMeatBlock;
import net.matos.elementalrealms.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ElementalRealms.MOD_ID);

    public static final DeferredBlock<RawAncientMeatBlock> RAW_ANCIENT_MEAT =
            registerBlock("raw_ancient_meat", () -> new RawAncientMeatBlock(BlockBehaviour.Properties.of()
                    .strength(0.8F)
                    .sound(SoundType.HONEY_BLOCK)));

    public static final DeferredBlock<CookedAncientMeatBlock> COOKED_ANCIENT_MEAT =
            registerBlock("cooked_ancient_meat", () -> new CookedAncientMeatBlock(BlockBehaviour.Properties.of()
                    .strength(0.8F)
                    .sound(SoundType.HONEY_BLOCK)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
