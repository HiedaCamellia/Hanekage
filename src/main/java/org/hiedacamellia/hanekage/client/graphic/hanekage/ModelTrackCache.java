package org.hiedacamellia.hanekage.client.graphic.hanekage;

import org.hiedacamellia.hanekage.client.util.ModelUtil;
import software.bernie.geckolib.cache.object.BakedGeoModel;

import java.util.List;

public record ModelTrackCache(List<ModelPath> tracks) {

    public static ModelTrackCache create(BakedGeoModel model) {
        return new ModelTrackCache(ModelUtil.getTrackBones(model));
    }
}
