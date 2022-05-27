package net.foxpet.classes;

import net.foxpet.interfaces.IExtraFoxGoals;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class FoxBegGoal
        extends Goal {
    private final FoxEntity fox;

    private final IExtraFoxGoals goals;
    @Nullable
    private PlayerEntity begFrom;
    private final World world;
    private final float begDistance;
    private int timer;
    private final TargetPredicate validPlayerPredicate;

    public FoxBegGoal(FoxEntity fox, IExtraFoxGoals goals, float begDistance) {
        this.fox = fox;
        this.goals = goals;
        this.world = fox.world;
        this.begDistance = begDistance;
        this.validPlayerPredicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(begDistance);
        this.setControls(EnumSet.of(Control.LOOK));
    }

    @Override
    public boolean canStart() {
        this.begFrom = this.world.getClosestPlayer(this.validPlayerPredicate, this.fox);
        if (this.begFrom == null) {
            return false;
        }
        return this.isAttractive(this.begFrom);
    }

    @Override
    public boolean shouldContinue() {
        if (!this.begFrom.isAlive()) {
            return false;
        }
        if (this.fox.squaredDistanceTo(this.begFrom) > (double)(this.begDistance * this.begDistance)) {
            return false;
        }
        return this.timer > 0 && this.isAttractive(this.begFrom);
    }

    @Override
    public void start() {
        this.goals.setBegging(true);
        this.timer = this.getTickCount(40 + this.fox.getRandom().nextInt(40));
    }

    @Override
    public void stop() {
        this.goals.setBegging(false);
        this.begFrom = null;
    }

    @Override
    public void tick() {
        this.fox.getLookControl().lookAt(this.begFrom.getX(), this.begFrom.getEyeY(), this.begFrom.getZ(), 10.0f, this.fox.getMaxLookPitchChange());
        --this.timer;
    }

    private boolean isAttractive(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack itemStack = player.getStackInHand(hand);
            if (this.goals.isTamed() && itemStack.isOf(Items.BONE)) {
                return true;
            }
            if (!this.fox.isBreedingItem(itemStack)) continue;
            return true;
        }
        return false;
    }
}

