package lol.aabss.pertix.client;

import lol.aabss.pertix.Pertix;
import lol.aabss.pertix.PertixMenuApiImpl;
import lol.aabss.pertix.elements.*;
import lol.aabss.pertix.elements.commands.JoinTime;
import lol.aabss.pertix.elements.commands.PlayerInfo;
import lol.aabss.pertix.elements.commands.ServerInfo;
import lol.aabss.pertix.elements.commands.WorldInfo;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import java.util.*;

import static lol.aabss.pertix.Pertix.sendUpdateMessage;
import static lol.aabss.pertix.elements.FilterWords.filterwordsbind;
import static lol.aabss.pertix.elements.FilterWords.filterwordstoggle;
import static lol.aabss.pertix.elements.HealthIndicators.*;
import static lol.aabss.pertix.elements.HidePlayers.*;
import static lol.aabss.pertix.elements.Pinging.*;
import static lol.aabss.pertix.elements.PlayerChecker.*;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

    public static int JOIN_TIME=0;
    public static Timer JOIN_TIMER;

    @Override
    public void onInitializeClient() {
        //AutoJump.loadBinds();
        HidePlayers.loadBinds();
        HealthIndicators.loadBinds();
        PlayerChecker.loadBinds();
        Pinging.loadBinds();
        FilterWords.loadBinds();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            JoinTime.register(dispatcher, registryAccess);
            PlayerInfo.register(dispatcher, registryAccess);
            ServerInfo.register(dispatcher, registryAccess);
            WorldInfo.register(dispatcher, registryAccess);
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            /*while (jumpbind.wasPressed()) {
                jumpbindtoggle = !jumpbindtoggle;
                if (p != null) {
                    p.sendMessage(Text.literal((jumpbindtoggle ? "§aenabled" : "§cdisabled")+" auto jump"), true);
                }
            }
            if (jumpbindtoggle) {
                autoJump(client, p);
            }*/
            // --
            while (hideplayersbind.wasPressed()){
                playershidden = !playershidden;
                if (p != null){
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
            // --
            while (playercheckerbind.wasPressed()) {
                playerchecker = !playerchecker;
                if (p != null) {
                    p.sendMessage(Text.literal((playerchecker ? "§aenabled" : "§cdisabled") + " player checker"), true);
                }
            }
            // --
            while (pingingbind.wasPressed()) {
                pinging = !pinging;
                if (p != null) {
                    p.sendMessage(Text.literal((pinging ? "§aenabled" : "§cdisabled") + " chat pinging"), true);
                }
            }
            // --
            while (filterwordsbind.wasPressed()) {
                filterwordstoggle = !filterwordstoggle;
                if (p != null) {
                    p.sendMessage(Text.literal((filterwordstoggle ? "§aenabled" : "§cdisabled") + " filtered words"), true);
                }
            }
        });

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (playerchecker){
                    ClientPlayerEntity p = MinecraftClient.getInstance().player;
                    if (p == null){
                        return;
                    }
                    List<String> players = checkForPlayers();
                    if (!players.isEmpty()){
                        p.sendMessage(Text.literal("§6[PERTIX] §e"+Pertix.formatList(players.toArray(String[]::new)) + (players.size() == 1 ? " is " : " are ")+"online!"));
                        p.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 10, 1);
                    }
                }
            }
        }, 0L, 11L*1000L);

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) ->  {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p == null){
                return true;
            }
            return !containsPlayer(message);
        });

        ClientReceiveMessageEvents.CHAT_CANCELED.register((message, signedMessage, sender, params, receptionTimestamp) ->  {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p == null){
                return;
            }
            p.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 10, 2);
            Text newtext;
            if (pinging) {
                if (filterwordstoggle) {
                    newtext = FilterWords.filterText(Text.literal(message.getString().replaceAll(p.getName().getString(), p.getName().getString() + "§r")));
                } else {
                    newtext = Text.literal(message.getString().replaceAll(p.getName().getString(), p.getName().getString() + "§r"));
                }
            } else {
                if (filterwordstoggle) {
                    newtext = FilterWords.filterText(message);
                } else {
                    newtext = message;
                }
            }
            p.sendMessage(newtext);
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

