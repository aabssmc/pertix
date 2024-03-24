package lol.aabss.pertix.elements;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import org.lwjgl.glfw.GLFW;

import static lol.aabss.pertix.config.ModConfigs.JUMP_OFFSET;

public class AutoJump {

    public static boolean jumpbindtoggle = false;
    public static KeyBinding jumpbind;

    public static void autoJump(MinecraftClient client, PlayerEntity p){
        if (jumpbindtoggle && p != null && client.world != null && p.isOnGround() && !p.isSneaking()) {
            Box playerBox = p.getBoundingBox();
            Box adjustedBox = playerBox.offset(0, -JUMP_OFFSET, 0).expand(-JUMP_OFFSET, 0, -JUMP_OFFSET);
            if (client.world.isSpaceEmpty(p, adjustedBox)) {
                p.jump();
            }
        }
    }

    public static void loadBinds(){
        jumpbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.togglejump",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_J,
                        "key.category.pertix"
                )
        );
    }

    public static void saveConfig(double offset){
        JUMP_OFFSET = offset;
        ModConfigs.JSON.put("jumpoffset", offset);
        ModConfigs.saveConfig();
    }
}
