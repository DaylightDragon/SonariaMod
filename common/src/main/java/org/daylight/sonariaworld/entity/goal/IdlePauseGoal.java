package org.daylight.sonariaworld.entity.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class IdlePauseGoal extends Goal {
    private final Mob mob;
    private int time;

    public IdlePauseGoal(Mob mob) {
        this.mob = mob;
        this.time = 0;
    }

    @Override
    public boolean canUse() {
        return mob.getRandom().nextInt(100) < 5; // 5% шанс начать паузу
    }

    @Override
    public boolean canContinueToUse() {
        return time > 0;
    }

    @Override
    public void start() {
        time = 40 + mob.getRandom().nextInt(80); // 2–6 секунд
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        time--;
    }
}
