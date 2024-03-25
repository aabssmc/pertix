package lol.aabss.pertix.client;

import lol.aabss.pertix.Pertix;
import lol.aabss.pertix.elements.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.realms.Ping;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static lol.aabss.pertix.elements.AutoJump.*;
import static lol.aabss.pertix.elements.HealthIndicators.*;
import static lol.aabss.pertix.elements.HidePlayers.*;
import static lol.aabss.pertix.elements.Pinging.*;
import static lol.aabss.pertix.elements.PlayerChecker.*;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

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
                    p.sendMessage(Text.literal((jumpbindtoggle ? "§aenabled" : "§cdisabled")+" auto jump"), false);
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
                    if (players != null){
                        p.sendMessage(Text.literal(Pertix.formatList(players) + (players.size() == 1 ? " is " : " are ")+"online!"));
                        p.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 10, 1);
                    }
                }
            }
        }, 0L, 20L*1000L);

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

    }
}

