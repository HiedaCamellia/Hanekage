package org.hiedacamellia.hanekage.client.graphic.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.hiedacamellia.hanekage.client.graphic.hanekage.HanekagePath;
import org.hiedacamellia.hanekage.client.graphic.util.InterpolationUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class HanekageRenderer {

    public static void render(PoseStack poseStack, HanekagePath path) {
        startBatch();
        renderInBatch(poseStack, path);
        endBatch();
    }

    private static BufferBuilder builder;
    private static int batchSize = 0;

    public static void startBatch() {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    }

    public static void endBatch() {
        if (builder != null && batchSize > 0) {
            BufferUploader.drawWithShader(builder.buildOrThrow());
        }
        builder = null;
        batchSize = 0;
    }

    public static void renderInBatch(PoseStack poseStack, HanekagePath path) {

        if (builder == null) {
            startBatch();
        }


        Vector3f[] starts = path.getPositionsStart();
        Vector3f[] ends = path.getPositionsEnd();

        Vector3f[] startsInterpolated = switch (path.getInterpolationType()){
            case "lerp" -> InterpolationUtils.lerpPoints(starts, path.getInterpolationSteps());
            case "catmullrom" -> InterpolationUtils.catmullRomPoints(starts, path.getInterpolationSteps());
            default -> starts;
        };
        Vector3f[] endsInterpolated = switch (path.getInterpolationType()){
            case "lerp" -> InterpolationUtils.lerpPoints(ends, path.getInterpolationSteps());
            case "catmullrom" -> InterpolationUtils.catmullRomPoints(ends, path.getInterpolationSteps());
            default -> ends;
        };

        if (path.getLength() < 2) return;

        Matrix4f matrix = poseStack.last().pose();
        matrix.mul(RenderSystem.getProjectionMatrix().invert());
        int color = path.getColor();
        float r = FastColor.ARGB32.red(color) / 255.0f;
        float g = FastColor.ARGB32.green(color) / 255.0f;
        float b = FastColor.ARGB32.blue(color) / 255.0f;

        for (int i = 0; i < startsInterpolated.length - 1; i++) {
            if (Mth.abs(startsInterpolated[i].distance(endsInterpolated[i])) < 1e-6f) {
                continue;
            }

            Vector3f s0 = startsInterpolated[i];
            Vector3f s1 = startsInterpolated[i + 1];
            Vector3f e0 = endsInterpolated[i];
            Vector3f e1 = endsInterpolated[i + 1];

            float alpha = ((float) i / startsInterpolated.length);
            float alpha2 = ((float) (i + 1) / startsInterpolated.length);

            builder.addVertex(matrix, s0.x, s0.y, s0.z).setColor(r, g, b, alpha);
            builder.addVertex(matrix, s1.x, s1.y, s1.z).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, e1.x, e1.y, e1.z).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, e0.x, e0.y, e0.z).setColor(r, g, b, alpha);

            builder.addVertex(matrix, s0.x, s0.y, s0.z).setColor(r, g, b, alpha);
            builder.addVertex(matrix, e0.x, e0.y, e0.z).setColor(r, g, b, alpha);
            builder.addVertex(matrix, e1.x, e1.y, e1.z).setColor(r, g, b, alpha2);
            builder.addVertex(matrix, s1.x, s1.y, s1.z).setColor(r, g, b, alpha2);


            batchSize += 6 * 2; // Each segment has 6 vertices (2 triangles)
        }

    }
}
