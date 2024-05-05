package lol.aabss.pertix.config;

import net.fabricmc.loader.api.FabricLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModConfigs {
    public static File CONFIG;
    public static JSONObject JSON;

    //public static double JUMP_OFFSET;
    public static boolean SHOW_MOB_HEALTH;
    public static List<String> WHITELISTED_PLAYERS = new ArrayList<>();
    public static List<String> CHECK_PLAYERS = new ArrayList<>();
    public static List<String> FILTERED_WORDS = new ArrayList<>();
    public static String FILLER_WORD;

    public static void registerConfigs() {
        try {
            CONFIG = new File(FabricLoader.getInstance().getConfigDir().toString(), "pertix.json");
            if (CONFIG.createNewFile()) {
                JSONObject json = new JSONObject();
                //JUMP_OFFSET = put("jumpoffset", 0.001d, json);
                SHOW_MOB_HEALTH = put("showmobhealth", true, json);
                WHITELISTED_PLAYERS = put("whitelistedplayers", List.of("Notch", "Dinnerbone"), json);
                CHECK_PLAYERS = put("checkplayers", List.of("Skeppy", "BadBoyHalo"), json);
                FILTERED_WORDS = put("filteredwords", List.of("aabss sucks", "aabss is dumb"), json);
                FILLER_WORD = put("fillerword", "****", json);
                try (FileWriter fileWriter = new FileWriter(CONFIG.getPath())) {
                    fileWriter.write(json.toString(4));
                } catch (IOException e) {
                    throw new IOException(e);
                }
                JSON = json;
                return;
            } loadJson();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static void loadJson(){
        try (FileReader fileReader = new FileReader(CONFIG.getPath())) {
            try {
                JSON = new JSONObject(new JSONTokener(fileReader));
            } catch (JSONException ignored){
                JSON = new JSONObject();
            }
            //JUMP_OFFSET = Double.parseDouble(String.valueOf(loadObject("jumpoffset", 0.001d)));
            SHOW_MOB_HEALTH = loadObject("showmobhealth", true);
            WHITELISTED_PLAYERS = loadObject("whitelistedplayers", List.of("Notch", "Dinnerbone"));
            CHECK_PLAYERS = loadObject("checkplayers", List.of("Skeppy", "BadBoyHalo"));
            FILTERED_WORDS = loadObject("filteredwords", List.of("aabss sucks", "aabss is dumb"));
            FILLER_WORD = loadObject("fillerword", "****");
            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ---------------

    public static void saveConfig(){
        try (FileWriter fileWriter = new FileWriter(CONFIG.getPath())) {
            fileWriter.write(JSON.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T>T loadObject(String key, T defaultValue){
        try {
            if (defaultValue instanceof List<?>) {
                return (T) JSON.getJSONArray(key).toList();
            }
            return (T) JSON.get(key);
        } catch (JSONException ignored){
            JSON.put(key, defaultValue);
            return defaultValue;
        }
    }

    public static <T>T put(String key, T defaultValue, JSONObject json) {
        json.put(key, defaultValue);
        return defaultValue;
    }
}
