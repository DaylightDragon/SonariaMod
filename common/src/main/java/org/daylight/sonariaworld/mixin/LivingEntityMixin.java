package org.daylight.sonariaworld.mixin;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.client.data.LateInitializations;
import org.daylight.sonariaworld.morph.MorphDimensions;
import org.daylight.sonariaworld.morph.MorphService;
import org.daylight.sonariaworld.morph.MorphState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
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

        MorphState state = MorphService.get(player);
//        System.out.println("Overriding getDimensions for state: " + state);
        if (state != null && state.isMorphed()) {
            EntityDimensions dimensions = MorphDimensions.get(
                    BuiltInRegistries.ENTITY_TYPE.getValue(state.getEntityIdentifier()),
                    pose
            );

            System.out.println("Returning dimensions " + dimensions);

            cir.setReturnValue(dimensions);
        } else {
            cir.setReturnValue(MorphDimensions.getNormalPlayerDimensions());
        }
    }
}
