package net.foxpet.classes;

import net.foxpet.interfaces.IExtraFoxGoals;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.passive.FoxEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class UntamedActiveTargetGoalFoxPet <T extends LivingEntity> extends ActiveTargetGoal<T> {
    private final Tameable tameable;
    private FoxEntity fox;

    private IExtraFoxGoals goals;

    public UntamedActiveTargetGoalFoxPet(Tameable tameable, FoxEntity fox, IExtraFoxGoals goals, Class<T> targetClass, boolean checkVisibility, @Nullable Predicate<LivingEntity> targetPredicate) {
        super(fox, targetClass, 10, checkVisibility, false, targetPredicate);
        this.tameable = tameable;
        this.fox = fox;
        this.goals = goals;
    }

    public boolean canStart() {
        return !this.goals.isTamed() && super.canStart();
    }

    public boolean shouldContinue() {
        return this.targetPredicate != null ? this.targetPredicate.test(this.mob, this.targetEntity) : super.shouldContinue();
    }
}