package org.daylight.sonariaworld.morph;

public class PlayerMorphState {
    private boolean morphed;
    private boolean morphingChanged = false; // to keep track of dirty state

    public boolean isMorphed() { return morphed; }

    public void setMorphed(boolean value) {
        if (this.morphed != value) {
            this.morphed = value;
            this.morphingChanged = true;
        }
    }

    public boolean isDirty() { return morphingChanged; }
    public void clearDirty() { morphingChanged = false; }
}
