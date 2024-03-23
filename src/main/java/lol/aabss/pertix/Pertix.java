package lol.aabss.pertix;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;

public class Pertix implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfigs.registerConfigs();
        LoggerFactory.getLogger("pertix").info("pertix is loading.");
    }
}
