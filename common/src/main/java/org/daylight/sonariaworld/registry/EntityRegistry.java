package org.daylight.sonariaworld.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.entity.species.OlatuaEntity;
import org.daylight.sonariaworld.entity.hitboxes.species.OlatuaHitboxes;
import org.daylight.sonariaworld.entity.hitboxes.SpeciesHitboxes;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class EntityRegistry {
    public static final Supplier<EntityType<OlatuaEntity>> OLATUA = registerEntity("olatua", OlatuaEntity::new, 0.7f, 1.3f, 0x1F1F1F, 0x0D0D0D);

    public static void init() {

    }

    public static void registerEntityAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier> registrar) {
        AttributeSupplier.Builder genericAttribs = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 1);
        AttributeSupplier.Builder genericMovingAttribs = PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25f);
        AttributeSupplier.Builder genericMonsterAttribs = Monster.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.25f)
                .add(Attributes.ATTACK_DAMAGE, 5)
                .add(Attributes.ATTACK_KNOCKBACK, 0.1);

        registrar.accept(EntityRegistry.OLATUA.get(), PathfinderMob.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.MOVEMENT_SPEED, 0.12D)
                .add(Attributes.MAX_HEALTH, 1)
                .build());
    }

    public static void registerHitboxPresets() {
        SpeciesHitboxes.register(OLATUA.get(), new OlatuaHitboxes());
    }

    private static <T extends Mob> Supplier<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> entity, float width, float height, int primaryEggColor, int secondaryEggColor) {
        return SonariaWorld.COMMON_PLATFORM.registerEntity(name, () -> EntityType.Builder.of(entity, MobCategory.CREATURE).sized(width, height).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, name))));
    }
}
