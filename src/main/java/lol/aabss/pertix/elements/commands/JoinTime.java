package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;









// purple boat if you know what i mena
// heh ya













import java.time.Duration;

import static lol.aabss.pertix.client.PertixClient.JOIN_TIME;

public class JoinTime {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("jointime")
                .executes(JoinTime::run)
        );
    }

    private static int run(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity p = context.getSource().getPlayer();
        MinecraftServer s = context.getSource().getServer();
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
