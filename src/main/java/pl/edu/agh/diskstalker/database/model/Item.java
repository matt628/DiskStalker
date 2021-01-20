package pl.edu.agh.diskstalker.database.model;

import java.io.File;
import java.util.Objects;

public class Item {
    private final String name;
    private final String path;
    private final Type type;
    private final long size;
    private final Root root;

    public Item(String name, String path, Type type, long size, Root root) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
        this.root = root;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Type getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public Root getRoot() {
        return root;
    }

    public String getPathname() {
        String pathname = path + File.separator + name;
        return type.getExtension().equals("folder") ? pathname : pathname + type;
    }

    public boolean isFile() {
        return !type.getExtension().equals("folder");
    }

    public boolean isClosestParent(Item item) {
        return getPathname().equals(item.getPath());
    }

    public boolean isChild(String rootPathname) {
        return getPath().contains(rootPathname);
    }

    @Override
    public String toString() {
        return name + getConvertedSizeString();
    }

    private String getConvertedSizeString() {
        float s = size;
        if (size / 1000000 > 1) {
            return " " + s / 1000000 + " MB";
        }
        return " " + s / 1000 + " KB";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return size == item.size && name.equals(item.name) && path.equals(item.path) && Objects.equals(type, item.type) && root.equals(item.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, type, size, root);
    }
}
