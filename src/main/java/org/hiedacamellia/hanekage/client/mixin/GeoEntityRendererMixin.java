package org.hiedacamellia.hanekage.client.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekageManager;
import org.hiedacamellia.hanekage.client.util.EntityUtil;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.UUID;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable>  {


    @Inject(method = "preRender(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/Entity;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZFIII)V",at = @At("RETURN"))
    private void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {

        String item = EntityUtil.toString(animatable);

        if(!HanekageManager.hasCache(item)){
            HanekageManager.cacheModel(item, model);
            HanekageManager.cacheTexture(item,(GeoRenderer<T>) this,animatable);
        }
    }

    @Inject(method = "renderFinal(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/Entity;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;FIII)V",at = @At("RETURN"))
    private void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {

        UUID uuid = animatable.getUUID();

        String entity = EntityUtil.toString(animatable);
        Matrix4f matrix4f = poseStack.last().pose();

        if(HanekageManager.hasCache(entity)){
            HanekageManager.pushHanekagePath(entity,model,matrix4f,uuid);
        }

    }


}
