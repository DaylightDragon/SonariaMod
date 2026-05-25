package org.daylight.sonariaworld.data.systems;

import org.daylight.sonariaworld.data.CreatureCharacter;

import java.util.List;

public class CharactersManager {
    private static boolean initialized = false;

    private CharactersManager() {}

    public static void loadCharacters() {
        if(initialized) return;
        // something
        initialized = true;
    }

    public static List<CreatureCharacter> getCharactersPerPlayer(String playerId) {
        return null; // unfinished
    }
}
