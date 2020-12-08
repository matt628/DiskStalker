package pl.edu.agh.diskstalker.model;

import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class Root {
    public static final String TABLE_NAME = "Roots";

    private final int id;

    private final String name;

    private final String path;

    private final String size;

    private final String maxSize;

    public Root(int id, String name, String path, String size, String maxSize) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
        this.maxSize = maxSize;
    }

    public static Optional<Root> create(final String name, final String path, final String size, final String maxSize) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.NAME + ", " + Columns.PATH + ", " +
                 Columns.SIZE + ", " + Columns.MAX_SIZE + ") VALUES (?, ?, ?, ?)";

        Object[] args = { name, path, size, maxSize };

        try {
            if (findByLocation(name, path).isPresent())
                return Optional.empty();

            int id = QueryExecutor.createAndObtainId(sql, args);
            return Root.findByLocation(name, path);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Root> findById(final int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        return find(value, sql);
    }

    public static Optional<Root> findByLocation(final String name, final String path) {
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + Columns.NAME + " = (?) AND " + Columns.PATH + " = (?)";
        Object[] value = { name, path };
        return find(value, sql);
    }

    public static Optional<Root> find(Object[] args, String sql) {
        try {
            ResultSet rs = QueryExecutor.read(sql, args);
            return Optional.of(new Root(
                    rs.getInt(Columns.ID),
                    rs.getString(Columns.NAME),
                    rs.getString(Columns.PATH),
                    rs.getString(Columns.SIZE),
                    rs.getString(Columns.MAX_SIZE)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
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

    public String getSize() {
        return size;
    }

    public String getMaxSize() {
        return maxSize;
    }

    public static class Columns {

        public static final String ID = "RootID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String SIZE = "Size";

        public static final String MAX_SIZE = "MaxSize";
    }

    @Override
    public String toString() {
        return "Root{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                ", maxSize='" + maxSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Root root = (Root) o;
        return id == root.id &&
                name.equals(root.name) &&
                path.equals(root.path) &&
                size.equals(root.size) &&
                maxSize.equals(root.maxSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, size, maxSize);
    }
}
