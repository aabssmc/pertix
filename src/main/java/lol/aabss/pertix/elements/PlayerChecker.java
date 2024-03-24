package lol.aabss.pertix.elements;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.world.World;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class PlayerChecker {

    public static boolean playerchecker = false;
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
        World world = MinecraftClient.getInstance().world;
        if (world == null || ModConfigs.CHECK_PLAYERS.isEmpty()){
            return null;
        }
        List<String> players = new ArrayList<>();
        world.getPlayers().forEach((playerEntity -> players.add(playerEntity.getName().getString().toLowerCase())));
        List<String> onlineplayers = new ArrayList<>();
        for (String p : ModConfigs.CHECK_PLAYERS){
            if (players.contains(p.toLowerCase())){
                onlineplayers.add(p);
            }
        }
        if (onlineplayers.isEmpty()) {
            return null;
        }
        return onlineplayers;
    }

    public static void saveConfig(List<String> newValue){
        ModConfigs.WHITELISTED_PLAYERS = newValue;
        ModConfigs.JSON.put("checkplayers", newValue);
        ModConfigs.saveConfig();
    }

}
