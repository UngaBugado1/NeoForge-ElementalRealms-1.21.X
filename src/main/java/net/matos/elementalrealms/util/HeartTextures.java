
package net.matos.elementalrealms.util;

import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

public class HeartTextures {

    private static final String MOD_ID = "elementalrealms";

    private static ResourceLocation heart(String name) {
        return ResourceLocation.fromNamespaceAndPath(
                MOD_ID, "hud/heart/" + name
        );
    }

    public static final EnumProxy<Gui.HeartType> HEART_TYPE_ENUM_PROXY =
            new EnumProxy<>(
                    Gui.HeartType.class,
                    heart("golden_bloom_full"),
                    heart("golden_bloom_full_blinking"),
                    heart("golden_bloom_half"),
                    heart("golden_bloom_half_blinking"),
                    heart("golden_bloom_hardcore_full"),
                    heart("golden_bloom_hardcore_full_blinking"),
                    heart("golden_bloom_hardcore_half"),
                    heart("golden_bloom_hardcore_half_blinking")
            );
}
