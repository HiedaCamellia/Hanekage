package org.hiedacamellia.hanekage.client.graphic.hanekage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.hiedacamellia.hanekage.client.config.json.SwordTrailConfig;
import org.hiedacamellia.hanekage.client.graphic.render.HanekageRenderer;
import org.hiedacamellia.hanekage.client.util.EntityUtil;
import org.hiedacamellia.hanekage.client.util.ItemUtil;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.slf4j.Logger;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HanekageManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final HashMap<String, ModelTrackCache> TRACK_CACHE = new HashMap<>();
    private static final HashMap<String, ResourceLocation> TEX_CACHE = new HashMap<>();

    public static void cacheModel(String name, BakedGeoModel model) {
        TRACK_CACHE.put(name, ModelTrackCache.create(model));
    }

    public static void cacheTexture(String name, GeoRenderer<?> renderer, Item item) {

        try {
            // 通过反射找到 getTextureLocation 方法

            Method method = renderer.getClass().getMethod("getTextureLocation", GeoAnimatable.class);

            // 反射调用，返回 ResourceLocation
            ResourceLocation texture = (ResourceLocation) method.invoke(renderer, item);

            TEX_CACHE.put(name, texture);

        } catch (Exception e) {
            LOGGER.error("Failed to cache texture for item: {}", ItemUtil.toString(item), e);
        }
    }

    public static void cacheTexture(String name, GeoRenderer<?> renderer, Entity entity) {

        try {
            // 通过反射找到 getTextureLocation 方法

            Method method = renderer.getClass().getMethod("getTextureLocation", GeoAnimatable.class);

            // 反射调用，返回 ResourceLocation
            ResourceLocation texture = (ResourceLocation) method.invoke(renderer, entity);

            TEX_CACHE.put(name, texture);

        } catch (Exception e) {
            LOGGER.error("Failed to cache texture for Entity: {}", EntityUtil.toString(entity), e);
        }
    }

    public static boolean hasCache(String name) {
        return TRACK_CACHE.containsKey(name) || TEX_CACHE.containsKey(name);
    }

    public static ModelTrackCache getCache(String name) {
        return TRACK_CACHE.get(name);
    }

    private static final HashMap<String, Map<UUID, HanekagePath>> PATH_CACHE = new HashMap<>();

    public static void pushHanekagePath(String name, BakedGeoModel model, Matrix4f matrix4f, UUID uuid) {
        getCache(name).tracks().forEach(modelPath -> {
            GeoBone parentBone = model.getBone(modelPath.first()).get();

            Vector4f parent_transform = parentBone.getLocalSpaceMatrix().transform(new Vector4f(parentBone.getPivotX()/16, parentBone.getPivotY()/16, parentBone.getPivotZ()/16, 1));

            for (String string : modelPath.subPath(1).path()) {
                GeoBone geoBone = model.searchForChildBone(parentBone, string);
                parent_transform = new Vector4f(parent_transform).add(getOffset(parentBone,geoBone,matrix4f));
                parentBone = geoBone;
            }
            //这里应用完track父级的所有变换

            GeoBone trackStartBone = parentBone;

            GeoBone trackEndBone = null;
            for (GeoBone childBone : parentBone.getChildBones()) {
                if (childBone.getName().endsWith("-trackstart")) {
                    trackStartBone = childBone;
                } else if (childBone.getName().endsWith("-trackend")) {
                    trackEndBone = childBone;
                }
            }
            if(trackEndBone == null){
                LOGGER.error("Track end bone not found for track: {}, cannot render path.", modelPath);
                return;
            }

            Vector4f offset_start = getOffset(parentBone, trackStartBone, matrix4f);
            Vector4f start = new Vector4f(parent_transform).add(offset_start);
            Vector4f offset_end = getOffset(parentBone, trackEndBone, matrix4f);
            Vector4f end = new Vector4f(parent_transform).add(offset_end);

            pushPoint(parentBone.getName(),uuid,
                    start,
                    end);
        });
//
//        getCache(name).tracks().forEach(modelPath -> {
//            GeoBone parentBone = model.getBone(modelPath.last()).get();
//
//            GeoBone trackStartBone = parentBone;
//
//            GeoBone trackEndBone = null;
//            for (GeoBone childBone : parentBone.getChildBones()) {
//                if (childBone.getName().endsWith("start")) {
//                    trackStartBone = childBone;
//                } else if (childBone.getName().endsWith("end")) {
//                    trackEndBone = childBone;
//                }
//            }
//
//            Matrix4f worldMatrix = new Matrix4f(matrix4f).mul(trackStartBone.getModelSpaceMatrix());
//
//            Vector4f transform = parentBone.getLocalSpaceMatrix().transform(new Vector4f(parentBone.getPivotX()/16, parentBone.getPivotY()/16, parentBone.getPivotZ()/16, 1));
//
//            Vector4f parent = new Vector4f(transform.x(), transform.y(), transform.z(), 1).mul(worldMatrix);
//
//            Vector4f offset_start = new Vector4f((parentBone.getPivotX()-trackStartBone.getPivotX())/16, (parentBone.getPivotY()-trackStartBone.getPivotY())/16, (parentBone.getPivotZ()-trackStartBone.getPivotZ())/16, 0)
//                    .rotateX(parentBone.getRotX())
//                    .rotateY(parentBone.getRotY())
//                    .rotateZ(parentBone.getRotZ())
//                    .mul(parentBone.getScaleX(), parentBone.getScaleY(), parentBone.getScaleZ(), 1)
//                    .mul(worldMatrix);
//
//            Vector4f start = new Vector4f(parent).add(offset_start);
//
//            Vector4f offset_end = new Vector4f((trackEndBone.getPivotX()-parentBone.getPivotX())/16, (trackEndBone.getPivotY()-parentBone.getPivotY())/16, (trackEndBone.getPivotZ()-parentBone.getPivotZ())/16, 0)
//                    .rotateX(parentBone.getRotX())
//                    .rotateY(parentBone.getRotY())
//                    .rotateZ(parentBone.getRotZ())
//                    .mul(parentBone.getScaleX(), parentBone.getScaleY(), parentBone.getScaleZ(), 1)
//                    .mul(worldMatrix);
//
//            Vector4f end = new Vector4f(parent).add(offset_end);
//
//            pushPoint(trackStartBone.getName(),uuid,
//                    start,
//                    end);
//        });


    }

    private static Vector4f getOffset(GeoBone parent,GeoBone child,Matrix4f matrix4f){
        Matrix4f worldMatrix = new Matrix4f(matrix4f).mul(parent.getModelSpaceMatrix());

        return new Vector4f((parent.getPivotX()-child.getPivotX())/16, (parent.getPivotY()-child.getPivotY())/16, (parent.getPivotZ()-child.getPivotZ())/16, 0)
                .rotateX(parent.getRotX())
                .rotateY(parent.getRotY())
                .rotateZ(parent.getRotZ())
                .mul(parent.getScaleX(), parent.getScaleY(), parent.getScaleZ(), 1)
                .mul(worldMatrix);
    }

    private static void pushPoint(String bone_name, UUID uuid, Vector4f start, Vector4f end) {
        if (!PATH_CACHE.containsKey(bone_name)) {
            PATH_CACHE.put(bone_name, new HashMap<>());
        }
        if (!PATH_CACHE.get(bone_name).containsKey(uuid)) {
            PATH_CACHE.get(bone_name).put(uuid, new HanekagePath(SwordTrailConfig.getTrailTime(bone_name),SwordTrailConfig.getTrailColor(bone_name)));
        }

        PATH_CACHE.get(bone_name).get(uuid).pushPoint(new Vector3f(start.x(), start.y(), start.z())
                , new Vector3f(end.x(), end.y(), end.z()));


    }


    public static void renderHanekage(PoseStack poseStack) {
        HanekageRenderer.startBatch();
        PATH_CACHE.forEach((string, map) -> map.forEach((uuid, hanekagePath) -> HanekageRenderer.renderInBatch(poseStack,hanekagePath)));
        HanekageRenderer.endBatch();

        popPoints();
    }

    private static void popPoints() {
        PATH_CACHE.forEach((bone_name, path) ->
                path.forEach((uuid, hanekagePath) ->
                        hanekagePath.popPoint()));
        for (String bone_name : PATH_CACHE.keySet()) {
            for (UUID uuid : PATH_CACHE.get(bone_name).keySet()) {
                if(PATH_CACHE.get(bone_name).get(uuid).shouldRemove()){
                    PATH_CACHE.get(bone_name).remove(uuid);
                }
            }
        }
    }

}
