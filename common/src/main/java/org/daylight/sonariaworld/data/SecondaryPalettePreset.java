package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SecondaryPalettePreset {
    NONE("none"),
    SUNSET("sunset"),
    FRUIT_SALAD("fruit_salad");

    private final String id;

    public static final Codec<SecondaryPalettePreset> CODEC =
            Codec.STRING.xmap(
                    SecondaryPalettePreset::valueOf,
                    SecondaryPalettePreset::name
            );
}
