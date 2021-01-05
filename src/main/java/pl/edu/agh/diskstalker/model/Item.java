package pl.edu.agh.diskstalker.model;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Item {

    private final String name;

    private final String path;

    private final String type;

    private final String size;

    private final Root root;

    public Item(String name, String path, String type, String size, Root root) {
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

    public String getSize() {
        return size;
    }

    public Root getRoot() {
        return root;
    }

    public String getPathname() {
        return path + File.separator + name;
    }

    public List<Item> getChildren() {
        List<Item> rootChildren = root.getItems();
        return rootChildren.stream()
                .filter(item -> isSubItem(item.getPath()))
                .collect(Collectors.toList());
    }

    private boolean isSubItem(String pathname) {
        return pathname.equals(getPathname());
    }

//    private boolean isChild(String pathname) {
//        return
//    }


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
