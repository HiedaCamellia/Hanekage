package org.hiedacamellia.hanekage.client.config.json;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public record SwordTrail(int trail_time, int color, ResourceLocation texture,Interpolation interpolation,boolean autoPush) {

    public static SwordTrail load(JsonObject jsonObject){
        int trail_time = jsonObject.has("trail_time")? jsonObject.get("trail_time").getAsInt() : SwordTrailConfig.getDefaultTrailTime();
        int color = jsonObject.has("color")? jsonObject.get("color").getAsInt() : SwordTrailConfig.getDefaultTrailColor();
        ResourceLocation texture = jsonObject.has("texture")?ResourceLocation.tryParse(jsonObject.get("texture").getAsString()):null;
        Interpolation interpolation = jsonObject.has("interpolation")? Interpolation.load(jsonObject.get("interpolation").getAsJsonObject()):SwordTrailConfig.getDefaultInterpolation();
        boolean autoPush = jsonObject.has("auto_push")? jsonObject.get("auto_push").getAsBoolean() : true;
        return new SwordTrail(trail_time, color, texture, interpolation,autoPush);
    }

    public JsonObject save(JsonObject jsonObject) {
        jsonObject.addProperty("trail_time", trail_time);
        jsonObject.addProperty("color", color);
        if (texture != null) {
            jsonObject.addProperty("texture", texture.toString());
        }
        jsonObject.add("interpolation", interpolation.save(new JsonObject()));
        jsonObject.addProperty("auto_push", autoPush);
        return jsonObject;
    }

    public boolean hasTexture() {
        return texture != null;
    }
}
