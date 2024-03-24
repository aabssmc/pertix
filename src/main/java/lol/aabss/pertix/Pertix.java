package lol.aabss.pertix;

import lol.aabss.pertix.config.ModConfigs;
import net.fabricmc.api.ModInitializer;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Pertix implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConfigs.registerConfigs();
        LoggerFactory.getLogger("pertix").info("pertix is loading.");
    }

    public static String formatList(List<?> list){
        StringBuilder string = new StringBuilder();
        int i = 0;
        if (list.size() == 1){
            return list.get(0).toString();
        }
        for (Object obj : list){
            if (i == list.size() - 1) {
                string.append(obj);
            } else if (i == list.size() - 2) {
                string.append(obj).append(" and ");
            } else {
                string.append(obj).append(", ");
            }
            i++;
        }
        return string.toString();
    }
}
