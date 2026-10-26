package net.matos.elementalrealms.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.matos.elementalrealms.ElementalRealms;
import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.matos.elementalrealms.entity.custom.TectoraxEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RockspineRenderer extends MobRenderer<RockspineEntity, RockspineModel> {
    public RockspineRenderer(EntityRendererProvider.Context context) {
        super(context, new RockspineModel(context.bakeLayer(ModModelLayers.ROCKSPINE)), 1f);
    }

    @Override
    public ResourceLocation getTextureLocation(RockspineEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(ElementalRealms.MOD_ID, "textures/entity/rockspine/rockspine.png");
    }

}
