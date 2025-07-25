package org.hiedacamellia.hanekage.client.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.hiedacamellia.hanekage.registries.HanekageDataComponent;

@EventBusSubscriber
public class ItemUseHandler {

    @SubscribeEvent
    public static void onItemUseStart(LivingEntityUseItemEvent.Start event) {
        ItemStack item = event.getItem();
        LivingEntity livingEntity = event.getEntity();
        if(livingEntity.level().isClientSide()){
            item.set(HanekageDataComponent.UUID, livingEntity.getUUID());
        }
    }
    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        ItemStack item = event.getItem();
        LivingEntity livingEntity = event.getEntity();
        if(livingEntity.level().isClientSide()){
//            item.remove(HanekageDataComponent.UUID);
        }
    }
}
