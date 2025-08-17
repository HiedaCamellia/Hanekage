package org.hiedacamellia.hanekage.client.graphic.util;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InterpolationUtils {

    // 线性插值函数
    public static Vector3f lerp(Vector3f p0, Vector3f p1, float t) {
        // (1 - t) * p0 + t * p1
        return new Vector3f(
                p0.x * (1 - t) + p1.x * t,
                p0.y * (1 - t) + p1.y * t,
                p0.z * (1 - t) + p1.z * t
        );
    }

    // Catmull-Rom 插值函数
    public static Vector3f catmullRom(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, float t) {
        float t2 = t * t;
        float t3 = t2 * t;

        float x = 0.5f * ((2 * p1.x) +
                (-p0.x + p2.x) * t +
                (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * t2 +
                (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * t3);

        float y = 0.5f * ((2 * p1.y) +
                (-p0.y + p2.y) * t +
                (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * t2 +
                (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * t3);

        float z = 0.5f * ((2 * p1.z) +
                (-p0.z + p2.z) * t +
                (2 * p0.z - 5 * p1.z + 4 * p2.z - p3.z) * t2 +
                (-p0.z + 3 * p1.z - 3 * p2.z + p3.z) * t3);

        return new Vector3f(x, y, z);
    }
    // 对一组点进行插值，每两个点之间生成 steps 个插值点
    public static List<Vector3f> lerpPoints(List<Vector3f> points, int steps) {
        List<Vector3f> result = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Vector3f p0 = points.get(i);
            Vector3f p1 = points.get(i + 1);
            for (int j = 0; j <= steps; j++) {
                float t = (float) j / steps;
                result.add(lerp(p0, p1, t));
            }
        }
        return result;
    }

    public static Vector3f[] lerpPoints(Vector3f[] points, int steps) {
        List<Vector3f> result = new ArrayList<>();
        for (int i = 0; i < points.length - 1; i++) {
            Vector3f p0 = points[i];
            Vector3f p1 = points[i + 1];
            for (int j = 0; j <= steps; j++) {
                float t = (float) j / steps;
                result.add(lerp(p0, p1, t));
            }
        }
        return result.toArray(new Vector3f[0]);
    }
    // 对一组点进行 Catmull-Rom 插值
    public static List<Vector3f> catmullRomPoints(List<Vector3f> points, int steps) {
        List<Vector3f> result = new ArrayList<>();

        // 至少需要4个点
        if (points.size() < 4) {
            throw new IllegalArgumentException("Catmull-Rom 插值需要至少 4 个点");
        }

        for (int i = 0; i < points.size() - 3; i++) {
            Vector3f p0 = points.get(i);
            Vector3f p1 = points.get(i + 1);
            Vector3f p2 = points.get(i + 2);
            Vector3f p3 = points.get(i + 3);

            for (int j = 0; j <= steps; j++) {
                float t = (float) j / steps;
                result.add(catmullRom(p0, p1, p2, p3, t));
            }
        }
        return result;
    }

    public static Vector3f[] catmullRomPoints(Vector3f[] points, int steps) {
        if (points.length < 4) {
            return points;
        }

        List<Vector3f> result = new ArrayList<>();

        for (int i = 0; i < points.length - 3; i++) {
            Vector3f p0 = points[i];
            Vector3f p1 = points[i + 1];
            Vector3f p2 = points[i + 2];
            Vector3f p3 = points[i + 3];

            for (int j = 0; j <= steps; j++) {
                float t = (float) j / steps;
                result.add(catmullRom(p0, p1, p2, p3, t));
            }
        }
        // 将剩下的点也加进去

        result.addAll(Arrays.asList(points).subList(points.length - 3, points.length));

        return result.toArray(new Vector3f[0]);
    }
}
