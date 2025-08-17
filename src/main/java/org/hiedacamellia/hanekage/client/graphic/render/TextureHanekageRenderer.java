package org.hiedacamellia.hanekage.client.graphic.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekagePath;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TextureHanekageRenderer {

    public static void render(PoseStack poseStack, HanekagePath path, ResourceLocation trailTexture) {

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, trailTexture);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);


        Vector3f[] starts = path.getPositionsStart();
        Vector3f[] ends = path.getPositionsEnd();

        if (path.getLength() < 2) return;

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix = pose.pose();
        int color = path.getColor();
        float r = FastColor.ARGB32.red(color) / 255.0f;
        float g = FastColor.ARGB32.green(color) / 255.0f;
        float b = FastColor.ARGB32.blue(color) / 255.0f;

        for (int i = 0; i < starts.length - 1; i++) {
            if (Mth.abs(starts[i].distance(ends[i]))<1e-6f) {
                continue;
            }

            Vector3f s0 = starts[i];
            Vector3f s1 = starts[i + 1];
            Vector3f e0 = ends[i];
            Vector3f e1 = ends[i + 1];

            float alpha = ((float) i / starts.length);
            float alpha2 = ((float) (i + 1) / starts.length);

            builder.addVertex(matrix, s0.x, s0.y, s0.z).setUv(alpha,0).setColor(r, g, b, alpha);
            builder.addVertex(matrix, s1.x, s1.y, s1.z).setUv(alpha2,1).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, e1.x, e1.y, e1.z).setUv(alpha2,1).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, e0.x, e0.y, e0.z).setUv(alpha,0).setColor(r, g, b, alpha);

            builder.addVertex(matrix, s0.x, s0.y, s0.z).setUv(alpha,0).setColor(r, g, b, alpha);
            builder.addVertex(matrix, e0.x, e0.y, e0.z).setUv(alpha,0).setColor(r, g, b, alpha);
            builder.addVertex(matrix, e1.x, e1.y, e1.z).setUv(alpha2,1).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, s1.x, s1.y, s1.z).setUv(alpha2,1).setColor(r, g, b, alpha2);
        }

        BufferUploader.drawWithShader(builder.buildOrThrow());
    }
}
