package net.matos.elementalrealms.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matos.elementalrealms.entity.custom.MosshroomEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MosshroomModel extends HierarchicalModel<MosshroomEntity> {

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart torso;
    private final ModelPart Cap;
    private final ModelPart CapSegment;
    private final ModelPart CapTop;
    private final ModelPart Moss3;
    private final ModelPart Moss2;
    private final ModelPart Moss1;
    private final ModelPart Feet;
    private final ModelPart RightFoot;
    private final ModelPart LeftFoot;

    public MosshroomModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.torso = this.body.getChild("torso");
        this.Cap = this.torso.getChild("Cap");
        this.CapSegment = this.Cap.getChild("CapSegment");
        this.CapTop = this.CapSegment.getChild("CapTop");
        this.Moss3 = this.Cap.getChild("Moss3");
        this.Moss2 = this.Cap.getChild("Moss2");
        this.Moss1 = this.Cap.getChild("Moss1");
        this.Feet = this.torso.getChild("Feet");
        this.RightFoot = this.Feet.getChild("RightFoot");
        this.LeftFoot = this.Feet.getChild("LeftFoot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -2.0F, 0.5F));

        PartDefinition Cap = torso.addOrReplaceChild("Cap", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, 0.0F));

        PartDefinition CapSegment = Cap.addOrReplaceChild("CapSegment", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -2.5F, -4.0F, 8.0F, 2.5F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

        PartDefinition CapTop = CapSegment.addOrReplaceChild("CapTop", CubeListBuilder.create().texOffs(24, 25).addBox(-2.5F, -1.0F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, 0.0F));

        PartDefinition Moss3 = Cap.addOrReplaceChild("Moss3", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.943F, -6.1611F, -2.3609F, 0.2735F, 0.0573F, -0.2888F));

        PartDefinition cube_r1 = Moss3.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(40, 0).addBox(0.0F, -5.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r2 = Moss3.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(10, 36).addBox(-1.312F, 0.7149F, -2.061F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.9952F, -5.7149F, 0.9611F, 0.0F, -1.0908F, 0.0F));

        PartDefinition Moss2 = Cap.addOrReplaceChild("Moss2", CubeListBuilder.create(), PartPose.offsetAndRotation(-1.197F, -5.6685F, 2.2707F, -0.2182F, 0.0F, -0.0873F));

        PartDefinition cube_r3 = Moss2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, 14).addBox(0.0F, -5.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4981F, 0.0386F, -0.3755F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r4 = Moss2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(24, 31).addBox(0.0F, -5.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4981F, -0.0386F, 0.3755F, 0.0F, -1.0908F, 0.0F));

        PartDefinition Moss1 = Cap.addOrReplaceChild("Moss1", CubeListBuilder.create(), PartPose.offsetAndRotation(2.1335F, -5.7903F, 0.4368F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r5 = Moss1.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 36).addBox(2.2139F, -0.7664F, -0.434F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.8796F, -4.2336F, -0.9368F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r6 = Moss1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(34, 31).addBox(0.0F, -5.0F, -2.5F, 0.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.0908F, 0.0F));

        PartDefinition Feet = torso.addOrReplaceChild("Feet", CubeListBuilder.create(), PartPose.offset(0.5F, -7.0F, -0.5F));

        PartDefinition RightFoot = Feet.addOrReplaceChild("RightFoot", CubeListBuilder.create().texOffs(40, 10).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, 7.0F, 0.45F));

        PartDefinition LeftFoot = Feet.addOrReplaceChild("LeftFoot", CubeListBuilder.create().texOffs(20, 41).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.75F, 7.0F, 0.5F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(MosshroomEntity entity, float limbSwing, float limbSwingAmount,
                          float ageInTicks, float netHeadYaw, float headPitch) {

        this.root().getAllParts().forEach(ModelPart::resetPose);


        // Defense has priority over all other animations.
        if (entity.defenseAnimationState.isStarted()) {
            this.animate(
                    entity.defenseAnimationState,
                    MosshroomAnimations.ANIM_MOSSHROOM_DEFENSE,
                    ageInTicks,
                    1.0F
            );

            return;
        }

        this.animateWalk(
                MosshroomAnimations.ANIM_MOSSHROOM_WALK,
                limbSwing,
                limbSwingAmount,
                2.0F,
                2.5F
        );

        this.animate(
                entity.idleAnimationState,
                MosshroomAnimations.ANIM_MOSSHROOM_IDLE,
                ageInTicks,
                1.0F
        );
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}

