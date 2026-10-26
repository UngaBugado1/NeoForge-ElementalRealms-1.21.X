package net.matos.elementalrealms.entity.client;// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matos.elementalrealms.entity.custom.RockspineEntity;
import net.matos.elementalrealms.entity.custom.TectoraxEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class RockspineModel extends HierarchicalModel<RockspineEntity> {

    private final ModelPart root;
    private final ModelPart Body;
    private final ModelPart Torso;
    private final ModelPart Tail0;
    private final ModelPart Tail1;
    private final ModelPart Tail2;
    private final ModelPart Tail3;
    private final ModelPart Tail_Boulder;
    private final ModelPart Torso_spikes;
    private final ModelPart Head;
    private final ModelPart left_back_leg;
    private final ModelPart right_back_leg;
    private final ModelPart right_front_leg;
    private final ModelPart left_front_leg;

    public RockspineModel(ModelPart root) {
        this.root = root.getChild("root");
        this.Body = this.root.getChild("Body");
        this.Torso = this.Body.getChild("Torso");
        this.Tail0 = this.Torso.getChild("Tail0");
        this.Tail1 = this.Tail0.getChild("Tail1");
        this.Tail2 = this.Tail1.getChild("Tail2");
        this.Tail3 = this.Tail2.getChild("Tail3");
        this.Tail_Boulder = this.Tail3.getChild("Tail_Boulder");
        this.Torso_spikes = this.Torso.getChild("Torso_spikes");
        this.Head = this.Torso.getChild("Head");
        this.left_back_leg = this.Body.getChild("left_back_leg");
        this.right_back_leg = this.Body.getChild("right_back_leg");
        this.right_front_leg = this.Body.getChild("right_front_leg");
        this.left_front_leg = this.Body.getChild("left_front_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Body = root.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -10.0F, 0.0F, 0.0F, 0.0F, 0.0F));

        PartDefinition Torso = Body.addOrReplaceChild("Torso", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -7.75F, -9.25F, 16.0F, 15.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.25F, 0.75F));

        PartDefinition Tail0 = Torso.addOrReplaceChild("Tail0", CubeListBuilder.create().texOffs(24, 61).addBox(-2.75F, -2.5F, 0.0F, 5.5F, 4.75F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.25F, 8.75F));

        PartDefinition Tail1 = Tail0.addOrReplaceChild("Tail1", CubeListBuilder.create().texOffs(40, 57).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 4.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(0.25F, -3.95F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition Tail2 = Tail1.addOrReplaceChild("Tail2", CubeListBuilder.create().texOffs(64, 60).addBox(-2.0F, -1.75F, 0.0F, 4.0F, 3.5F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(0.25F, -3.7F, -1.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.25F, 7.0F));

        PartDefinition Tail3 = Tail2.addOrReplaceChild("Tail3", CubeListBuilder.create().texOffs(82, 60).addBox(-1.75F, -1.5F, 0.0F, 3.5F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(0.25F, -3.45F, -1.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

        PartDefinition Tail_Boulder = Tail3.addOrReplaceChild("Tail_Boulder", CubeListBuilder.create().texOffs(0, 115).addBox(-1.4536F, -4.9536F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(1.4464F, -4.9536F, 0.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(98, 55).addBox(-3.0536F, -3.0036F, 0.0F, 6.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0536F, 0.0036F, 5.0F));

        PartDefinition tail_boulder_spike_r1 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r1", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4536F, 2.9464F, 3.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition tail_boulder_spike_r2 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r2", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.4464F, 2.9464F, 3.0F, 0.0F, 0.0F, -3.1416F));

        PartDefinition tail_boulder_spike_r3 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r3", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9536F, -0.5036F, 0.5F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition tail_boulder_spike_r4 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r4", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9536F, -0.5036F, 2.5F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition tail_boulder_spike_r5 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r5", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.9536F, -0.5036F, 4.5F, 0.0F, -1.5708F, -1.5708F));

        PartDefinition tail_boulder_spike_r6 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r6", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(3.9F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.4536F, -0.4536F, 7.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition tail_boulder_spike_r7 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r7", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9964F, 0.4964F, 4.5F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition tail_boulder_spike_r8 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r8", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9964F, 0.4964F, 2.5F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition tail_boulder_spike_r9 = Tail_Boulder.addOrReplaceChild("tail_boulder_spike_r9", CubeListBuilder.create().texOffs(0, 115).addBox(1.0F, -2.0F, -3.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.9964F, 0.4964F, 0.5F, 0.0F, -1.5708F, 1.5708F));

        PartDefinition Torso_spikes = Torso.addOrReplaceChild("Torso_spikes", CubeListBuilder.create(), PartPose.offset(0.0F, 0.5F, -8.25F));

        PartDefinition spike_r1 = Torso_spikes.addOrReplaceChild("spike_r1", CubeListBuilder.create().texOffs(0, 107).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 1.5708F, 0.2182F, 1.5708F));

        PartDefinition spike_r2 = Torso_spikes.addOrReplaceChild("spike_r2", CubeListBuilder.create().texOffs(0, 107).addBox(1.0F, -5.0F, -7.0F, 0.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.25F, -1.0F, 1.5708F, 0.1745F, 1.5708F));

        PartDefinition spike_r3 = Torso_spikes.addOrReplaceChild("spike_r3", CubeListBuilder.create().texOffs(0, 107).addBox(1.0F, -5.0F, -7.0F, 0.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 1.75F, 17.0F, -1.5708F, 0.2618F, -1.5708F));

        PartDefinition spike_r4 = Torso_spikes.addOrReplaceChild("spike_r4", CubeListBuilder.create().texOffs(0, 107).addBox(1.0F, -5.0F, -7.0F, 0.0F, 5.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -4.25F, 17.0F, -1.5708F, 0.1745F, -1.5708F));

        PartDefinition spike_r5 = Torso_spikes.addOrReplaceChild("spike_r5", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, 0.75F, 7.0F, 0.0F, 0.0F, 1.309F));

        PartDefinition spike_r6 = Torso_spikes.addOrReplaceChild("spike_r6", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(8.0F, -5.25F, 7.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition spike_r7 = Torso_spikes.addOrReplaceChild("spike_r7", CubeListBuilder.create().texOffs(0, 105).addBox(1.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 1.75F, 7.0F, 0.0F, 0.0F, -1.309F));

        PartDefinition spike_r8 = Torso_spikes.addOrReplaceChild("spike_r8", CubeListBuilder.create().texOffs(0, 105).addBox(1.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, -4.25F, 7.0F, 0.0F, 0.0F, -1.3963F));

        PartDefinition spike_r9 = Torso_spikes.addOrReplaceChild("spike_r9", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -8.25F, 7.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition spike_r10 = Torso_spikes.addOrReplaceChild("spike_r10", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -8.25F, 7.0F, 0.0F, 0.0F, 0.1745F));

        PartDefinition spike_r11 = Torso_spikes.addOrReplaceChild("spike_r11", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -8.25F, 7.0F, 0.0F, 0.0F, 0.0873F));

        PartDefinition spike_r12 = Torso_spikes.addOrReplaceChild("spike_r12", CubeListBuilder.create().texOffs(0, 105).addBox(0.0F, -5.0F, -8.0F, 0.0F, 5.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.25F, 7.0F, 0.0F, 0.0F, -0.0873F));

        PartDefinition Head = Torso.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 56).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 115).addBox(0.0F, -4.95F, -6.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.75F, -9.25F));

        PartDefinition left_back_leg = Body.addOrReplaceChild("left_back_leg", CubeListBuilder.create().texOffs(0, 33).addBox(-2.25F, 0.0F, -2.25F, 4.5F, 6.0F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(4.75F, 4.0F, 5.0F));

        PartDefinition right_back_leg = Body.addOrReplaceChild("right_back_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, 0.0F, -2.25F, 4.5F, 6.0F, 4.5F, new CubeDeformation(0.0F)), PartPose.offset(-4.75F, 4.0F, 5.0F));

        PartDefinition right_front_leg = Body.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(28, 33).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 4.0F, -5.25F));

        PartDefinition left_front_leg = Body.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(16, 33).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 4.0F, -5.25F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public void setupAnim(
            RockspineEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        if (entity.defenseAnimationState.isStarted()) {
            this.animate(
                    entity.defenseAnimationState,
                    RockspineAnimations.rockspine_hide,
                    ageInTicks,
                    1f
            );
        } else if (entity.smashAnimationState.isStarted()) {
            this.animate(
                    entity.smashAnimationState,
                    RockspineAnimations.rockspine_smash,
                    ageInTicks,
                    1f
            );
        } else {
            this.animateWalk(
                    RockspineAnimations.rockspine_walk,
                    limbSwing,
                    limbSwingAmount,
                    2f,
                    2.5f
            );

            this.animate(
                    entity.idleAnimationState,
                    RockspineAnimations.rockspine_idle,
                    ageInTicks,
                    1f
            );

            this.animate(
                    entity.attackAnimationState,
                    RockspineAnimations.rockspine_attack,
                    ageInTicks,
                    1f
            );
        }
    }


    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.Head.yRot = headYaw * ((float)Math.PI / 180f);
        this.Head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}