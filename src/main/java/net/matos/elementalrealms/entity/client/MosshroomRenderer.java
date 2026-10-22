package net.matos.elementalrealms.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MosshroomRenderer extends MobRenderer<MosshroomEntity, MosshroomModel> {
    public MosshroomRenderer(EntityRendererProvider.Context context) {
        super(context, new MosshroomModel(context.bakeLayer(ModModelLayers.MOSSHROOM)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(MosshroomEntity entity) {
        if (entity.hasGoldenBloom()) {
            return ResourceLocation.fromNamespaceAndPath(
                    "elementalrealms",
                    "textures/entity/mosshroom/golden_mosshroom.png"
            );
        }

        return ResourceLocation.fromNamespaceAndPath(
                "elementalrealms",
                "textures/entity/mosshroom/mosshroom.png"
        );
    }

    @Override
    public void render(MosshroomEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby())
        {
            poseStack.scale(0.7f,0.7f, 0.7f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
