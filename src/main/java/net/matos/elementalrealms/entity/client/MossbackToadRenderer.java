package net.matos.elementalrealms.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MossbackToadRenderer extends MobRenderer<MossbackToadEntity, MossbackToadModel> {
    public MossbackToadRenderer(EntityRendererProvider.Context context) {
        super(context, new MossbackToadModel(context.bakeLayer(ModModelLayers.MOSSBACK_TOAD)), 0.8f);
    }

    @Override
    public ResourceLocation getTextureLocation(MossbackToadEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(ElementalRealms.MOD_ID, "textures/entity/mossback_toad/mossback_toad.png");
    }

    @Override
    public void render(MossbackToadEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby())
        {
            poseStack.scale(0.5f,0.5f, 0.5f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
