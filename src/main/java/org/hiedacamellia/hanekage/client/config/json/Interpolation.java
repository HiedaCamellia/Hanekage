package org.hiedacamellia.hanekage.client.config.json;

import com.google.gson.JsonObject;

public record Interpolation(String type, int steps) {

    public static Interpolation load(JsonObject jsonObject){
        String type = jsonObject.has("type")? jsonObject.get("type").getAsString() : SwordTrailConfig.getDefaultInterpolationType();
        int steps = jsonObject.has("steps")? jsonObject.get("steps").getAsInt() : SwordTrailConfig.getDefaultInterpolationSteps();
        return new Interpolation(type, steps);
    }

    public JsonObject save(JsonObject jsonObject) {
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("steps", steps);
        return jsonObject;
    }

}
