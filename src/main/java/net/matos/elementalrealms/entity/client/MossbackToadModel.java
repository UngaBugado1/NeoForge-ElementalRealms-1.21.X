package net.matos.elementalrealms.entity.client;// Made with Blockbench 5.2.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.matos.elementalrealms.entity.custom.MossbackToadEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class MossbackToadModel extends HierarchicalModel<MossbackToadEntity> {


    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart torso;
    private final ModelPart r_lung;
    private final ModelPart r_front_leg;
    private final ModelPart l_front_leg;
    private final ModelPart r_back_leg;
    private final ModelPart r_foot;
    private final ModelPart mushroom;
    private final ModelPart moss1;
    private final ModelPart moss2;
    private final ModelPart l_lung;
    private final ModelPart l_back_leg;
    private final ModelPart l_foot;

    public MossbackToadModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.torso = this.body.getChild("torso");
        this.r_lung = this.torso.getChild("r_lung");
        this.r_front_leg = this.torso.getChild("r_front_leg");
        this.l_front_leg = this.torso.getChild("l_front_leg");
        this.r_back_leg = this.torso.getChild("r_back_leg");
        this.r_foot = this.r_back_leg.getChild("r_foot");
        this.mushroom = this.torso.getChild("mushroom");
        this.moss1 = this.torso.getChild("moss1");
        this.moss2 = this.torso.getChild("moss2");
        this.l_lung = this.torso.getChild("l_lung");
        this.l_back_leg = this.torso.getChild("l_back_leg");
        this.l_foot = this.l_back_leg.getChild("l_foot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(-0.5F, 11.6174F, -3.5015F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 50).addBox(-5.0F, -5.0F, -12.0F, 10.0F, 3.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -0.5353F, -2.8637F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(78, 47).addBox(-2.5F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(69, 70).addBox(-11.5F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.5F, -5.0825F, -2.78F, -0.7854F, 0.0F, 0.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 29).addBox(-6.0F, 0.0F, -13.0F, 12.0F, 7.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        PartDefinition torso = body.addOrReplaceChild("torso", CubeListBuilder.create(), PartPose.offset(0.5F, 2.5353F, -3.1363F));

        PartDefinition cube_r2 = torso.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -4.5257F, -0.9109F, 14.0F, 11.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 0.5F, -0.2618F, 0.0F, 0.0F));

        PartDefinition r_lung = torso.addOrReplaceChild("r_lung", CubeListBuilder.create(), PartPose.offset(-5.5F, -2.7684F, 7.2594F));

        PartDefinition cube_r3 = r_lung.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(45, 50).mirror().addBox(-7.0F, -7.0F, -5.0F, 5.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.5F, 2.7684F, -1.2594F, -0.2618F, 0.0F, 0.0F));

        PartDefinition r_front_leg = torso.addOrReplaceChild("r_front_leg", CubeListBuilder.create().texOffs(78, 56).addBox(-1.25F, 0.0497F, -1.6147F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(63, 0).addBox(-4.25F, 7.9503F, -5.8853F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.25F, 1.8476F, -1.4579F));

        PartDefinition l_front_leg = torso.addOrReplaceChild("l_front_leg", CubeListBuilder.create().texOffs(69, 79).addBox(-1.0F, 0.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(51, 29).addBox(-3.0F, 7.9006F, -5.7705F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, 1.8973F, -1.5726F));

        PartDefinition r_back_leg = torso.addOrReplaceChild("r_back_leg", CubeListBuilder.create(), PartPose.offset(-7.0F, 0.298F, 15.1569F));

        PartDefinition cube_r4 = r_back_leg.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(48, 70).addBox(1.0F, -1.0F, 2.0F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, -0.298F, -5.1569F, -0.2618F, 0.0F, 0.0F));

        PartDefinition r_foot = r_back_leg.addOrReplaceChild("r_foot", CubeListBuilder.create().texOffs(63, 18).addBox(-2.5F, -0.75F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(51, 38).addBox(-3.5F, 0.25F, -11.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.25F, -0.5F));

        PartDefinition mushroom = torso.addOrReplaceChild("mushroom", CubeListBuilder.create().texOffs(116, 9).addBox(-3.0F, -9.3401F, -1.0357F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -5.8302F, 5.4916F, -0.2618F, 0.0F, 0.0F));

        PartDefinition cube_r5 = mushroom.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(116, 9).addBox(-5.75F, -4.5F, 0.0F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 1.75F, 0.0F, -1.5708F, 0.0F));

        PartDefinition moss1 = torso.addOrReplaceChild("moss1", CubeListBuilder.create().texOffs(116, 0).addBox(-3.1581F, -9.3603F, -0.2199F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0584F, -3.0555F, 14.2252F, -0.4247F, -0.0779F, -0.2956F));

        PartDefinition cube_r6 = moss1.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(116, 0).addBox(-5.75F, -4.5F, 0.0F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1581F, -5.0201F, 2.5659F, 0.0F, -1.5708F, 0.0F));

        PartDefinition moss2 = torso.addOrReplaceChild("moss2", CubeListBuilder.create().texOffs(116, 0).addBox(-3.0F, -9.3401F, -1.0357F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7059F, -2.6303F, 14.9961F, -0.5087F, 0.1298F, 0.228F));

        PartDefinition cube_r7 = moss2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(116, 0).addBox(-2.75F, -9.5F, 0.0F, 6.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -1.25F, 0.0F, -1.5708F, 0.0F));

        PartDefinition l_lung = torso.addOrReplaceChild("l_lung", CubeListBuilder.create(), PartPose.offset(5.5F, -2.7684F, 7.2594F));

        PartDefinition cube_r8 = l_lung.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(45, 50).addBox(2.0F, -7.0F, -5.0F, 5.0F, 8.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.5F, 2.7684F, -1.2594F, -0.2618F, 0.0F, 0.0F));

        PartDefinition l_back_leg = torso.addOrReplaceChild("l_back_leg", CubeListBuilder.create(), PartPose.offset(7.0F, 0.25F, 15.0F));

        PartDefinition cube_r9 = l_back_leg.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 66).addBox(-5.0F, -1.0F, 2.0F, 4.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -0.25F, -5.0F, -0.2618F, 0.0F, 0.0F));

        PartDefinition l_foot = l_back_leg.addOrReplaceChild("l_foot", CubeListBuilder.create().texOffs(21, 70).addBox(-2.5F, -0.75F, -7.0F, 5.0F, 1.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(63, 9).addBox(-3.5F, 0.25F, -11.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 9.298F, -0.3431F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }


    @Override
    public void setupAnim(
            MossbackToadEntity entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        entity.ensureAnimationStatesForPhase();

        List<ModelPart> parts = new ArrayList<>();
        this.root().getAllParts().forEach(parts::add);

        // Reset every part before applying an animation.
        this.root().getAllParts().forEach(ModelPart::resetPose);

        // Special animations take priority over idle and walking.
        switch (entity.getAnimationPhase()) {
            case MossbackToadEntity.PHASE_PUFF ->
                    this.animate(
                            entity.puffAnimationState,
                            MossbackToadAnimations.ANIM_MOSSBACK_TOAD_PUFF,
                            ageInTicks,
                            1.0F
                    );

            case MossbackToadEntity.PHASE_SLEEPING ->
                    this.animate(
                            entity.sleepingAnimationState,
                            MossbackToadAnimations.ANIM_MOSSBACK_TOAD_SLEEPING,
                            ageInTicks,
                            1.0F
                    );

            case MossbackToadEntity.PHASE_AWAKE ->
                    this.animate(
                            entity.awakeAnimationState,
                            MossbackToadAnimations.ANIM_MOSSBACK_TOAD_AWAKE,
                            ageInTicks,
                            1.0F
                    );

            default -> {
                // Calculate the idle pose.
                this.animate(
                        entity.idleAnimationState,
                        MossbackToadAnimations.ANIM_MOSSBACK_TOAD_IDLE,
                        ageInTicks,
                        1.0F
                );

                float[][] idlePose = capturePose(parts);

                // Calculate the walking pose.
                this.root().getAllParts().forEach(ModelPart::resetPose);

                this.animate(
                        entity.walkAnimationState,
                        MossbackToadAnimations.ANIM_MOSSBACK_TOAD_WALK,
                        ageInTicks,
                        1.0F
                );

                float[][] walkPose = capturePose(parts);

                // Blend between idle and walking.
                float blend = Mth.clamp(
                        entity.getWalkAnimationBlend(),
                        0.0F,
                        1.0F
                );

                for (int i = 0; i < parts.size(); i++) {
                    ModelPart part = parts.get(i);

                    part.x = Mth.lerp(blend, idlePose[i][0], walkPose[i][0]);
                    part.y = Mth.lerp(blend, idlePose[i][1], walkPose[i][1]);
                    part.z = Mth.lerp(blend, idlePose[i][2], walkPose[i][2]);

                    part.xRot = Mth.lerp(blend, idlePose[i][3], walkPose[i][3]);
                    part.yRot = Mth.lerp(blend, idlePose[i][4], walkPose[i][4]);
                    part.zRot = Mth.lerp(blend, idlePose[i][5], walkPose[i][5]);

                    part.xScale = Mth.lerp(blend, idlePose[i][6], walkPose[i][6]);
                    part.yScale = Mth.lerp(blend, idlePose[i][7], walkPose[i][7]);
                    part.zScale = Mth.lerp(blend, idlePose[i][8], walkPose[i][8]);
                }

                // Only track the player's view during normal behaviour.
                this.applyHeadRotation(netHeadYaw, headPitch);
            }
        }
    }


    private float[][] capturePose(List<ModelPart> parts) {
        float[][] pose = new float[parts.size()][9];

        for (int i = 0; i < parts.size(); i++) {
            ModelPart part = parts.get(i);

            pose[i][0] = part.x;
            pose[i][1] = part.y;
            pose[i][2] = part.z;

            pose[i][3] = part.xRot;
            pose[i][4] = part.yRot;
            pose[i][5] = part.zRot;

            pose[i][6] = part.xScale;
            pose[i][7] = part.yScale;
            pose[i][8] = part.zScale;
        }

        return pose;
    }


    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root()
    {
        return root;
    }
}