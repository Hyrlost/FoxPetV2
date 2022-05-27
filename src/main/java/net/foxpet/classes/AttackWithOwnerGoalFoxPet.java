package net.foxpet.classes;

import net.foxpet.interfaces.IExtraFoxGoals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.passive.FoxEntity;

import java.util.EnumSet;

public class AttackWithOwnerGoalFoxPet extends TrackTargetGoal {
    private final Tameable tameable;

    private IExtraFoxGoals goals;

    private FoxEntity foxEntity;
    private LivingEntity attacking;
    private int lastAttackTime;

    public AttackWithOwnerGoalFoxPet(Tameable tameable, FoxEntity foxEntity, IExtraFoxGoals goals) {
        super(foxEntity, false);
        this.foxEntity = foxEntity;
        this.tameable = tameable;
        this.goals = goals;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    public boolean canStart() {
        if (this.goals.isTamed() && !this.foxEntity.isSitting()) {
            LivingEntity livingEntity = this.goals.getOwner();
            if (livingEntity == null) {
                return false;
            } else {
                this.attacking = livingEntity.getAttacking();
                int i = livingEntity.getLastAttackTime();
                return i != this.lastAttackTime && this.canTrack(this.attacking, TargetPredicate.DEFAULT) && this.goals.canAttackWithOwner(this.attacking, livingEntity);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.attacking);
        LivingEntity livingEntity = this.goals.getOwner();
        if (livingEntity != null) {
            this.lastAttackTime = livingEntity.getLastAttackTime();
        }

        super.start();
    }
}
