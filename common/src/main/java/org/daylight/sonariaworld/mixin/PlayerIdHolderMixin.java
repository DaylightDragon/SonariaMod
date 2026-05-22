package org.daylight.sonariaworld.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.daylight.sonariaworld.ModOptions;
import org.daylight.sonariaworld.mixinrelated.IdHolder;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public class PlayerIdHolderMixin implements IdHolder {
    @Override
    public String sonaria$getId() {
        Player player = (Player)(Object) this;
        switch (ModOptions.getPlayerIdType()) {
            case UUID -> {
                return player.getStringUUID();
            }
            case USERNAME -> {
                return player.getGameProfile().name();
            }
            default -> {
                return null;
            }
        }
    }
}
