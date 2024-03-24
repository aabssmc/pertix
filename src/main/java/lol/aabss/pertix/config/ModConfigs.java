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
    public static List<String> CHECK_PLAYERS = new ArrayList<>();
    public static void registerConfigs() {
        try {
            CONFIG = new File(FabricLoader.getInstance().getConfigDir().toString(), "pertix.json");
            if (CONFIG.createNewFile()) {
                JSONObject json = new JSONObject();
                JUMP_OFFSET = put("jumpoffset", 0.001d, json);
                SHOW_MOB_HEALTH = put("showmobhealth", true, json);
                WHITELISTED_PLAYERS = put("whitelistedplayers", List.of("Notch", "Dinnerbone"), json);
                CHECK_PLAYERS = put("checkplayers", List.of("Skeppy", "BadBoyHalo"), json);
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
            JUMP_OFFSET = Double.parseDouble(String.valueOf(loadObject("jumpoffset", 0.001d)));
            SHOW_MOB_HEALTH = loadObject("showmobhealth", true);
            WHITELISTED_PLAYERS = loadObject("whitelistedplayers", List.of("Notch", "Dinnerbone"));
            CHECK_PLAYERS = loadObject("checkplayers", List.of("Skeppy", "BadBoyHalo"));
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T>T loadObject(String key, T defaultValue){
        if (defaultValue instanceof List<?>) {
            try {
                JSONArray array = JSON.getJSONArray(key);
                List<String> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getString(i));
                }
                return (T) list;
            } catch (JSONException ignored) {
                JSON.put(key, defaultValue);
                return defaultValue;
            }
        } else{
            try {
                return (T) JSON.get(key);
            } catch (JSONException ignored){
                JSON.put(key, defaultValue);
                return defaultValue;
            }
        }
    }

    public static <T>T put(String key, T defaultValue, JSONObject json) {
        json.put(key, defaultValue);
        return defaultValue;
    }
}
