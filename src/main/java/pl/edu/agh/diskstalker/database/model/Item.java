package pl.edu.agh.diskstalker.database.model;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Item {

    private final String name;

    private final String path;

    private final String type;

    private final long size;

    private final Root root;

    public Item(String name, String path, String type, long size, Root root) {
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

    public String getType() {
        return type;
    }

    public long getSize() {
        return size;
    }

    public Root getRoot() {
        return root;
    }

    public String getPathname() {
        return path + File.separator + name;
    }

    public boolean isFile() {
        return type != null;
    }

    public boolean isSubItem(String path) {
        return path.equals(getPathname());
    }

    public boolean isChild(String pathname) {
        return getPath().contains(pathname);
    }


    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", root=" + root +
                '}';
    }
}
