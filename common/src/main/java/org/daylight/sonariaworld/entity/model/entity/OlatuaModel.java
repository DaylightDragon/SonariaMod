package org.daylight.sonariaworld.entity.model.entity;

import net.minecraft.resources.Identifier;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.entity.species.OlatuaEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class OlatuaModel extends DefaultedEntityGeoModel<OlatuaEntity> {
    public OlatuaModel() {
        super(Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, "olatua"), "head");
    }
}
