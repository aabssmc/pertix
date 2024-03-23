package lol.aabss.pertix.elements;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HealthIndicators {

    private static KeyBinding renderingbind;
    private static KeyBinding heartstackingbind;

    private static boolean renderingenabled = false;
    private static boolean heartstackingenabled = false;

    public static void loadBinds(){
        renderingbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.renderingEnabled",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_DELETE,
                        "key.category.pertix"
                )
        );
        heartstackingbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.heartStackingEnabled",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_END,
                        "key.category.pertix"
                )
        );
    }

    public static KeyBinding getRenderingBind(){
        return renderingbind;
    }

    public static KeyBinding getHeartStackingBind(){
        return heartstackingbind;
    }

    // --

    public static void toggleRendering(){
        renderingenabled = !renderingenabled;
    }

    public static void toggleHeartStacking(){
        heartstackingenabled = !heartstackingenabled;
    }

    // --

    public static boolean getRendering(){
        return renderingenabled;
    }

    public static boolean getHeartStacking(){
        return heartstackingenabled;
    }

}
