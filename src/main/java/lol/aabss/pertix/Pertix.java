package lol.aabss.pertix;

import lol.aabss.pertix.config.ModConfigs;
import lol.aabss.pertix.elements.commands.JoinTime;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;

public class Pertix implements ModInitializer {

    public static String[] newVersion;

    @Override
    public void onInitialize() {
        ModConfigs.registerConfigs();
        LoggerFactory.getLogger("pertix").info("pertix is loading.");
        ClientCommandRegistrationCallback.EVENT.register(JoinTime::register);
        newVersion = newVersion();
    }

    public static void sendUpdateMessage(){
        String[] temp = newVersion;
        ClientPlayerEntity p =  MinecraftClient.getInstance().player;
        if (temp.length > 1 && p != null) {
            String currentver = temp[0];
            String newver = temp[1];
            p.sendMessage(
                    Text.literal("§6[PERTIX]§r §eThere is a new update available!§r §7(v" + currentver + " -> v" + newver + ")")
                            .setStyle(
                                    Style.EMPTY.withHoverEvent(
                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("§eClick to open download."))
                                    ).withClickEvent(
                                            new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/aabssmc/pertix/releases/tag/" + newver)
                                    )
                            ),
                    false
            );
        }
    }

    public static String formatList(List<?> list){
        StringBuilder string = new StringBuilder();
        int i = 0;
        if (list.size() == 1){
            return list.get(0).toString();
        }
        for (Object obj : list){
            if (i == list.size() - 1) {
                string.append(obj);
            } else if (i == list.size() - 2) {
                string.append(obj).append(" and ");
            } else {
                string.append(obj).append(", ");
            }
            i++;
        }
        return string.toString();
    }


    public static String[] newVersion() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com/repos/aabssmc/pertix/releases/latest"))
                .build();
        try {
            String body = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).get().body();
            String newver = new JSONObject(body).getString("tag_name");
            String currentver = FabricLoader.getInstance().getModContainer("pertix").get().getMetadata().getVersion().getFriendlyString();
            if (!Objects.equals(newver, currentver)) {
                return new String[]{currentver, newver};
            }
            return new String[]{};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
