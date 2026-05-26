package org.daylight.sonariaworld.morph;

import lombok.*;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.daylight.sonariaworld.data.ClientMorphVisualsInfo;
import org.daylight.sonariaworld.data.InterpolatedCoords;
import org.daylight.sonariaworld.data.coordinatesystems.CoordinateSystemComponent;
import org.daylight.sonariaworld.entity.hitboxes.HitboxHolder;

import java.util.List;

/**
 * Client and server
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class MorphState {
    private String playerId;
    private boolean morphed = false;
    private Identifier entityIdentifier = null;
    private int variant = 0;
    private boolean dirty = false;
    private LivingEntity morphEntity;
    private ClientMorphVisualsInfo clientMorphVisualsInfo = new ClientMorphVisualsInfo().setState(this);
    private InterpolatedCoords realPlayerCoords = new InterpolatedCoords();
    private EntityDimensions realPlayerDimensions = EntityDimensions.fixed(0.5f, 0.5f); // temp initialization
}
