package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.world.entity.EntityType;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CreatureCharacter {
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
                    Codec.STRING.fieldOf("species_id")
                            .forGetter(CreatureCharacter::getSpeciesId),

                    ColoringData.CODEC.fieldOf("coloring")
                            .forGetter(CreatureCharacter::getColoring),

                    Codec.FLOAT.fieldOf("hp")
                            .forGetter(CreatureCharacter::getHp),

                    Codec.FLOAT.fieldOf("stamina")
                            .forGetter(CreatureCharacter::getStamina),

                    Codec.FLOAT.fieldOf("hunger")
                            .forGetter(CreatureCharacter::getHunger),

                    Codec.FLOAT.fieldOf("thirst")
                            .forGetter(CreatureCharacter::getThirst),

                    EFFECT_MAP_CODEC.fieldOf("effects")
                            .forGetter(CreatureCharacter::getAppliedEffectProperties)
            ).apply(instance, CreatureCharacter::new));
}
