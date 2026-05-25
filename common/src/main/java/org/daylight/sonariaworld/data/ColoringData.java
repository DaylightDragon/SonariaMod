package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColoringData {
    private Map<String, ColorIdentifier> coloring;

    public static final Codec<ColoringData> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(

                    Codec.unboundedMap(
                                    Codec.STRING,
                                    ColorIdentifier.CODEC
                            ).optionalFieldOf("coloring", new HashMap<>())
                            .forGetter(ColoringData::getColoring)

            ).apply(instance, ColoringData::new));
}
