package lol.aabss.pertix;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lol.aabss.pertix.config.ModConfigs;
import lol.aabss.pertix.elements.AutoJump;
import lol.aabss.pertix.elements.HealthIndicators;
import lol.aabss.pertix.elements.HidePlayers;
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

            ConfigCategory generalCategory = builder.getOrCreateCategory(Text.literal("General"));
            generalCategory.addEntry(ConfigEntryBuilder.create()
                    .startDoubleField(Text.literal("(JUMP) Jump Offset"), ModConfigs.JUMP_OFFSET)
                    .setDefaultValue(() -> ModConfigs.JUMP_OFFSET)
                    .setSaveConsumer(AutoJump::saveConfig)
                    .build());
            generalCategory.addEntry(ConfigEntryBuilder.create()
                    .startBooleanToggle(Text.literal("(HEALTH INDICATORS) Show Mob Health"), ModConfigs.SHOW_MOB_HEALTH)
                    .setDefaultValue(() -> ModConfigs.SHOW_MOB_HEALTH)
                    .setSaveConsumer(HealthIndicators::saveConfig)
                    .build());
            generalCategory.addEntry(ConfigEntryBuilder.create()
                    .startStrList(Text.literal("(HIDE PLAYERS) Whitelisted Players"), ModConfigs.WHITELISTED_PLAYERS)
                    .setDefaultValue(() -> ModConfigs.WHITELISTED_PLAYERS)
                    .setSaveConsumer(HidePlayers::saveConfig)
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
