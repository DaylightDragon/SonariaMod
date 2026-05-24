package org.daylight.sonariaworld.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreatureSurvivalStats {
    private float hp;
    private float stamina;
    private float hunger;
    private float thurs;
}
