package org.hiedacamellia.hanekage.client.config.json;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.neoforged.fml.loading.FMLPaths;
import org.hiedacamellia.hanekage.Hanekage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HanekageJsonHelper {

    private static final Map<String, JsonObject> jsonMap = new HashMap<>();

    public static void init(){
        Path path = FMLPaths.CONFIGDIR.get();
        if (!path.resolve(Hanekage.MODID).toFile().exists()) {
            path.resolve(Hanekage.MODID).toFile().mkdirs();
        }
        loadJson("config");
        SwordTrailConfig.load();
    }

    public static void reload() {
        jsonMap.clear();
        init();
    }
    public static void reload(String fileName) {
        if (jsonMap.containsKey(fileName)) {
            jsonMap.remove(fileName);
        }
        loadJson(fileName);
    }

    private static void loadJson(String fileName) {
        Path path = FMLPaths.CONFIGDIR.get().resolve(Hanekage.MODID).resolve(fileName+".json");
        if (!path.toFile().exists()) {
            Hanekage.LOGGER.debug("HanekageJsonHelper: File {} does not exist, skipping load.", fileName+".json");
            try {
                Files.writeString(path, "{}");
            } catch (IOException e) {
                Hanekage.LOGGER.debug("HanekageJsonHelper: Failed to create empty JSON file for {}: {}", fileName, e.getMessage());
            }
            return;
        }
        try {
            String content = Files.readString(path);
            jsonMap.put(fileName, GsonHelper.parse(content));
        } catch (IOException e) {
            Hanekage.LOGGER.debug("HanekageJsonHelper: Failed to load JSON file {}: {}", fileName, e.getMessage());
        } catch (com.google.gson.JsonSyntaxException e) {
            Hanekage.LOGGER.debug("HanekageJsonHelper: Invalid JSON syntax in file {}: {}", fileName, e.getMessage());
        }
    }

    public static JsonObject get(String key) {
        return jsonMap.get(key);
    }

    public static void save(String fileName, JsonObject jsonObject) {
        Path path = FMLPaths.CONFIGDIR.get().resolve(Hanekage.MODID).resolve(fileName + ".json");
        try {
            Files.writeString(path, jsonObject.toString());
        } catch (IOException e) {
            Hanekage.LOGGER.debug("HanekageJsonHelper: Failed to save JSON file {}: {}", fileName, e.getMessage());
        } catch (Exception e) {
            Hanekage.LOGGER.debug("HanekageJsonHelper: Unexpected error while saving JSON file {}: {}", fileName, e.getMessage());
        }
    }



}
