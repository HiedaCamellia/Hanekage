package org.hiedacamellia.hanekage.client.mixin;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekageManager;
import org.hiedacamellia.hanekage.client.util.ItemDisplayContextUtil;
import org.hiedacamellia.hanekage.client.util.ItemStackUtil;
import org.hiedacamellia.hanekage.client.util.ItemUtil;
import org.hiedacamellia.hanekage.registries.HanekageDataComponent;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.UUID;

@Mixin(GeoItemRenderer.class)
public abstract class GeoItemRendererMixin<T extends Item & GeoAnimatable>  {


    @Inject(method = "preRender(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/Item;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZFIII)V",at = @At("RETURN"))
    private void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour, CallbackInfo ci) {

        String item = ItemUtil.toString(animatable.asItem());

        if(!HanekageManager.hasCache(item)){
            HanekageManager.cacheModel(item, model);
            HanekageManager.cacheTexture(item,(GeoRenderer<T>)(Object)this,animatable);
        }
    }

    @Inject(method = "renderByItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",at = @At("RETURN"))
    private void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, CallbackInfo ci) {


        UUID uuid = stack.getOrDefault(HanekageDataComponent.UUID, UUID.randomUUID());
        stack.set(HanekageDataComponent.UUID, uuid);
        String item = ItemStackUtil.toString(stack);
        Matrix4f matrix4f = poseStack.last().pose();
        GeoModel<T> geoModel = ((GeoItemRenderer<T>) (Object) this).getGeoModel();

        BakedGeoModel model = geoModel.getBakedModel(geoModel.getModelResource(((GeoItemRenderer<T>) (Object) this).getAnimatable(), (GeoItemRenderer<T>)(Object)this));

        if(ItemDisplayContextUtil.isInMainHand(transformType) && HanekageManager.hasCache(item)){
            HanekageManager.pushHanekagePath(item,model,matrix4f,uuid);
        }

    }


}
