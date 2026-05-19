package org.daylight.sonariaworld.neoforge.events;

import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.daylight.sonariaworld.SonariaWorld;
import org.daylight.sonariaworld.morph.PlayerMorph;
import org.daylight.sonariaworld.morph.PlayerMorphState;

@EventBusSubscriber(modid = SonariaWorld.MOD_ID)
public class PlayerEvents {
    @SubscribeEvent
    public static void onSize(EntityEvent.Size event) {
        if (event.getEntity() instanceof Player player) {
            // Теперь состояние синхронизировано через сеть
            PlayerMorphState state = PlayerMorph.get(player);
            System.out.println("Morphed while EntitySize event: " + state.isMorphed());
            if (state.isMorphed()) {
                event.setNewSize(EntityDimensions.scalable(0.6F, 1.2F));
            } else {
                EntityDimensions.scalable(0.6F, 1.8F);
            }
//            player.refreshDimensions();
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        // У PlayerTickEvent нет phase, он вызывается один раз за тик
        if(event.getEntity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                PlayerMorphState state = PlayerMorph.get(player);
                if (state.isDirty()) {
                    player.refreshDimensions();
                    state.clearDirty();
                }
            }
        }
    }
}
