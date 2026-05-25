package org.daylight.sonariaworld.neoforge.events.special;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.data.CreatureCharacter;
import org.daylight.sonariaworld.data.systems.CharactersManager;
import org.daylight.sonariaworld.data.systems.SpeciesManager;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.daylight.sonariaworld.neoforge.network.handler.server.ServerMorphHandler;

import java.util.UUID;

public class CommandsHandler {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher =
                event.getDispatcher();

        dispatcher.register(
                Commands.literal("sonaria")

                        .then(
                                Commands.literal("createcharacter")

                                        .then(
                                                Commands.argument(
                                                                "species",
                                                                StringArgumentType.word()
                                                        )

                                                        .suggests((ctx, builder) -> {

                                                            ServerPlayer player =
                                                                    ctx.getSource().getPlayer();

                                                            if (player == null) {
                                                                return builder.buildFuture();
                                                            }

                                                            String playerId =
                                                                    ((IdHolder) player).sonaria$getId();

                                                            for (EntityType<?> species :
                                                                    SpeciesManager.getPlayerOwnedSpecies(playerId)) {

                                                                Identifier id =
                                                                        BuiltInRegistries.ENTITY_TYPE.getKey(species);

                                                                if (id != null) {
                                                                    builder.suggest(id.getPath());
                                                                }
                                                            }

                                                            return builder.buildFuture();
                                                        })

                                                        .executes(ctx -> {

                                                            ServerPlayer player =
                                                                    ctx.getSource().getPlayerOrException();

                                                            String playerId =
                                                                    ((IdHolder) player).sonaria$getId();

                                                            String species =
                                                                    StringArgumentType.getString(
                                                                            ctx,
                                                                            "species"
                                                                    );

                                                            ctx.getSource().sendSuccess(
                                                                    () -> Component.literal(
                                                                            "Species: " + species
                                                                    ),
                                                                    false
                                                            );

                                                            CharactersManager.addCharacter(playerId, new CreatureCharacter()
                                                                    .setSpeciesId(species)
                                                                    .init());

                                                            return 1;
                                                        })
                                        )
                        )

                        .then(
                                Commands.literal("spawnas")

                                        .then(
                                                Commands.argument(
                                                                "character",
                                                                StringArgumentType.word()
                                                        )

                                                        .suggests((ctx, builder) -> {

                                                            ServerPlayer player =
                                                                    ctx.getSource().getPlayer();

                                                            if (player == null) {
                                                                return builder.buildFuture();
                                                            }

                                                            String playerId =
                                                                    ((IdHolder) player).sonaria$getId();

                                                            for (CreatureCharacter character :
                                                                    CharactersManager.getCharacters(playerId)) {

                                                                builder.suggest(
                                                                        character.getUuid().toString()
                                                                );
                                                            }

                                                            return builder.buildFuture();
                                                        })

                                                        .executes(ctx -> {
                                                            ServerPlayer player =
                                                                    ctx.getSource().getPlayerOrException();

                                                            String playerId =
                                                                    ((IdHolder) player).sonaria$getId();

                                                            UUID uuid = UUID.fromString(
                                                                    StringArgumentType.getString(
                                                                            ctx,
                                                                            "character"
                                                                    )
                                                            );

                                                            CreatureCharacter character =
                                                                    CharactersManager.getCharacter(
                                                                            playerId,
                                                                            uuid
                                                                    );

                                                            ServerMorphHandler.morphPlayer(player, Identifier.fromNamespaceAndPath(SonariaWorld.MOD_ID, character.getSpeciesId()), 1);

                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}