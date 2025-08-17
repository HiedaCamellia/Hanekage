package org.hiedacamellia.hanekage.client.graphic.hanekage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.hiedacamellia.hanekage.client.config.json.SwordTrailConfig;
import org.hiedacamellia.hanekage.client.graphic.render.HanekageRenderer;
import org.hiedacamellia.hanekage.client.graphic.render.TextureHanekageRenderer;
import org.hiedacamellia.hanekage.client.util.EntityUtil;
import org.hiedacamellia.hanekage.client.util.ItemUtil;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
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
    private static final HashMap<String, Map<UUID, HanekagePath>> TEXTURE_PATH_CACHE = new HashMap<>();

    public static void pushHanekagePath(String name, BakedGeoModel model, Matrix4f matrix4f, UUID uuid) {
        getCache(name).tracks().forEach(modelPath -> {
            GeoBone parentBone = model.getBone(modelPath.first()).get();
            Matrix4f worldMatrix = new Matrix4f(matrix4f).mul(parentBone.getModelSpaceMatrix());


            Vector4f parent_transform = parentBone.getLocalSpaceMatrix().transform(new Vector4f(parentBone.getPivotX()/16, parentBone.getPivotY()/16, parentBone.getPivotZ()/16, 1));
            Vector4f parent = new Vector4f(parent_transform.x(), parent_transform.y(), parent_transform.z(), 1).mul(worldMatrix);

            for (String string : modelPath.subPath(1).path()) {
                GeoBone geoBone = model.searchForChildBone(parentBone, string);
                if(geoBone == null) {
                    LOGGER.error("Bone {} not found in model {}, cannot render path.", string, modelPath);
                    return;
                }


                parent = new Vector4f(parent).add(getOffset(parentBone,geoBone,matrix4f).mul(-1));
                parentBone = geoBone;
            }

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

            Vector4f start = new Vector4f(parent).add(getOffset(parentBone,trackStartBone,matrix4f));
            Vector4f end = new Vector4f(parent).add(getOffset(parentBone,trackEndBone,matrix4f).mul(-1));

            if(SwordTrailConfig.hasTrailTexture(parentBone.getName())){
                pushTexturePoint(parentBone.getName(), uuid, start, end);
            }else {
                pushPoint(parentBone.getName(),uuid, start, end);
            }

        });


    }

    private static Vector4f getOffset(GeoBone parent,GeoBone child,Matrix4f matrix4f){
        Matrix4f worldMatrix = new Matrix4f(matrix4f).mul(parent.getModelSpaceMatrix());
        Quaternionf rotation = new Quaternionf().identity()
                .rotateZ(parent.getRotZ())
                .rotateY(parent.getRotY())
                .rotateX(parent.getRotX());

        Vector3f translate = new Vector3f((parent.getPivotX()-child.getPivotX())/16, (parent.getPivotY()-child.getPivotY())/16, (parent.getPivotZ()-child.getPivotZ())/16);

        // 将平移向量旋转
        rotation.transform(translate);

        Vector4f vector4f = new Vector4f(translate, 0.0f);
//        vector4f = vector4f.rotate(rotation);
        vector4f.mul(parent.getScaleX(), parent.getScaleY(), parent.getScaleZ(), 1.0f); // 应用缩放
        vector4f.mul(worldMatrix); // 应用世界矩阵
        return vector4f;

    }

    private static void pushTexturePoint(String bone_name, UUID uuid, Vector4f start, Vector4f end) {
        if (!TEXTURE_PATH_CACHE.containsKey(bone_name)) {
            TEXTURE_PATH_CACHE.put(bone_name, new HashMap<>());
        }
        if (!TEXTURE_PATH_CACHE.get(bone_name).containsKey(uuid)) {
            TEXTURE_PATH_CACHE.get(bone_name).put(uuid, new HanekagePath(SwordTrailConfig.getTrailTime(bone_name),SwordTrailConfig.getTrailColor(bone_name)));
        }

        TEXTURE_PATH_CACHE.get(bone_name).get(uuid).pushPoint(new Vector3f(start.x(), start.y(), start.z())
                , new Vector3f(end.x(), end.y(), end.z()));
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
        TEXTURE_PATH_CACHE.forEach((string, map) -> {
            ResourceLocation texture = SwordTrailConfig.getTrailTexture(string);
            map.forEach((uuid, hanekagePath) -> TextureHanekageRenderer.render(poseStack, hanekagePath,texture));
        });
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
