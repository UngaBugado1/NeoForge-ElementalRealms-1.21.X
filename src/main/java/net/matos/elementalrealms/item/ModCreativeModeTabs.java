
package net.matos.elementalrealms.item;

import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ElementalRealms.MOD_ID);

    public static final Supplier<CreativeModeTab> TERRAVALE_TABS =
            CREATIVE_MODE_TABS.register("terravale_tabs", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.elementalrealms.terravale_tab"))
                    .icon(() -> new ItemStack(ModItems.VERINDITE_CRYSTAL.get()))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.VERINDITE_CRYSTAL.get());
                        pOutput.accept(ModItems.MOSSHROOM_CAP.get());
                        pOutput.accept(ModItems.MOSSHROOM_SPAWN_EGG.get());
                        pOutput.accept(ModItems.BUCKET_OF_MOSSHROOM.get());
                        pOutput.accept(ModItems.GOLDEN_ESSENCE.get());
                        pOutput.accept(ModItems.MOSSBACK_TOAD_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ANCIENT_STEW.get());
                        pOutput.accept(ModBlocks.RAW_ANCIENT_MEAT.get());
                        pOutput.accept(ModItems.RAW_ANCIENT_CHOP.get());
                        pOutput.accept(ModBlocks.COOKED_ANCIENT_MEAT);
                        pOutput.accept(ModItems.COOKED_ANCIENT_CHOP.get());
                        pOutput.accept(ModItems.FUNGUS_SPORES.get());
                        pOutput.accept(ModItems.TECTORAX_SPAWN_EGG.get());
                        pOutput.accept(ModItems.ROCKSPINE_SPAWN_EGG.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
