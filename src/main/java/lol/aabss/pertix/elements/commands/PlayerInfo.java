package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import lol.aabss.pertix.util.PlayerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PlayerInfo {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(
                ClientCommandManager.literal("playerinfo")
                        .then(ClientCommandManager.argument("player", PlayerArgumentType.player())
                                .executes(context -> run(context.getSource().getPlayer(), PlayerArgumentType.getPlayer(context, "player"))))
                        .executes(context -> run(context.getSource().getPlayer(), context.getSource().getPlayer()))
        );
    }

    private static int run(ClientPlayerEntity client, PlayerEntity player) {
        if (player != null) {
            PlayerListEntry[] entry = new PlayerListEntry[1];
            client.networkHandler.getPlayerList().forEach(playerListEntry -> {
                if (playerListEntry.getProfile().getId().equals(player.getUuid())){
                    entry[0] = playerListEntry;
                }
            });
            if (entry[0] != null) {
                String message = """
                        §7- §6[PERTIX] §ePLAYER INFO §7-
                        §7Name: §e<NAME>
                        §7ID: §e<ID>
                        §7UUID: §e<UUID>
                        §7Custom Name: §e<CUSTOMNAME>
                        §7Location: §e<LOCATION>
                        §7Dimension: §e<DIMENSION>
                        §7Health: §e<HEALTH>
                        §7Main Hand: §e<MAINHAND>
                        §7Ping: §e<PING>
                        §7§m----------------------"""
                        .replaceAll("<NAME>", player.getName().getString())
                        .replaceAll("<ID>", String.valueOf(player.getId()))
                        .replaceAll("<UUID>", player.getUuidAsString())
                        .replaceAll("<CUSTOMNAME>", (player.getCustomName() != null ? player.getCustomName().getString() : player.getName().getString()))
                        .replaceAll("<LOCATION>", getLocation(player))
                        .replaceAll("<DIMENSION>", player.getWorld().getRegistryKey().getValue().toString())
                        .replaceAll("<HEALTH>", player.getHealth() + "/" + player.getMaxHealth())
                        .replaceAll("<MAINHAND>", player.getMainArm().name())
                        .replaceAll("<PING>", String.valueOf(entry[0].getLatency()));
                client.sendMessage(Text.literal(message));
                return 1;
            }
        }
        client.sendMessage(Text.literal("§cPlayer doesn't exist! (or you are not in a server)"));
        return -1;
    }
    private static String getLocation(PlayerEntity p){
        BigDecimal x = BigDecimal.valueOf(p.getX()).setScale(2, RoundingMode.DOWN);
        BigDecimal y = BigDecimal.valueOf(p.getY()).setScale(2, RoundingMode.DOWN);
        BigDecimal z = BigDecimal.valueOf(p.getZ()).setScale(2, RoundingMode.DOWN);
        return x+", "+y+", "+z;
    }
}