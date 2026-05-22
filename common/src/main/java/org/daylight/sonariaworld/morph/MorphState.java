package org.daylight.sonariaworld.morph;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;

public class MorphState {
    private boolean morphed = false;
    private Identifier entityIdentifier = null;
    private int variant = 0;
    private boolean dirty = false;

    public MorphState() {}

    public MorphState(boolean morphed, Identifier entityIdentifier, int variant) {
        this.morphed = morphed;
        this.entityIdentifier = entityIdentifier;
        this.variant = variant;
    }

    public boolean isMorphed() {
        return morphed;
    }

    public void setMorphed(boolean morphed) {
        this.morphed = morphed;
    }

    public Identifier getEntityIdentifier() {
        return entityIdentifier;
    }

    public void setEntityIdentifier(Identifier entityIdentifier) {
        this.entityIdentifier = entityIdentifier;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }
}
