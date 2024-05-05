package lol.aabss.pertix.elements;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class FilterWords {
    public static boolean filterwordstoggle = true;
    public static KeyBinding filterwordsbind;

    public static void loadBinds() {
        filterwordsbind = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.pertix.filterwords",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_N,
                        "key.category.pertix"
                )
        );
    }

    public static Text filterText(Text text){
        Text newText = text;
        for (String s : ModConfigs.FILTERED_WORDS) {
            if (newText.getString().contains(s)){
                newText = Text.literal(newText.getString().replaceAll(s, "*"));
            }
        }
        return newText;
    }

    public static void saveWords(List<String> newvalue){
        ModConfigs.FILTERED_WORDS = newvalue;
        ModConfigs.JSON.put("filteredwords", newvalue);
        ModConfigs.saveConfig();
    }

    public static void saveFiller(String newvalue){
        ModConfigs.FILLER_WORD = newvalue;
        ModConfigs.JSON.put("fillerword", newvalue);
        ModConfigs.saveConfig();
    }

}
