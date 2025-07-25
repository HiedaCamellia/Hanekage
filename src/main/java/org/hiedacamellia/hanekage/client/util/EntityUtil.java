package org.hiedacamellia.hanekage.client.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class EntityUtil {

    public static EntityType<?> fromString(String string){
        return fromResourceLocation(ResourceLocation.tryParse(string));
    }
    public static EntityType<?> fromResourceLocation(ResourceLocation resourceLocation){
        return BuiltInRegistries.ENTITY_TYPE.get(resourceLocation);
    }
    public static String toString(Entity entity){
        return toResourceLocation(entity).toString();
    }
    public static ResourceLocation toResourceLocation(Entity entity){
        return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
    }
}
