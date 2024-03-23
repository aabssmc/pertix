package lol.aabss.pertix.config;

import com.mojang.datafixers.util.Pair;

public class ModConfigs {
    public static Config CONFIG;
    private static ModConfigProvider configs;

    public static double JUMP_OFFSET;
    public static boolean SHOW_MOB_HEALTH;

    public static void registerConfigs() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = Config.of("pertix").provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("key.pertix.jumpoffset", 0.001), "double");
        configs.addKeyValuePair(new Pair<>("key.pertix.showmobhealth", true), "boolean");
    }

    private static void assignConfigs() {
        JUMP_OFFSET = CONFIG.getOrDefault("key.pertix.jumpoffset", 0.001);
        SHOW_MOB_HEALTH = CONFIG.getOrDefault("key.pertix.showmobhealth", true);

        System.out.println("All " + configs.getConfigsList().size() + " have been set properly");
    }
}
