package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ColorIdentifier {
    private SecondaryPalettePreset secondaryPalette = SecondaryPalettePreset.NONE;
    private int colorId;

    public static final Codec<ColorIdentifier> CODEC =
            RecordCodecBuilder.create(instance -> instance.group(
                    SecondaryPalettePreset.CODEC.fieldOf("secondaryPalette")
                            .forGetter(ColorIdentifier::getSecondaryPalette),

                    Codec.INT.fieldOf("colorId")
                            .forGetter(ColorIdentifier::getColorId)

            ).apply(instance, ColorIdentifier::new));
}
