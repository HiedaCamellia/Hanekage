package org.hiedacamellia.hanekage.client.config.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import org.hiedacamellia.hanekage.Hanekage;

import java.util.HashMap;
import java.util.Map;

public class SwordTrailConfig {

    private static final Map<String, Pair<Integer, Integer>> swordTrailMap = new HashMap<>();
    private static int defaultTrailTime = 20;
    private static int defaultTrailColor = 0xFFFFFF;

    public static void load(){
        JsonObject swordTrail = HanekageJsonHelper.get("config");
        try{
            if(swordTrail.has("default_trail_color")) {
                defaultTrailColor = swordTrail.get("default_trail_color").getAsInt();
            }
            if(swordTrail.has("default_trail_time")) {
                defaultTrailTime = swordTrail.get("default_trail_time").getAsInt();
            }
            JsonArray asJsonArray = swordTrail.get("sword_trail").getAsJsonArray();
            for (int i = 0; i < asJsonArray.size(); i++) {
                JsonObject jsonObject = asJsonArray.get(i).getAsJsonObject();
                String itemId = jsonObject.get("bone_name").getAsString();
                int trail_time = jsonObject.get("trail_time").getAsInt();
                int color = jsonObject.get("color").getAsInt();
                swordTrailMap.put(itemId, new Pair<>(trail_time,color));
            }
            Hanekage.LOGGER.info("Hanekage loaded with " + swordTrailMap.size() + " SwordTrail configs.");
        } catch (Exception e) {
            Hanekage.LOGGER.debug("Hanekage load error: " + e.getMessage());
            save();
        }
    }

    public static void reload() {
        swordTrailMap.clear();
        HanekageJsonHelper.reload("config");
        load();
    }

    public static int getTrailTime(String bone_name) {
        Pair<Integer, Integer> pair = swordTrailMap.get(bone_name);
        return pair != null ? pair.getFirst() : defaultTrailTime;
    }

    public static int getTrailColor(String bone_name) {
        Pair<Integer, Integer> pair = swordTrailMap.get(bone_name);
        return pair != null ? pair.getSecond() : defaultTrailColor; // Default to white if not found
    }

    public static int getDefaultTrailTime() {
        return defaultTrailTime;
    }

    public static int getDefaultTrailColor() {
        return defaultTrailColor;
    }


    public static void save() {
        JsonArray jsonElements = new JsonArray();
        for (Map.Entry<String, Pair<Integer, Integer>> entry : swordTrailMap.entrySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("bone_name", entry.getKey());
            jsonObject.addProperty("trail_time", entry.getValue().getFirst());
            jsonObject.addProperty("color", entry.getValue().getSecond());
            jsonElements.add(jsonObject);
        }
        JsonObject object = new JsonObject();
        object.add("sword_trail", jsonElements);
        object.addProperty("default_trail_time", defaultTrailTime);
        object.addProperty("default_trail_color", defaultTrailColor);
        HanekageJsonHelper.save("config",object );
    }

}
