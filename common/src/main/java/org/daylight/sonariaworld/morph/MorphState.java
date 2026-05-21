package org.daylight.sonariaworld.morph;

import net.minecraft.resources.Identifier;

public class MorphState {
    private boolean morphed;
    private Identifier entityId;
    private int variant;
    private boolean dirty;

    public MorphState() {}

    public MorphState(boolean morphed, Identifier entityId, int variant) {
        this.morphed = morphed;
        this.entityId = entityId;
        this.variant = variant;
    }

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

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    public static final MorphState EMPTY =
            new MorphState(false, null, 0);
}
