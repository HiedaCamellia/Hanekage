package org.hiedacamellia.hanekage.test;

import net.minecraft.resources.ResourceLocation;
import org.hiedacamellia.hanekage.Hanekage;
import software.bernie.geckolib.model.GeoModel;

public class TestModel extends GeoModel<TestItem> {
    private final ResourceLocation model = Hanekage.rl("geo/test_item.geo.json");
    private final ResourceLocation texture = Hanekage.rl("textures/item/test_item.png");
    private final ResourceLocation animations = Hanekage.rl("animations/test_item.animation.json");


    @Override
    public ResourceLocation getModelResource(TestItem animatable) {
        return model;
    }

    @Override
    public ResourceLocation getTextureResource(TestItem animatable) {
        return texture;
    }

    @Override
    public ResourceLocation getAnimationResource(TestItem animatable) {
        return animations;
    }
}
