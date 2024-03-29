package lol.aabss.pertix.elements.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import lol.aabss.pertix.util.PlayerArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

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
            String message = """
                    §7- §6[PERTIX] §ePLAYER INFO §7-
                    §7Name: §e<NAME>
                    §7ID: §e<ID>
                    §7UUID: §e<UUID>
                    §7Custom Name: §e<CUSTOMNAME>
                    §7Location: §e<LOCATION>
                    §7Dimension: §e<DIMENSION>
                    §7Main Hand: §e<MAINHAND>
                    §7Luck: §e<LUCK>
                    §7§m----------------------"""
                    .replaceAll("<NAME>", player.getName().getString())
                    .replaceAll("<ID>", String.valueOf(player.getId()))
                    .replaceAll("<UUID>", player.getUuidAsString())
                    .replaceAll("<CUSTOMNAME>", (player.getCustomName() != null ? player.getCustomName().getString() : player.getName().getString()))
                    .replaceAll("<LOCATION>", getLocation(player))
                    .replaceAll("<DIMENSION>", player.getWorld().getRegistryKey().getValue().toString())
                    .replaceAll("<MAINHAND>", player.getMainArm().name())
                    .replaceAll("<LUCK>", String.valueOf(player.getLuck()));
            client.sendMessage(Text.literal(message));
            return 1;
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