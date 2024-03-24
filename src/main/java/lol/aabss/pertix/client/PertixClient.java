package lol.aabss.pertix.client;

import lol.aabss.pertix.Pertix;
import lol.aabss.pertix.elements.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static lol.aabss.pertix.elements.AutoJump.*;
import static lol.aabss.pertix.elements.HealthIndicators.*;
import static lol.aabss.pertix.elements.HidePlayers.*;
import static lol.aabss.pertix.elements.PlayerChecker.*;

@Environment(EnvType.CLIENT)
public class PertixClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoJump.loadBinds();
        HidePlayers.loadBinds();
        HealthIndicators.loadBinds();
        PlayerChecker.loadBinds();

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
    }
}

