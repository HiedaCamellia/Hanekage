package org.hiedacamellia.hanekage.client.graphic.event;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekageManager;

@EventBusSubscriber
public class LevelRenderHandler {
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event){
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_ENTITIES){
            HanekageManager.renderHanekage(event.getPoseStack());
        }
    }
}
