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
            // --

            while (HealthIndicators.getRenderingBind().wasPressed()) {
                HealthIndicators.toggleRendering();
                if (client.player != null) {
                    client.player.sendMessage(Text.literal((HealthIndicators.getRendering() ? "§aenabled" : "§cdisabled") + " health indicators"), true);
                }
            }
        });
    }


}

