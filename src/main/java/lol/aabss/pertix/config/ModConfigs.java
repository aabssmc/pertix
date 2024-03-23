package lol.aabss.pertix.config;

import net.fabricmc.loader.api.FabricLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class ModConfigs {
    public static File CONFIG;
    public static JSONObject JSON;

    public static double JUMP_OFFSET;
    public static boolean SHOW_MOB_HEALTH;
    public static List<String> WHITELISTED_PLAYERS = new ArrayList<>();

    public static void registerConfigs() {
        try {
            CONFIG = new File(FabricLoader.getInstance().getConfigDir().toString(), "pertix.json");
            if (CONFIG.createNewFile()) {
                JSONObject json = new JSONObject();
                json.put("jumpoffset", 0.001);
                json.put("showmobhealth", true);
                json.put("whitelistedplayers", List.of("Notch", "Dinnerbone"));

                JUMP_OFFSET = 0.001;
                SHOW_MOB_HEALTH = true;
                WHITELISTED_PLAYERS = List.of("Notch", "Dinnerbone");
                try (FileWriter fileWriter = new FileWriter(CONFIG.getPath())) {
                    fileWriter.write(json.toString(4));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSON = json;
            } else {
                loadJson();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void saveConfig(){
        try (FileWriter fileWriter = new FileWriter(CONFIG.getPath())) {
            fileWriter.write(JSON.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadJson(){
        try (FileReader fileReader = new FileReader(CONFIG.getPath())) {
            JSONTokener tokener = new JSONTokener(fileReader);
            try {
                JSON = new JSONObject(tokener);
            } catch (JSONException ignored){
                JSON = new JSONObject();
            }
            try {
                JUMP_OFFSET = JSON.getDouble("jumpoffset");
            } catch (JSONException ignored){
                JSON.put("jumpoffset", 0.001);
                JUMP_OFFSET = 0.001;
            }
            try {
                SHOW_MOB_HEALTH = JSON.getBoolean("showmobhealth");
            } catch (JSONException ignored){
                JSON.put("showmobhealth", true);
                SHOW_MOB_HEALTH = true;
            }
            try {
                JSONArray array = JSON.getJSONArray("whitelistedplayers");
                List<String> whitelistedplayers = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    whitelistedplayers.add(array.getString(i));
                }
                WHITELISTED_PLAYERS = whitelistedplayers;
            } catch (JSONException ignored){
                JSON.put("whitelistedplayers", List.of("Notch", "Dinnerbone"));
                WHITELISTED_PLAYERS = List.of("Notch", "Dinnerbone");
            }
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
