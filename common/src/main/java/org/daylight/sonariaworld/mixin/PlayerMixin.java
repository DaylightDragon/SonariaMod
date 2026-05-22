package org.daylight.sonariaworld.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerMixin {
//    @Inject(
//            method = "canPlayerFitWithinBlocksAndEntitiesWhen",
//            at = @At("TAIL")
//    )
//    private void sonaria$canPlayerFitWithinBlocksAndEntitiesWhen(Pose pose, CallbackInfoReturnable<Boolean> cir) {
//        LivingEntity livingEntity = (LivingEntity)((Object) this);
//        System.out.println("Player#canPlayerFitWithinBlocksAndEntitiesWhen");
//        if(Minecraft.getInstance().level != null) System.out.println(Minecraft.getInstance().level.isClientSide() ? "Client side" : "Server side");
//        System.out.println("Pose: " + pose);
//        System.out.println("Dimensions: " + livingEntity.getDimensions(pose));
//        System.out.println("Position: " + livingEntity.position());
//        System.out.println("Checking for: " + livingEntity.getDimensions(pose).makeBoundingBox(livingEntity.position()).deflate(1.0E-7));
//        System.out.println(cir.getReturnValue());
//    }
}
