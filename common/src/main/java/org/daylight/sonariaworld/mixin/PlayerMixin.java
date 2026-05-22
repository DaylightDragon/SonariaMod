package org.daylight.sonariaworld.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.MorphPlayerData;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements MorphPlayerData {
    @Unique
    private MorphState sonaria$morphState = new MorphState();

//    @Override
//    public MorphState sonaria$getMorphState() {
//        return sonaria$morphState;
//    }
//
//    @Override
//    public void sonaria$setMorphState(MorphState state) {
//        this.sonaria$morphState = state;
//    }

    @Inject(
            method = "canPlayerFitWithinBlocksAndEntitiesWhen",
            at = @At("TAIL")
    )
    private void sonaria$canPlayerFitWithinBlocksAndEntitiesWhen(Pose pose, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (LivingEntity)((Object) this);
        System.out.println("Player#canPlayerFitWithinBlocksAndEntitiesWhen");
        if(Minecraft.getInstance().level != null) System.out.println(Minecraft.getInstance().level.isClientSide() ? "Client side" : "Server side");
        System.out.println("Pose: " + pose);
        System.out.println("Dimensions: " + livingEntity.getDimensions(pose));
        System.out.println("Position: " + livingEntity.position());
        System.out.println("Checking for: " + livingEntity.getDimensions(pose).makeBoundingBox(livingEntity.position()).deflate(1.0E-7));
        System.out.println(cir.getReturnValue());
    }
}
