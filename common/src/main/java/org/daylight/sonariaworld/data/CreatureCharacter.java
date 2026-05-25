package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.EntityType;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreatureCharacter {
    private UUID uuid;

    // Visual
    private String speciesId;
    private ColoringData coloring;

    // Vitals
    private float hp;
    private float stamina;
    private float hunger;
    private float thirst;

    // Modificators
    private Map<AppliedEffect, AppliedEffectProperties> appliedEffectProperties;

    public static final Codec<Map<AppliedEffect, AppliedEffectProperties>> EFFECT_MAP_CODEC =
            Codec.unboundedMap(
                    AppliedEffect.CODEC,
                    AppliedEffectProperties.CODEC
            );

    public static final Codec<CreatureCharacter> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    UUIDUtil.CODEC.optionalFieldOf("id", UUID.randomUUID())
                            .forGetter(CreatureCharacter::getUuid),

                    Codec.STRING.optionalFieldOf("species_id", "sonariaworld:olatua")
                            .forGetter(CreatureCharacter::getSpeciesId),

                    ColoringData.CODEC.optionalFieldOf("coloring", new ColoringData())
                            .forGetter(CreatureCharacter::getColoring),

                    Codec.FLOAT.optionalFieldOf("hp", 1f)
                            .forGetter(CreatureCharacter::getHp),

                    Codec.FLOAT.optionalFieldOf("stamina", 10f)
                            .forGetter(CreatureCharacter::getStamina),

                    Codec.FLOAT.optionalFieldOf("hunger", 10f)
                            .forGetter(CreatureCharacter::getHunger),

                    Codec.FLOAT.optionalFieldOf("thirst", 10f)
                            .forGetter(CreatureCharacter::getThirst),

                    EFFECT_MAP_CODEC.optionalFieldOf("effects", new HashMap<>())
                            .forGetter(CreatureCharacter::getAppliedEffectProperties)
            ).apply(instance, CreatureCharacter::new));

    public CreatureCharacter init() {
        this.hp = 100;
        this.stamina = 100;
        this.hunger = 100;
        this.thirst = 100;
        return this;
    }
}
