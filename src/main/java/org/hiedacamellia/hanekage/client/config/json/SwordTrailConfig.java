package org.hiedacamellia.hanekage.client.config.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import org.hiedacamellia.hanekage.Hanekage;

import java.util.HashMap;
import java.util.Map;

public class SwordTrailConfig {

    private static final Map<String, SwordTrail> swordTrailMap = new HashMap<>();
    private static int defaultTrailTime = 20;
    private static int defaultTrailColor = 0xFFFFFF;
    private static String defaultTrailInterpolationType = "lerp";
    private static int defaultTrailInterpolationSteps = 3;

    public static void load(){
        JsonObject swordTrail = HanekageJsonHelper.get("config");
        try{
            if(swordTrail.has("default_trail_color")) {
                defaultTrailColor = swordTrail.get("default_trail_color").getAsInt();
            }
            if(swordTrail.has("default_trail_time")) {
                defaultTrailTime = swordTrail.get("default_trail_time").getAsInt();
            }
            if(swordTrail.has("default_trail_interpolation_type")) {
                defaultTrailInterpolationType = swordTrail.get("default_trail_interpolation_type").getAsString();
            }
            if(swordTrail.has("default_trail_interpolation_steps")) {
                defaultTrailInterpolationSteps = swordTrail.get("default_trail_interpolation_steps").getAsInt();
            }
            JsonArray asJsonArray = swordTrail.get("sword_trail").getAsJsonArray();
            for (int i = 0; i < asJsonArray.size(); i++) {
                JsonObject jsonObject = asJsonArray.get(i).getAsJsonObject();
                String itemId = jsonObject.get("bone_name").getAsString();
                swordTrailMap.put(itemId, SwordTrail.load(jsonObject));
            }
            Hanekage.LOGGER.info("Hanekage loaded with " + swordTrailMap.size() + " SwordTrail configs.");
        } catch (Exception e) {
            Hanekage.LOGGER.error("Hanekage load error: " + e.getMessage());
//            save();
        }
    }

    public static void reload() {
        swordTrailMap.clear();
        HanekageJsonHelper.reload("config");
        load();
    }

    public static int getTrailTime(String bone_name) {
        SwordTrail swordTrail = swordTrailMap.get(bone_name);
        return swordTrail != null ? swordTrail.trail_time() : defaultTrailTime;
    }

    public static int getTrailColor(String bone_name) {
        SwordTrail swordTrail = swordTrailMap.get(bone_name);
        return swordTrail != null ? swordTrail.color() : defaultTrailColor; // Default to white if not found
    }

    public static ResourceLocation getTrailTexture(String bone_name) {
        SwordTrail swordTrail = swordTrailMap.get(bone_name);
        return swordTrail != null && swordTrail.hasTexture() ? swordTrail.texture() : null; // Return null if no texture is set
    }

    public static boolean hasTrailTexture(String bone_name) {
        SwordTrail swordTrail = swordTrailMap.get(bone_name);
        return swordTrail != null && swordTrail.hasTexture(); // Check if the trail has a texture
    }

    public static Interpolation getTrailInterpolation(String bone_name) {
        SwordTrail swordTrail = swordTrailMap.get(bone_name);
        if (swordTrail != null) {
            return swordTrail.interpolation();
        }
        return getDefaultInterpolation(); // Return default interpolation if not found
    }

    public static Interpolation getDefaultInterpolation() {
        return new Interpolation(defaultTrailInterpolationType,defaultTrailInterpolationSteps);
    }

    public static String getDefaultInterpolationType() {
        return defaultTrailInterpolationType;
    }

    public static int getDefaultInterpolationSteps() {
        return defaultTrailInterpolationSteps;
    }

    public static int getDefaultTrailTime() {
        return defaultTrailTime;
    }

    public static int getDefaultTrailColor() {
        return defaultTrailColor;
    }


    public static void save() {
        JsonArray jsonElements = new JsonArray();
        for (Map.Entry<String, SwordTrail> entry : swordTrailMap.entrySet()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("bone_name", entry.getKey());
            jsonElements.add(entry.getValue().save(jsonObject));
        }
        JsonObject object = new JsonObject();
        object.add("sword_trail", jsonElements);
        object.addProperty("default_trail_time", defaultTrailTime);
        object.addProperty("default_trail_color", defaultTrailColor);
        object.addProperty("default_trail_interpolation_type", defaultTrailInterpolationType);
        object.addProperty("default_trail_interpolation_steps", defaultTrailInterpolationSteps);
        HanekageJsonHelper.save("config",object );
    }

}
