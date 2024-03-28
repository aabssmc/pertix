package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;



// purple boat if you know what i mena
// heh ya



import java.time.Duration;

import static lol.aabss.pertix.client.PertixClient.JOIN_TIME;

public class JoinTime {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(ClientCommandManager.literal("jointime").executes(context -> run()));
    }


    private static int run() {
        ClientPlayerEntity p = MinecraftClient.getInstance().player;
        ServerInfo s = MinecraftClient.getInstance().getCurrentServerEntry();
        if (s != null && p != null){
            String smalltime = time(JOIN_TIME, true);
            String bigtime = time(JOIN_TIME, false);
            p.sendMessage(
                    Text.literal("§6[PERTIX] §eYou have been online for ")
                            .append(Text.literal("§a"+smalltime)
                                    .setStyle(Style.EMPTY.withHoverEvent(
                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§a"+bigtime))
                                    ))
                            ).append(Text.literal("§e."))
            );
            return 1;
        }
        return -1;
    }

    public static String time(long seconds, boolean small) {
        if (small) {
            return Duration.ofSeconds(seconds)
                    .toString()
                    .substring(2)
                    .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                    .toLowerCase();
        }
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;
        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append(hours == 1 ? " hour" : " hours").append(minutes != 0 ? ", " : (remainingSeconds != 0 ? " and " : ""));
        }
        if (minutes > 0) {
            result.append(minutes).append(minutes == 1 ? " minute" : " minutes").append(remainingSeconds != 0 ? " and " : "");
        }
        if (remainingSeconds > 0) {
            result.append(remainingSeconds).append(remainingSeconds == 1 ? " second" : " seconds");
        }
        String finalResult = result.toString().trim();
        if (finalResult.isEmpty()) {
            return "0 seconds";
        }
        return finalResult;
    }


}
