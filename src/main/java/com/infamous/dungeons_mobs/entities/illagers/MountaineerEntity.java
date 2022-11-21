package com.infamous.dungeons_mobs.entities.illagers;

import java.util.Map;

import com.google.common.collect.Maps;
import com.infamous.dungeons_libraries.entities.SpawnArmoredMob;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorSet;
import com.infamous.dungeons_libraries.utils.GoalUtils;
import com.infamous.dungeons_mobs.entities.AnimatableMeleeAttackMob;
import com.infamous.dungeons_mobs.goals.ApproachTargetGoal;
import com.infamous.dungeons_mobs.goals.BasicModdedAttackGoal;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class MountaineerEntity extends VindicatorEntity implements SpawnArmoredMob, IAnimatable, AnimatableMeleeAttackMob {

    private static final DataParameter<Byte> DATA_FLAGS_ID = EntityDataManager.defineId(MountaineerEntity.class, DataSerializers.BYTE);
    private AnimationFactory factory = new AnimationFactory(this);
    public int attackAnimationTick;
    public int attackAnimationLength = 7;
    public int attackAnimationActionPoint = 6;

    public MountaineerEntity(World worldIn) {
        super(ModEntityTypes.MOUNTAINEER.get(), worldIn);
    }

    public MountaineerEntity(EntityType<? extends MountaineerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        GoalUtils.removeGoal(this.goalSelector, MeleeAttackGoal.class);
        this.goalSelector.addGoal(4, new BasicModdedAttackGoal<>(this, null, 20));
        this.goalSelector.addGoal(5, new ApproachTargetGoal(this, 0, 1.0D, true));
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.FOLLOW_RANGE, 16.0D).add(Attributes.MAX_HEALTH, 28.0D).add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    protected PathNavigator createNavigation(World p_175447_1_) {
        return new ClimberPathNavigator(this, p_175447_1_);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }

        this.tickDownAnimTimers();

    }

    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean p_70839_1_) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (p_70839_1_) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    protected float getSoundVolume() {
        return 0.5F;
    }

    protected SoundEvent getAmbientSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
    }

    public SoundEvent getCelebrateSound() {
        return ModSoundEvents.MOUNTAINEER_IDLE.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSoundEvents.MOUNTAINEER_DEATH.get();
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return ModSoundEvents.MOUNTAINEER_HURT.get();
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.MOUNTAINEER_AXE.get()));
        this.setItemSlot(EquipmentSlotType.HEAD, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getHead().get()));
        this.setItemSlot(EquipmentSlotType.CHEST, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getChest().get()));
        this.setItemSlot(EquipmentSlotType.LEGS, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getLegs().get()));
        this.setItemSlot(EquipmentSlotType.FEET, new ItemStack(ModItems.MOUNTAINEER_ARMOR.getFeet().get()));
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_,
                                           SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        ILivingEntityData iLivingEntityData = super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_,
                p_213386_5_);
        this.populateDefaultEquipmentSlots(p_213386_2_);
        this.populateDefaultEquipmentEnchantments(p_213386_2_);
        return iLivingEntityData;
    }

    public void applyRaidBuffs(int waveNumber, boolean bool) {
        ItemStack itemStack = new ItemStack(ModItems.MOUNTAINEER_AXE.get());
        Raid raid = this.getCurrentRaid();
        int i = 1;
        if (raid != null && waveNumber > raid.getNumGroups(Difficulty.NORMAL)) {
            i = 2;
        }

        boolean flag = false;
        if (raid != null) {
            flag = this.random.nextFloat() <= raid.getEnchantOdds();
        }
        if (flag) {
            Map<Enchantment, Integer> map = Maps.newHashMap();
            map.put(Enchantments.SHARPNESS, i);
            EnchantmentHelper.setEnchantments(map, itemStack);
        }

        this.setItemSlot(EquipmentSlotType.MAINHAND, itemStack);
    }

    @Override
    public ArmPose getArmPose() {
        ArmPose illagerArmPose = super.getArmPose();
        if (illagerArmPose == ArmPose.CROSSED) {
            return ArmPose.NEUTRAL;
        }
        return illagerArmPose;
    }

    @Override
    public boolean canBeLeader() {
        return false;
    }

    @Override
    public ArmorSet getArmorSet() {
        return ModItems.MOUNTAINEER_ARMOR;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 2, this::predicate));
    }

    public void handleEntityEvent(byte p_28844_) {
        if (p_28844_ == 4) {
            this.attackAnimationTick = attackAnimationLength;
        } else {
            super.handleEntityEvent(p_28844_);
        }
    }

    @Override
    public int getAttackAnimationTick() {
        return attackAnimationTick;
    }

    @Override
    public void setAttackAnimationTick(int attackAnimationTick) {
        this.attackAnimationTick = attackAnimationTick;
    }

    @Override
    public int getAttackAnimationLength() {
        return attackAnimationLength;
    }

    @Override
    public int getAttackAnimationActionPoint() {
        return attackAnimationActionPoint;
    }

    public void tickDownAnimTimers() {
        if (this.attackAnimationTick > 0) {
            this.attackAnimationTick--;
        }
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        String animation = "animation.vindicator";
        if (false) {
            animation += "_mcd";
        }
        String handSide = "_right";
        if(this.isLeftHanded()){
            handSide = "_left";
        }
        if(this.getMainHandItem().isEmpty()){
            handSide += "_both";
        }
        String crossed = "";
        if(IllagerArmsUtil.armorHasCrossedArms(this, this.getItemBySlot(EquipmentSlotType.CHEST))){
            crossed = "_crossed";
        }
        if (this.attackAnimationTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(animation + ".attack" + handSide, true));
        } else if (this.isAggressive() && !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".run" + handSide, true));
        } else if (!(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) {
            event.getController().setAnimation(new AnimationBuilder()
                    .addAnimation(animation + ".walk" + crossed, true));
        } else {
            if (this.isCelebrating()) {
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
