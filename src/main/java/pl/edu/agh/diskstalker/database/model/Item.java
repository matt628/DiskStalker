package pl.edu.agh.diskstalker.database.model;

import java.io.File;
import java.text.DecimalFormat;
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
        return path + File.separator + name;
    }

    public boolean isFile() {
        return type != Type.FOLDER;
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
        double result = (double) size;
        if(size/(1024*1024*1024)  > 1){
            result /= (1024*1024*1024);
            return " " + new DecimalFormat("##.##").format(result) + "GB";
        }else if(size/(1024*1024) > 1){
            result /= (1024*1024);
            return " " + new DecimalFormat("##.##").format(result) + "MB";
        }else if(size/1024 > 1){
            result /= 1024;
            return " " + new DecimalFormat("##.##").format(result) + "KB";
        }
        return " " + size + "B";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return size == item.size &&
                name.equals(item.name) &&
                path.equals(item.path) &&
                Objects.equals(type, item.type) &&
                root.equals(item.root);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, type, size, root);
    }
}
