package lol.aabss.pertix.elements;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

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
}
