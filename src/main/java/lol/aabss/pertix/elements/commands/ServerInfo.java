package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;

public class ServerInfo {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(
                ClientCommandManager.literal("serverinfo")
                        .executes(context -> run(context.getSource().getClient()))
        );
    }

    private static int run(MinecraftClient client) {
        net.minecraft.client.network.ServerInfo info = client.getCurrentServerEntry();
        if (info == null){
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§6[PERTIX] §cNo server on!"));
            }
            return -1;
        }
        String message = """
                    §7- §6[PERTIX] §eSERVER INFO §7-
                    §7Name: §e<NAME>
                    §7Address: §e<ADDRESS>
                    §7Label: §e<LABEL>
                    §7Ping: §e<PING>
                    §7Player Count Label: §e<PLAYERLABEL>
                    §7Players: §e<PLAYERS>
                    §7Version: §e<VERSION>
                    §7Protocol Version: §e<PROTOCOL>
                    §7§m----------------------"""
                .replaceAll("<NAME>", info.name)
                .replaceAll("<ADDRESS>", info.address)
                .replaceAll("<LABEL>", info.label.getString())
                .replaceAll("<PING>", String.valueOf(info.ping))
                .replaceAll("<PLAYERLABEL>", info.playerCountLabel.getString())
                .replaceAll("<PLAYERS>", info.players.online()+"/"+info.players.max())
                .replaceAll("<VERSION>", info.version.getString())
                .replaceAll("<PROTOCOL>", String.valueOf(info.protocolVersion));
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message));
        }
        return 1;
    }
}