package lol.aabss.pertix.elements;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PlayerChecker {

    public static List<String> recent = new ArrayList<>();

    public static boolean playerchecker = true;
    public static KeyBinding playercheckerbind;

    public static void loadBinds(){
        playercheckerbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.toggleplayerchecker",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_APOSTROPHE,
                        "key.category.pertix"
                )
        );
    }

    public static List<String> checkForPlayers(){
        ServerInfo info = MinecraftClient.getInstance().getCurrentServerEntry();
        if (info == null || ModConfigs.CHECK_PLAYERS.isEmpty()){
            return List.of();
        }
        List<String> players = new ArrayList<>();
        info.playerListSummary.forEach((playerEntity -> players.add(playerEntity.getString().toLowerCase())));
        List<String> onlineplayers = new ArrayList<>();
        for (String p : ModConfigs.CHECK_PLAYERS){
            if (players.contains(p.toLowerCase())){
                if (!recent.contains(p)) {
                    onlineplayers.add(p);
                    recent.add(p);
                }
            } else {
                recent.remove(p);
            }
        }
        if (!onlineplayers.isEmpty()) {
            onlineplayers.removeAll(recent);
            onlineplayers.addAll(recent);
        }
        return onlineplayers;
    }

    public static void saveConfig(List<String> newValue){
        ModConfigs.CHECK_PLAYERS = newValue;
        ModConfigs.JSON.put("checkplayers", newValue);
        ModConfigs.saveConfig();
    }

}
