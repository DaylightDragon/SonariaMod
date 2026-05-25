package org.daylight.sonariaworld.data;

import com.mojang.serialization.Codec;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppliedEffect {
    COLD("cold"),
    BLEEDING("bleeding");

    private String id;

    public static final Codec<AppliedEffect> CODEC =
            Codec.STRING.xmap(
                    AppliedEffect::valueOf,
                    AppliedEffect::name
            );
}
