package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppliedEffectProperties {
    private float durationLeft;

    public static final Codec<AppliedEffectProperties> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    Codec.FLOAT.optionalFieldOf("duration", 1f)
                            .forGetter(AppliedEffectProperties::getDurationLeft)

            ).apply(instance, AppliedEffectProperties::new));
}
