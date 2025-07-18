package org.hiedacamellia.hanekage.client.util;

import net.minecraft.world.item.ItemDisplayContext;

public class ItemDisplayContextUtil {


    public static boolean isInHand(ItemDisplayContext context){
        return context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
    }

    public static boolean isInMainHand(ItemDisplayContext context){
        return context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
    }
}
