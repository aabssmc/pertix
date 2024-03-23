package lol.aabss.pertix;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import lol.aabss.pertix.config.Config;
import lol.aabss.pertix.config.ModConfigs;
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
                    .setSaveConsumer(newValue -> ModConfigs.JUMP_OFFSET = newValue)
                    .build());
            generalCategory.addEntry(ConfigEntryBuilder.create()
                    .startBooleanToggle(Text.literal("(HEALTH INDICATORS) Show Mob Health"), ModConfigs.SHOW_MOB_HEALTH)
                    .setDefaultValue(() -> ModConfigs.SHOW_MOB_HEALTH)
                    .setSaveConsumer(newValue -> ModConfigs.SHOW_MOB_HEALTH = newValue)
                    .build());
            generalCategory.addEntry(ConfigEntryBuilder.create()
                    .startStrField(Text.literal("(HIDE PLAYERS) Whitelisted Players"), ModConfigs.WHITELISTED_PLAYERS)
                    .setDefaultValue(() -> ModConfigs.WHITELISTED_PLAYERS)
                            .setTooltip(Text.literal("SPLIT BY COMMAS"))
                    .setSaveConsumer(newValue -> ModConfigs.WHITELISTED_PLAYERS = newValue)
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
