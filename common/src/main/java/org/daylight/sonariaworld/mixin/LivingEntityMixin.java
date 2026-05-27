package org.daylight.sonariaworld.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.mixinrelated.PossibleGhostEntity;
import org.daylight.sonariaworld.morph.MorphDimensions;
import org.daylight.sonariaworld.morph.MorphStateService;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements PossibleGhostEntity {
    @Unique
    private boolean sonaria$ghostEntity = false;

    @Inject(
            method = "getDimensions",
            at = @At("TAIL"),
            cancellable = true
    )
    private void sonaria$overrideDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        Entity self = (Entity)(Object)this;
//        System.out.println("sonaria$overrideDimensions");

        if (!(self instanceof Player player)) {
            return;
        }

        MorphState state = MorphStateService.get(player);
//        System.out.println("Overriding getDimensions for state: " + state);
        if (state != null && state.isMorphed() && state.getEntityIdentifier().isPresent()) {
            EntityDimensions dimensions = MorphDimensions.get(
                    BuiltInRegistries.ENTITY_TYPE.getValue(state.getEntityIdentifier().get()),
                    pose
            );

//            System.out.println("Returning dimensions " + dimensions);

            cir.setReturnValue(dimensions);
        } else {
            cir.setReturnValue(MorphDimensions.getNormalPlayerDimensions());
        }
    }

    @Override
    public boolean sonariaworld$isGhostEntity() {
        return sonaria$ghostEntity;
    }

    @Override
    public void sonariaworld$setGhostEntity(boolean ghostEntity) {
        this.sonaria$ghostEntity = ghostEntity;
    }

    @Inject(
            method = "hurtServer",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) cir.cancel();
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$tick(CallbackInfo ci) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) ci.cancel();
        }
    }

    @Inject(
            method = "aiStep",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$aiStep(CallbackInfo ci) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) ci.cancel();
        }
    }

    @Inject(
            method = "push",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$push(CallbackInfo ci) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) ci.cancel();
        }
    }

    @Inject(
            method = "pushEntities",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$pushEntities(CallbackInfo ci) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) ci.cancel();
        }
    }

    @Inject(
            method = "isPushable",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$isPushable(CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) cir.setReturnValue(false);
        }
    }
}
