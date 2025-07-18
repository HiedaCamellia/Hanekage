package org.hiedacamellia.hanekage.client.util;

import org.hiedacamellia.hanekage.client.graphic.hanekage.ModelPath;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;

import java.util.ArrayList;
import java.util.List;

public class ModelUtil {

    public static List<ModelPath> getTrackBones(BakedGeoModel model){
        List<ModelPath> bones = new ArrayList<>();
        List<GeoBone> geoBones = model.topLevelBones();
        for (GeoBone geoBone : geoBones) {
            bones.addAll(searchChildBones(geoBone));
        }
        return bones;
    }

    private static List<ModelPath> searchChildBones(GeoBone bone){
        List<ModelPath> bones = new ArrayList<>();
        for (GeoBone child : bone.getChildBones()) {
            bones.addAll(addBonePrefix(bone,searchChildBones(child)));
        }
        if (isTrackBone(bone)){
            bones.add(ModelPath.single(bone.getName()));
        }
        return bones;

    }

    private static List<ModelPath> addBonePrefix(GeoBone parent, List<ModelPath> bones) {
        List<ModelPath> prefixedBones = new ArrayList<>();
        for (ModelPath bone : bones) {
            prefixedBones.add(bone.prefix(parent.getName()));
        }
        return prefixedBones;
    }

    private static boolean isTrackBone(GeoBone bone) {
        return bone.getName().endsWith("-track");
    }

}
