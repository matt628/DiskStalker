package pl.edu.agh.diskstalker.database.model;

import java.io.File;
import java.util.Objects;

public class Root {

    private final int id;

    private final String name;

    private final String path;

    private final long maxSize;

    public Root(int id, String name, String path, long maxSize) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.maxSize = maxSize;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public long getMaxSize() {
        return maxSize;
    }

    public String getPathname() {
        return path + File.separator + name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root root = (Root) o;
        return id == root.id &&
                name.equals(root.name) &&
                path.equals(root.path) &&
                maxSize == root.maxSize;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, maxSize);
    }
}
