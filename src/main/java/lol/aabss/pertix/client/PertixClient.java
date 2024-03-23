package lol.aabss.pertix.client;

import lol.aabss.pertix.elements.AutoJump;
import lol.aabss.pertix.elements.HealthIndicators;
import lol.aabss.pertix.elements.HidePlayers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoJump.loadBinds();
        HidePlayers.loadBinds();
        HealthIndicators.loadBinds();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            while (AutoJump.getJumpBind().wasPressed()) {
                AutoJump.toggleJump();
                if (p != null) {
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal(AutoJump.getJumpBindToggle() ? "§ajump on" : "§cjump off"), false);
                }
            }
            AutoJump.autoJump(client, p);

            // --

            while (HidePlayers.getPlayersHiddenBind().wasPressed()){
                HidePlayers.toggleHidePlayers();
                if (p != null){
                    MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.literal(HidePlayers.getPlayersHidden() ? "§cplayers hidden" : "§aplayers shown"), false);
                }
            }

            ClientWorld world = client.world;
            if (world != null) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof PlayerEntity && !entity.equals(client.player)) {
                        entity.setInvisible(HidePlayers.getPlayersHidden());
                    }
                }
            }

            // --

            while (HealthIndicators.getRenderingBind().wasPressed()) {
                HealthIndicators.toggleRendering();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal((HealthIndicators.getRendering() ? "§aEnabled" : "§cDisabled") + " Health Indicators"), true);
                }
            }

            while (HealthIndicators.getHeartStackingBind().wasPressed()) {
                HealthIndicators.toggleHeartStacking();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal((HealthIndicators.getHeartStacking() ? "§aEnabled" : "§cDisabled") + " Heart Stacking"), true);
                }
            }
        });
    }


}

