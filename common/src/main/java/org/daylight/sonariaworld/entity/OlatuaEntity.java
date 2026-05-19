package org.daylight.sonariaworld.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.daylight.sonariaworld.ModAnimations;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.util.ClientUtil;
import software.bernie.geckolib.util.GeckoLibUtil;

public class OlatuaEntity extends PathfinderMob implements GeoEntity, IControllableEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private boolean testWalkingActive = false;

    public OlatuaEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.@NonNull ControllerRegistrar controllers) {
        controllers.add(
                // Add our flying animation controller
                new AnimationController<>("Main", 10, state -> state.setAndContinue(this.testWalkingActive ? ModAnimations.WALKING : ModAnimations.IDLE))
                        // Handle the custom instruction keyframe that is part of our animation json
                        .setCustomInstructionKeyframeHandler(animTest -> {
                            Player player = ClientUtil.getClientPlayer();

                            if (player != null)
                                player.displayClientMessage(Component.literal("KeyFraming"), true);
                        })
                // Add our generic living animation controller
//                DefaultAnimations.genericLivingController()
        );
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
//        super.registerGoals();
    }

    @Override
    public @NonNull InteractionResult interactAt(@NonNull Player player, @NonNull Vec3 hitPos, @NonNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND)
            this.testWalkingActive = !this.testWalkingActive;

        return super.interactAt(player, hitPos, hand);
    }

    @Override
    public @NonNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean isControlled() {
        return false;
    }
}
