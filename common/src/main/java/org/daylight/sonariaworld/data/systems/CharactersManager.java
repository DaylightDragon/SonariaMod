package org.daylight.sonariaworld.data.systems;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import org.daylight.sonariaworld.data.CreatureCharacter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class CharactersManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    /**
     * root/
     *   players/
     *      player-id-1.json
     *      player-id-2.json
     */
    private static Path rootFolder;

    private static boolean initialized = false;

    /**
     * playerId -> storage
     */
    private static final Map<String, PlayerCharacters> PLAYERS = new ConcurrentHashMap<>();

    private CharactersManager() {}

    public static void init(Path gameDir) {
        if (initialized) return;

        rootFolder = gameDir
                .resolve("sonariaworld")
                .resolve("characters");

        try {
            Files.createDirectories(rootFolder);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create character directory", e);
        }

        initialized = true;
    }

    // ADD

    public static CreatureCharacter addCharacter(
            String playerId,
            CreatureCharacter character
    ) {
        requireInit();

        PlayerCharacters storage = getOrLoad(playerId);

        UUID uuid = character.getUuid();

        if (uuid == null) {
            uuid = generateUniqueUUID(storage);
            character.setUuid(uuid);
        } else {
            if (storage.byUuid.containsKey(uuid)) {
                throw new IllegalArgumentException(
                        "Character UUID already exists for player: " + uuid
                );
            }
        }

        storage.characters.add(character);
        storage.byUuid.put(uuid, character);

        return character;
    }

    // GET

    public static List<CreatureCharacter> getCharacters(String playerId) {
        requireInit();

        return Collections.unmodifiableList(
                getOrLoad(playerId).characters
        );
    }

    public static CreatureCharacter getCharacter(
            String playerId,
            UUID uuid
    ) {
        requireInit();

        return getOrLoad(playerId)
                .byUuid
                .get(uuid);
    }

    /**
     * slower global search
     */
    public static CreatureCharacter findCharacter(UUID uuid) {
        requireInit();

        for (PlayerCharacters storage : PLAYERS.values()) {
            CreatureCharacter c = storage.byUuid.get(uuid);

            if (c != null) {
                return c;
            }
        }

        return null;
    }

    // REMOVE

    public static boolean removeCharacter(
            String playerId,
            UUID uuid
    ) {
        requireInit();

        PlayerCharacters storage = getOrLoad(playerId);

        CreatureCharacter removed = storage.byUuid.remove(uuid);

        if (removed == null) {
            return false;
        }

        storage.characters.remove(removed);

        return true;
    }

    // SAVE

    public static void savePlayer(String playerId) {
        requireInit();

        PlayerCharacters storage = getOrLoad(playerId);

        Path file = getPlayerFile(playerId);

        CharacterListFile wrapper = new CharacterListFile(
                storage.characters
        );

        try (Writer writer = Files.newBufferedWriter(file)) {

            var result = CharacterListFile.CODEC.encodeStart(
                    JsonOps.INSTANCE,
                    wrapper
            );

            result.resultOrPartial(System.err::println)
                    .ifPresent(json ->
                            GSON.toJson(json, writer)
                    );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save characters for player: " + playerId,
                    e
            );
        }
    }

    public static void saveAll() {
        requireInit();

        for (String playerId : PLAYERS.keySet()) {
            savePlayer(playerId);
        }
    }

    // LOAD

    public static void loadPlayer(String playerId) {
        requireInit();

        Path file = getPlayerFile(playerId);

        PlayerCharacters storage = new PlayerCharacters();

        if (!Files.exists(file)) {
            PLAYERS.put(playerId, storage);
            return;
        }

        try (Reader reader = Files.newBufferedReader(file)) {

            var json = GSON.fromJson(reader, com.google.gson.JsonElement.class);

            var result = CharacterListFile.CODEC.parse(
                    JsonOps.INSTANCE,
                    json
            );

            CharacterListFile loaded = result
                    .resultOrPartial(System.err::println)
                    .orElseGet(() -> new CharacterListFile(List.of()));

            for (CreatureCharacter character : loaded.characters()) {

                UUID uuid = character.getUuid();

                if (uuid == null) {
                    continue;
                }

                if (storage.byUuid.containsKey(uuid)) {
                    continue;
                }

                storage.characters.add(character);
                storage.byUuid.put(uuid, character);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load characters for player: " + playerId,
                    e
            );
        }

        PLAYERS.put(playerId, storage);
    }

    // INTERNAL

    private static PlayerCharacters getOrLoad(String playerId) {
        PlayerCharacters existing = PLAYERS.get(playerId);

        if (existing != null) {
            return existing;
        }

        loadPlayer(playerId);

        return PLAYERS.get(playerId);
    }

    private static UUID generateUniqueUUID(PlayerCharacters storage) {
        UUID uuid;

        do {
            uuid = UUID.randomUUID();
        } while (storage.byUuid.containsKey(uuid));

        return uuid;
    }

    private static Path getPlayerFile(String playerId) {
        return rootFolder.resolve(playerId + ".json");
    }

    private static void requireInit() {
        if (!initialized) {
            throw new IllegalStateException(
                    "CharactersManager not initialized"
            );
        }
    }

    // STORAGE

    private static final class PlayerCharacters {
        private final List<CreatureCharacter> characters =
                new ArrayList<>();

        private final Map<UUID, CreatureCharacter> byUuid =
                new HashMap<>();
    }

    // FILE WRAPPER

    public record CharacterListFile(
            List<CreatureCharacter> characters
    ) {
        public static final Codec<CharacterListFile> CODEC =
                CreatureCharacter.CODEC.listOf()
                        .xmap(
                                CharacterListFile::new,
                                CharacterListFile::characters
                        );
    }
}