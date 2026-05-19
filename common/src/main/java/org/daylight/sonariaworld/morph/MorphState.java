package org.daylight.sonariaworld.morph;

import net.minecraft.resources.Identifier;

public class MorphState {

    private boolean morphed;
    private Identifier entityId;
    private int variant;

    public boolean isMorphed() {
        return morphed;
    }

    public void setMorphed(boolean morphed) {
        this.morphed = morphed;
    }

    public Identifier getEntityId() {
        return entityId;
    }

    public void setEntityId(Identifier entityId) {
        this.entityId = entityId;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }
}
