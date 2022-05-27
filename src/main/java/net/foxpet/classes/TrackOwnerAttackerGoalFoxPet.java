package net.foxpet.classes;

import net.foxpet.interfaces.IExtraFoxGoals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.passive.FoxEntity;

import java.util.EnumSet;

public class TrackOwnerAttackerGoalFoxPet extends TrackTargetGoal {
    private final FoxEntity foxEntity;

    private IExtraFoxGoals goals;
    private LivingEntity attacker;
    private int lastAttackedTime;

    public TrackOwnerAttackerGoalFoxPet(FoxEntity foxEntity, IExtraFoxGoals goals) {
        super(foxEntity, false);
        this.goals = goals;
        this.foxEntity = foxEntity;
        this.setControls(EnumSet.of(Control.TARGET));
    }

    public boolean canStart() {
        if (this.goals.isTamed() && !this.foxEntity.isSitting()) {
            LivingEntity livingEntity = this.goals.getOwner();
            if (livingEntity == null) {
                return false;
            } else {
                this.attacker = livingEntity.getAttacker();
                int i = livingEntity.getLastAttackedTime();
                return i != this.lastAttackedTime && this.canTrack(this.attacker, TargetPredicate.DEFAULT) && this.goals.canAttackWithOwner(this.attacker, livingEntity);
            }
        } else {
            return false;
        }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity livingEntity = this.goals.getOwner();
        if (livingEntity != null) {
            this.lastAttackedTime = livingEntity.getLastAttackedTime();
        }

        super.start();
    }
}
