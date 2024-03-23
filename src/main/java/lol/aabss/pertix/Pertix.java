package lol.aabss.pertix;

import lol.aabss.pertix.config.ModConfigs;
import lol.aabss.pertix.elements.HealthIndicators;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.slf4j.LoggerFactory;

public class Pertix implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfigs.registerConfigs();
        LoggerFactory.getLogger("pertix").info("pertix is loading.");
    }
}
