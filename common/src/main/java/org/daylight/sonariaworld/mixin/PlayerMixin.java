package org.daylight.sonariaworld.mixin;

import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.MorphPlayerData;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements MorphPlayerData {
    @Unique
    private MorphState sonaria$morphState = new MorphState();

    @Override
    public MorphState sonaria$getMorphState() {
        return sonaria$morphState;
    }

    @Override
    public void sonaria$setMorphState(MorphState state) {
        this.sonaria$morphState = state;
    }
}
