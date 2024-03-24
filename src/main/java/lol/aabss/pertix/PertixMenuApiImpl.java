package lol.aabss.pertix;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lol.aabss.pertix.config.ModConfigs;
import lol.aabss.pertix.elements.*;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PertixMenuApiImpl implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Pertix Mod Config"));

            ConfigCategory jump = builder.getOrCreateCategory(Text.literal("Auto Jump"));
            jump.addEntry(ConfigEntryBuilder.create()
                    .startDoubleField(Text.literal("(JUMP) Jump Offset"), ModConfigs.JUMP_OFFSET)
                    .setDefaultValue(() -> ModConfigs.JUMP_OFFSET)
                    .setSaveConsumer(AutoJump::saveConfig)
                    .build());

            ConfigCategory health = builder.getOrCreateCategory(Text.literal("Health Indicators"));
            health.addEntry(ConfigEntryBuilder.create()
                    .startBooleanToggle(Text.literal("(HEALTH INDICATORS) Show Mob Health"), ModConfigs.SHOW_MOB_HEALTH)
                    .setDefaultValue(() -> ModConfigs.SHOW_MOB_HEALTH)
                    .setSaveConsumer(HealthIndicators::saveConfig)
                    .build());

            ConfigCategory hide = builder.getOrCreateCategory(Text.literal("Hide Players"));
            hide.addEntry(ConfigEntryBuilder.create()
                    .startStrList(Text.literal("(HIDE PLAYERS) Whitelisted Players"), ModConfigs.WHITELISTED_PLAYERS)
                    .setDefaultValue(() -> ModConfigs.WHITELISTED_PLAYERS)
                    .setTooltip(Text.literal("The players that you can still see with hide players on."))
                    .setSaveConsumer(HidePlayers::saveConfig)
                    .build());

            ConfigCategory checker = builder.getOrCreateCategory(Text.literal("Player Checker"));
            checker.addEntry(ConfigEntryBuilder.create()
                    .startStrList(Text.literal("(PLAYER CHECKER) Checked Players"), ModConfigs.CHECK_PLAYERS)
                    .setDefaultValue(() -> ModConfigs.CHECK_PLAYERS)
                    .setTooltip(Text.literal("The players that will make a sound if they are online."))
                    .setSaveConsumer(PlayerChecker::saveConfig)
                    .build());

            return builder.build();
        };
    }


    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> factories = new HashMap<>();
        factories.put("category_name", parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Additional Config"));

            builder.getOrCreateCategory(Text.literal("Options")).addEntry(ConfigEntryBuilder.create()
                    .startIntField(Text.literal("Option Value"), 42)
                    .build());

            return builder.build();
        });

        return factories;
    }

    @Override
    public void attachModpackBadges(Consumer<String> consumer) {
        consumer.accept("modmenu");
    }
}
