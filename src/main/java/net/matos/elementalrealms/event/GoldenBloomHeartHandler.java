
package net.matos.elementalrealms.event;

import net.matos.elementalrealms.effect.ModEffects;
import net.minecraft.client.gui.Gui;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;

public class GoldenBloomHeartHandler {

    @SubscribeEvent
    public static void onPlayerHeartType(PlayerHeartTypeEvent event) {
        if (event.getEntity().hasEffect(
                ModEffects.GOLDEN_BLOOM_EFFECT
        )) {
            event.setType(
                    Gui.HeartType.valueOf("ELEMENTALREALMS_GOLDEN")
            );
        }
    }
}
