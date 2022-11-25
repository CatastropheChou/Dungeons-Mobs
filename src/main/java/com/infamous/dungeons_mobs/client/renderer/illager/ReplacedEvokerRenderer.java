package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.ReplacedEvokerModel;
import com.infamous.dungeons_mobs.client.renderer.util.ModExtentedGeoReplacedEntityRenderer;
import com.infamous.dungeons_mobs.entities.illagers.ReplacedEvokerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nullable;
import java.util.List;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class ReplacedEvokerRenderer extends ModExtentedGeoReplacedEntityRenderer<ReplacedEvokerEntity> {
    public ReplacedEvokerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ReplacedEvokerModel(),
                new ReplacedEvokerEntity()
        );
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (this.isArmorBone(bone)) {
            bone.setCubesHidden(true);
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return bone.getName().startsWith("armor");
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, ReplacedEvokerEntity currentEntity) {
        return null;
    }

    @Override
    protected ItemStack getHeldItemForBone(String boneName, ReplacedEvokerEntity currentEntity) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return currentEntity.getMobEntity().isLeftHanded() ? mainHand : offHand;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return currentEntity.getMobEntity().isLeftHanded() ? offHand : mainHand;
            case DefaultBipedBoneIdents.POTION_BONE_IDENT:
                break;
        }
        return null;
    }

    @Override
    protected ItemCameraTransforms.TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
        switch (boneName) {
            case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
                return ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            default:
                return ItemCameraTransforms.TransformType.NONE;
        }
    }

    @Override
    protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, ReplacedEvokerEntity currentEntity, IBone bone) {
        if (item == this.mainHand || item == this.offHand) {
            stack.scale(1.1F, 1.1F, 1.1F);
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            boolean shieldFlag = item.getItem() instanceof ShieldItem;
            if (item == this.mainHand) {
                if (shieldFlag) {
                    stack.translate(0.0, 0.125, -0.25);
                } else {

                }
            } else {
                if (shieldFlag) {
                    stack.translate(-0.15, 0.125, 0.05);
                    stack.mulPose(Vector3f.YP.rotationDegrees(90));
                } else {

                }


            }
        }
    }

    @Override
    protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, ReplacedEvokerEntity currentEntity, IBone bone) {

    }

    @Override
    protected BlockState getHeldBlockForBone(String boneName, ReplacedEvokerEntity currentEntity) {
        return null;
    }

    @Override
    protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
                                  ReplacedEvokerEntity currentEntity) {

    }

    @Override
    protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
                                   ReplacedEvokerEntity currentEntity) {

    }

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, ReplacedEvokerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return boots;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return leggings;
            case "armorBipedBody":
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
                return chestplate;
            case "armorBipedHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, ReplacedEvokerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlotType.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlotType.LEGS;
            case "armorBipedRightHand":
                return !currentEntity.getMobEntity().isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedLeftHand":
                return currentEntity.getMobEntity().isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
            case "armorBipedBody":
                return EquipmentSlotType.CHEST;
            case "armorBipedHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorBipedModel) {
        switch (name) {
            case "armorBipedLeftFoot":
            case "armorBipedLeftLeg":
                return armorBipedModel.leftLeg;
            case "armorBipedRightFoot":
            case "armorBipedRightLeg":
                return armorBipedModel.rightLeg;
            case "armorBipedRightArm":
            case "armorIllagerRightArm":
                return armorBipedModel.rightArm;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
                return armorBipedModel.leftArm;
            case "armorBipedBody":
                return armorBipedModel.body;
            case "armorBipedHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }

    @Override
    protected void prepareArmorPositionAndScale(GeoBone bone, List<ModelRenderer.ModelBox> cubeList, ModelRenderer sourceLimb, MatrixStack stack, boolean geoArmor, boolean modMatrixRot) {
        super.prepareArmorPositionAndScale(bone, cubeList, sourceLimb, stack, geoArmor, modMatrixRot);
        if (bone.getName().equals("armorBipedHead") && geoArmor && helmet.getItem().getRegistryName().getNamespace().equals(MODID)) {
            stack.translate(0, 0.125, 0); // 1y is 1 cube up, we want 2/16
        }
    }
}