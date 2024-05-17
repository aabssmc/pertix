package lol.aabss.pertix.client;

import lol.aabss.pertix.elements.*;
import lol.aabss.pertix.elements.commands.JoinTime;
import lol.aabss.pertix.elements.commands.PlayerInfo;
import lol.aabss.pertix.elements.commands.ServerInfo;
import lol.aabss.pertix.elements.commands.WorldInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import java.util.*;

import static lol.aabss.pertix.Pertix.sendUpdateMessage;
import static lol.aabss.pertix.elements.HealthIndicators.*;
import static lol.aabss.pertix.elements.HidePlayers.*;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

    public static int JOIN_TIME=0;
    public static Timer JOIN_TIMER;

    @Override
    public void onInitializeClient() {
        //AutoJump.loadBinds();
        HidePlayers.loadBinds();
        HealthIndicators.loadBinds();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            JoinTime.register(dispatcher, registryAccess);
            PlayerInfo.register(dispatcher, registryAccess);
            ServerInfo.register(dispatcher, registryAccess);
            WorldInfo.register(dispatcher, registryAccess);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            // --
            while (hideplayersbind.wasPressed()) {
                playershidden = !playershidden;
                if (p != null) {
                    p.sendMessage(Text.literal((playershidden ? "§aenabled" : "§cdisabled") + " hide players"), true);
                }
            }
            // --
            while (renderingbind.wasPressed()) {
                renderingenabled = !renderingenabled;
                if (p != null) {
                    p.sendMessage(Text.literal((renderingenabled ? "§aenabled" : "§cdisabled") + " health indicators"), true);
                }
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->  {
            if (JOIN_TIMER != null){
                JOIN_TIMER.cancel();
                JOIN_TIMER = null;
                JOIN_TIME = 0;
            }
            JOIN_TIMER = new Timer();
            JOIN_TIMER.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (MinecraftClient.getInstance().getCurrentServerEntry() != null) {
                        JOIN_TIME = JOIN_TIME + 1;
                    } else {
                        JOIN_TIME = 0;
                        JOIN_TIMER.cancel();
                        JOIN_TIMER = null;
                    }
                }
            }, 0L, 1000L);
            sendUpdateMessage();
        });
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            JOIN_TIME=0;
            JOIN_TIMER.cancel();
            JOIN_TIMER = null;
        });
    }
}

