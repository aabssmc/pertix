package lol.aabss.pertix.elements;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class HidePlayers {

    private static boolean playershidden = false;
    private static KeyBinding hideplayersbind;

    public static void loadBinds(){
        hideplayersbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.hideplayers",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F10,
                        "key.category.pertix"
                )
        );
    }

    public static KeyBinding getPlayersHiddenBind(){
        return hideplayersbind;
    }

    public static boolean getPlayersHidden(){
        return playershidden;
    }

    public static void toggleHidePlayers(){
        playershidden = !playershidden;
    }


    public static List<Text> getWhitelistedPlayers(){
        List<Text> whitelistedlist = new ArrayList<>();
        String[] whitelisted = ModConfigs.WHITELISTED_PLAYERS.toArray(String[]::new);
        for (String player : whitelisted){
            whitelistedlist.add(Text.of(player.replaceAll(" ", "").toLowerCase()));
        }
        return whitelistedlist;
    }

    public static void saveConfig(List<String> newValue){
        ModConfigs.WHITELISTED_PLAYERS = newValue;
        ModConfigs.JSON.put("whitelistedplayers", newValue);
        ModConfigs.saveConfig();
    }
}
