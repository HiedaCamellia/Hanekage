package org.hiedacamellia.hanekage.client.graphic.hanekage;

public record ModelPath(String[] path) {

    public static ModelPath single(String path) {
        return new ModelPath(new String[]{path});
    }

    public ModelPath prefix(String prefix) {
        String[] newPath = new String[path.length + 1];
        newPath[0] = prefix;
        System.arraycopy(path, 0, newPath, 1, path.length);
        return new ModelPath(newPath);
    }

    public ModelPath(String[] path) {
        this.path = path;
    }

    public String first(){
        return path.length > 0 ? path[0] : "";
    }

    public String last() {
        return path.length > 0 ? path[path.length - 1] : "";
    }

    public ModelPath subPath(int start){
        if (start < 0 || start >= path.length) {
            throw new IndexOutOfBoundsException("Invalid start index: " + start);
        }
        String[] subPath = new String[path.length - start];
        System.arraycopy(path, start, subPath, 0, path.length - start);
        return new ModelPath(subPath);
    }

    public ModelPath subPath(int start, int end) {
        if (start < 0 || end > path.length || start >= end) {
            throw new IndexOutOfBoundsException("Invalid subPath range: " + start + " to " + end);
        }
        String[] subPath = new String[end - start];
        System.arraycopy(path, start, subPath, 0, end - start);
        return new ModelPath(subPath);
    }

    public String getPath() {
        return String.join(".", path);
    }

    @Override
    public String toString() {
        return getPath();
    }
}
