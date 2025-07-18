package org.hiedacamellia.hanekage.client.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemStackUtil {

    public static ItemStack fromString(String string){
        return fromResourceLocation(ResourceLocation.tryParse(string));
    }
    public static ItemStack fromResourceLocation(ResourceLocation resourceLocation){
        return BuiltInRegistries.ITEM.get(resourceLocation).getDefaultInstance();
    }
    public static String toString(ItemStack itemStack){
        return toResourceLocation(itemStack).toString();
    }
    public static ResourceLocation toResourceLocation(ItemStack itemStack){
        return BuiltInRegistries.ITEM.getKey(itemStack.getItem());
    }
}
