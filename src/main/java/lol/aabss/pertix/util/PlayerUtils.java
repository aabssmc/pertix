package lol.aabss.pertix.util;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class PlayerUtils {

    private static final Identifier PLAYER_LIST_PACKET_ID = new Identifier("pertix", "player_list_packet");

    public static List<ServerPlayerEntity> getPertixPlayers(){
        IntegratedServer server = MinecraftClient.getInstance().getServer();
        if (server == null){
            return null;
        }
        List<ServerPlayerEntity> players = new ArrayList<>();
        for (ServerPlayerEntity p : server.getPlayerManager().getPlayerList()){
            if (ServerPlayNetworking.canSend(p, PLAYER_LIST_PACKET_ID)){
                players.add(p);
            }
        }
        return players;
    }

    public static Map<ServerPlayerEntity, String> getOnlineSpecialPlayers(@NotNull List<ServerPlayerEntity> normalPlayers){
        try {
            Map<String,Object> specialPlayers = new JSONObject(HttpClient.newHttpClient().sendAsync(HttpRequest.newBuilder()
                            .uri(new URI("https://raw.githubusercontent.com/aabssmc/boolean/main/people.json"))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            ).get().body()).toMap();
            Map<ServerPlayerEntity, String> onlineSpecialPlayers = new HashMap<>();
            for (ServerPlayerEntity p : normalPlayers) {
                if (specialPlayers.containsKey(p.getUuidAsString())) {
                    onlineSpecialPlayers.put(p, String.valueOf(specialPlayers.get(p.getUuidAsString())));
                }
            }
            return onlineSpecialPlayers;
        } catch (URISyntaxException | ExecutionException | InterruptedException ignored){}
        return null;
    }

    /*
    static {
        List<ServerPlayerEntity> pertix = getPertixPlayers();
        if (pertix != null) {
            Map<ServerPlayerEntity, String> map = getOnlineSpecialPlayers(pertix);
            if (map != null) {
                pertix.removeAll(map.keySet());
                for (ServerPlayerEntity p : pertix) {
                    map.put(p, "yellow");
                }
                map.keySet().forEach(serverPlayerEntity -> {
                    String color = map.get(serverPlayerEntity);
                    FabricLoader.getInstance().getModContainer("pertix").get().getMetadata().get
                    ScoreboardObjective belowname = serverPlayerEntity.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.BELOW_NAME);
                    if (belowname != null) {
                        belowname.setDisplayName(Text.of("symbol"+belowname.getDisplayName().getString()));
                    }
                    ScoreboardObjective list = serverPlayerEntity.getScoreboard().getObjectiveForSlot(ScoreboardDisplaySlot.LIST);
                    if (list != null) {
                        list.setDisplayName(Text.of("symbol"+list.getDisplayName().getString()));
                    }
                });
            }
        }
    }
     */

}
