package org.hiedacamellia.hanekage.client.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class ItemUtil {

    public static Item fromString(String string){
        return fromResourceLocation(ResourceLocation.tryParse(string));
    }
    public static Item fromResourceLocation(ResourceLocation resourceLocation){
        return BuiltInRegistries.ITEM.get(resourceLocation);
    }
    public static String toString(Item item){
        return toResourceLocation(item).toString();
    }
    public static ResourceLocation toResourceLocation(Item item){
        return BuiltInRegistries.ITEM.getKey(item);
    }
}
