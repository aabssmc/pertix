package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class WorldInfo {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(
                ClientCommandManager.literal("worldinfo")
                        .executes(context -> run(context.getSource().getClient()))
        );
    }

    private static int run(MinecraftClient client) {
        World world = client.world;
        if (world == null){
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§6[PERTIX] §cNo server on!"));
            }
            return -1;
        }
        String message = """
                    §7- §6[PERTIX] §eWORLD INFO §7-
                    §7Name: §e<NAME>
                    §7Spawn: §e<SPAWN>
                    §7Difficulty: §e<DIFFICULTY>
                    §7Time: §e<TIME>
                    §7World Height: §e<WORLDHEIGHT>
                    §7World Border Size: §e<BORDERSIZE>
                    §7Moon Phase: §e<MOONPHASE>
                    §7Weather: §e<WEATHER>
                    §7§m----------------------"""
                .replaceAll("<NAME>", world.getRegistryKey().getValue().getPath())
                .replaceAll("<SPAWN>", getLocation(world))
                .replaceAll("<DIFFICULTY>", world.getLevelProperties().getDifficulty().name())
                .replaceAll("<TIME>", String.valueOf(world.getTime()))
                .replaceAll("<WORLDHEIGHT>", String.valueOf(world.getTopY()))
                .replaceAll("<BORDERSIZE>", String.valueOf(world.getWorldBorder().getSize()))
                .replaceAll("<MOONPHASE>", String.valueOf(world.getMoonPhase()))
                .replaceAll("<WEATHER>", getWeather(world));
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message));
        }
        return 1;
    }

    private static String getLocation(World w){
        BigDecimal x = BigDecimal.valueOf(w.getLevelProperties().getSpawnPos().getX()).setScale(2, RoundingMode.DOWN);
        BigDecimal y = BigDecimal.valueOf(w.getLevelProperties().getSpawnPos().getY()).setScale(2, RoundingMode.DOWN);
        BigDecimal z = BigDecimal.valueOf(w.getLevelProperties().getSpawnPos().getZ()).setScale(2, RoundingMode.DOWN);
        return x+", "+y+", "+z;
    }

    private static String getWeather(World w){
        return w.isRaining() ? "RAINING" : "THUNDERING";
    }
}
