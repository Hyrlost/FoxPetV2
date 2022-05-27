package net.foxpet.interfaces;

import net.minecraft.entity.LivingEntity;

public interface IExtraFoxGoals {
    public void setBegging(boolean begging);

    public boolean isTamed();

    public LivingEntity getOwner();

    public boolean canAttackWithOwner(LivingEntity target, LivingEntity owner);
}
