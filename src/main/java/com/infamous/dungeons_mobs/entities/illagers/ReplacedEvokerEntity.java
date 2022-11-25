package com.infamous.dungeons_mobs.entities.illagers;

import com.infamous.dungeons_mobs.client.renderer.util.IGeoReplacedEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class ReplacedEvokerEntity implements IAnimatable, IGeoReplacedEntity {

    public MobEntity entity;

    @Override
    public MobEntity getMobEntity(){
        return this.entity;
    }

    @Override
    public void setMobEntity(MobEntity mob){
        this.entity = mob;
    }

    private AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        String animation = "animation.evoker";
        if (false) {
            animation += "_mcd";
        }
        String crossed = "";
        if(IllagerArmsUtil.armorHasCrossedArms((EvokerEntity) this.entity, this.entity.getItemBySlot(EquipmentSlotType.CHEST))){
            crossed = ".crossed";
        }
        if (((EvokerEntity) this.entity).isCastingSpell()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".cast-spell", true));
        } else if (this.entity.isAggressive() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".run" + crossed, true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".walk" + crossed, true));
        } else {
            if (((EvokerEntity) this.entity).isCelebrating()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".win", true));
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".idle" + crossed, true));
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}