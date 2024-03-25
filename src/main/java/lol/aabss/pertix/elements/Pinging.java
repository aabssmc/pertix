package lol.aabss.pertix.elements;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class Pinging {

    public static boolean pinging = false;
    public static KeyBinding pingingbind;

    public static void loadBinds(){
        pingingbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.pinging",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F10,
                        "key.category.pertix"
                )
        );
    }

    public static boolean containsPlayer(Text s){
        ClientPlayerEntity p = MinecraftClient.getInstance().player;
        if (p == null){
            return false;
        }
        return s.getString().toLowerCase().contains(p.getName().getString().toLowerCase());
    }

}
