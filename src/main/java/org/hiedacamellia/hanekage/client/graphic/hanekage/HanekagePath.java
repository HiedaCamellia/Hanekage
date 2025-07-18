package org.hiedacamellia.hanekage.client.graphic.hanekage;

import org.joml.Vector3f;

public class HanekagePath {
    private int length;
    private final Vector3f[] positions_start;
    private final Vector3f[] positions_end;
    private final int maxLength;
    private final int color;
    private boolean shouldpop = false;

    public HanekagePath(int maxLength, int color) {
        this.maxLength = maxLength;
        this.color = color;
        this.length = 0;
        this.positions_start = new Vector3f[maxLength];
        this.positions_end = new Vector3f[maxLength];
    }

    public boolean shouldRemove() {
        return length == 0;
    }

    public void pushPoint(Vector3f position_start,Vector3f position_end) {
        if (length >= maxLength) {
            System.arraycopy(positions_start, 1, positions_start, 0, maxLength - 1);
            System.arraycopy(positions_end, 1, positions_end, 0, maxLength - 1);
            length--;
        }
        positions_start[length] = position_start;
        positions_end[length] = position_end;
        length++;
        shouldpop = false;
    }

    public void popPoint() {
        if (shouldpop && length > 0) {
            length--;
            System.arraycopy(positions_start, 1, positions_start, 0, length);
            System.arraycopy(positions_end, 1, positions_end, 0, length);
            positions_start[length] = null;
            positions_end[length] = null;
        }
        shouldpop = true;
    }

    public Vector3f[] getPositionsStart() {
        Vector3f[] result = new Vector3f[length];
        System.arraycopy(positions_start, 0, result, 0, length);
        return result;
    }

    public Vector3f[] getPositionsEnd() {
        Vector3f[] result = new Vector3f[length];
        System.arraycopy(positions_end, 0, result, 0, length);
        return result;
    }

    public int getLength() {
        return length;
    }

    public int getColor(){
        return color;
    }

}
