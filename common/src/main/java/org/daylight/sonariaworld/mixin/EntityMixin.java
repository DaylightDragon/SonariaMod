package org.daylight.sonariaworld.mixin;

import net.minecraft.world.entity.*;
import org.daylight.sonariaworld.mixinrelated.PossibleGhostEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Inject(
            method = "shouldBeSaved",
            at = @At("HEAD"),
            cancellable = true
    )
    public void shouldBeSaved(CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "canCollideWith",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$canCollideWith(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "canBeCollidedWith",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sonaria$canBeCollidedWith(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if((Object) this instanceof PossibleGhostEntity ghostEntity) {
            if(ghostEntity.sonariaworld$isGhostEntity()) cir.setReturnValue(false);
        }
    }

//    @Inject(
//            method = "getAddEntityPacket",
//            at = @At("HEAD"),
//            cancellable = true
//    )
//    private void sonaria$preventPacket(ServerEntity serverEntity, CallbackInfoReturnable<Packet<ClientGamePacketListener>> cir) {
//        if((Object) this instanceof MorphProxyEntity) {
//            cir.cancel();
//        }
//    }

//    @Shadow
//    public abstract AABB getBoundingBox();
//
//    @Inject(
//            method = "isColliding",
//            at = @At("HEAD")
//    )
//    private void onIsColliding(BlockPos blockPos, BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
//        if ((Object) this instanceof Player player) {
//            System.out.println("Player AABB: " + player.getBoundingBox());
//        }
//    }
//
//    @ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true)
//    private Vec3 modifyMovement(Vec3 movement) {
//        Entity self = (Entity) (Object) this;
//        if (self instanceof Player player) {
//            // Проверяем, не вызовет ли движение коллизию
//            AABB currentBB = self.getBoundingBox();
//            AABB movedBB = currentBB.move(movement);
//
//            if (!self.level().noCollision(self, movedBB)) {
//                System.out.println("Would collide with movement: " + movement);
//                // Разрешаем движение только если НЕТ коллизий с новым размером
//                if (self.level().noCollision(self, currentBB)) {
//                    System.out.println("No collision with current position, allowing movement");
//                    return movement; // Разрешаем
//                }
//            }
//        }
//        return movement;
//    }
//
//    @Inject(method = "checkInsideBlocks", at = @At("HEAD"))
//    private void onCheckInsideBlocks(CallbackInfo ci) {
//        Entity self = (Entity) (Object) this;
//        if (self instanceof Player player && player.tickCount % 10 == 0) {
//            System.out.println("=== CHECK INSIDE BLOCKS ===");
//            System.out.println("Side: " + (self.level().isClientSide() ? "CLIENT" : "SERVER"));
//            System.out.println("AABB: " + self.getBoundingBox());
//
//            // Проверяем, какие блоки пересекаются с AABB
//            AABB aabb = self.getBoundingBox();
//            BlockPos.betweenClosed(
//                    (int) Math.floor(aabb.minX),
//                    (int) Math.floor(aabb.minY),
//                    (int) Math.floor(aabb.minZ),
//                    (int) Math.floor(aabb.maxX),
//                    (int) Math.floor(aabb.maxY),
//                    (int) Math.floor(aabb.maxZ)
//            ).forEach(blockPos -> {
//                BlockState state = self.level().getBlockState(blockPos);
//                if (!state.isAir()) {
//                    System.out.println("Block at " + blockPos + ": " + state);
//                }
//            });
//        }
//    }
//
//    @Inject(method = "getBoundingBox", at = @At("RETURN"))
//    private void onGetBoundingBox(CallbackInfoReturnable<AABB> cir) {
//        Entity self = (Entity) (Object) this;
//        if (self instanceof Player && self.tickCount % 20 == 0) {
//            System.out.println(
//                    "[" + (self.level().isClientSide() ? "CLIENT" : "SERVER") +
//                            "] getBoundingBox() returned: " + cir.getReturnValue()
//            );
//        }
//    }
}
