package lol.aabss.pertix.client;

import lol.aabss.pertix.Pertix;
import lol.aabss.pertix.elements.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static lol.aabss.pertix.elements.AutoJump.*;
import static lol.aabss.pertix.elements.HealthIndicators.*;
import static lol.aabss.pertix.elements.HidePlayers.*;
import static lol.aabss.pertix.elements.Pinging.*;
import static lol.aabss.pertix.elements.PlayerChecker.*;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

    public static int JOIN_TIME=0;

    @Override
    public void onInitializeClient() {
        AutoJump.loadBinds();
        HidePlayers.loadBinds();
        HealthIndicators.loadBinds();
        PlayerChecker.loadBinds();
        Pinging.loadBinds();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPlayerEntity p = client.player;
            while (jumpbind.wasPressed()) {
                jumpbindtoggle = !jumpbindtoggle;
                if (p != null) {
                    p.sendMessage(Text.literal((jumpbindtoggle ? "§aenabled" : "§cdisabled")+" auto jump"), true);
                }
            }
            autoJump(client, p);
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
                        p.sendMessage(Text.literal("§6[PERTIX] §e"+Pertix.formatList(players) + (players.size() == 1 ? " is " : " are ")+"online!"));
                        p.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 10, 1);
                    }
                }
            }
        }, 0L, 11L*1000L);

        ClientReceiveMessageEvents.ALLOW_CHAT.register((message, signedMessage, sender, params, receptionTimestamp) ->  {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p == null){
                return true;
            }
            if (!pinging){
                return true;
            }
            return !containsPlayer(message);
        });

        ClientReceiveMessageEvents.CHAT_CANCELED.register((message, signedMessage, sender, params, receptionTimestamp) ->  {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p == null){
                return;
            }
            if (sender == null){
                p.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 10, 2);
                p.sendMessage(Text.literal(message.getString().replaceAll(p.getName().getString(), p.getName().getString() + "§r")));
            } else if (sender.getId() != p.getUuid()) {
                p.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 10, 2);
                p.sendMessage(Text.literal(message.getString().replaceAll(p.getName().getString(), p.getName().getString() + "§r")));
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->  {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (MinecraftClient.getInstance().getServer() != null) {
                        JOIN_TIME = JOIN_TIME + 1;
                    } else {
                        this.cancel();
                    }
                }
            }, 0L, 1000L);
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p != null) {
                String temp = newVersion();
                if (temp != null) {
                    String currentver = temp.split("\\|\\|")[0];
                    String newver = temp.split("\\|\\|")[1];
                    p.sendMessage(
                            Text.literal("§6[PERTIX]§r §eThere is a new update available!§r §7(v" + currentver + " -> v" + newver + ")")
                                    .setStyle(
                                            Style.EMPTY.withHoverEvent(
                                                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§eClick to open download."))
                                            ).withClickEvent(
                                                    new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/aabssmc/pertix/releases/tag/" + newver)
                                            )
                                    ),
                            false
                    );
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> JOIN_TIME=0);

    }

    public static String newVersion() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/aabssmc/pertix/releases/latest"))
                .build();
        try {
            String body = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get().body();
            String newver = new JSONObject(body).getString("tag_name");
            String currentver = FabricLoader.getInstance().getModContainer("pertix").get().getMetadata().getVersion().getFriendlyString();
            if (!Objects.equals(newver, currentver)) {
                return currentver+"||"+newver;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

