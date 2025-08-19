package org.hiedacamellia.hanekage.client.api;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekageManager;
import org.hiedacamellia.hanekage.registries.HanekageDataComponent;
import org.joml.Vector4f;

import java.util.UUID;

public class HanekageAPI {

    public static void pushPoint(String bone_name, Entity entity, Vector4f start, Vector4f end){
        HanekageManager.pushPoint(bone_name, entity.getUUID(), start, end);
    }
    public static void pushPoint(String bone_name, ItemStack stack, Vector4f start, Vector4f end){
        UUID uuid = stack.getOrDefault(HanekageDataComponent.UUID, UUID.randomUUID());
        stack.set(HanekageDataComponent.UUID, uuid);
        HanekageManager.pushPoint(bone_name, uuid, start, end);
    }
    public static void pushPoint(String bone_name, UUID uuid, Vector4f start, Vector4f end){
        HanekageManager.pushPoint(bone_name, uuid, start, end);
    }

}
