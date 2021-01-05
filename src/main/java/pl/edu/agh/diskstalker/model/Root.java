package pl.edu.agh.diskstalker.model;

import pl.edu.agh.diskstalker.database.executor.QueryExecutor;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Root {
    public static final String TABLE_NAME = "Roots";

    private final int id;

    private final String name;

    private final String path;

    private final String maxSize;

    private final List<Item> items = new ArrayList<>();

    public Root(int id, String name, String path, String maxSize) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.maxSize = maxSize;
    }

    public static Optional<Root> create(final String name, final String path, final String maxSize) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.NAME + ", " + Columns.PATH + ", " +
                 Columns.MAX_SIZE + ") VALUES (?, ?, ?)";

        Object[] args = { name, path, maxSize };
        try {
            if (findByLocation(name, path).isPresent())
                return Optional.empty();

            int id = QueryExecutor.createAndObtainId(sql, args);
            return Root.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static List<Root> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;

        try{
            ResultSet rs = QueryExecutor.read(sql);
            List<Root> resultList = new LinkedList<>();

            while(rs.next()){
                resultList.add(new Root(rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getString(Columns.MAX_SIZE))
                );
            }
            return resultList;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
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
                    rs.getString(Columns.MAX_SIZE)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void deleteById(int id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        try {
            QueryExecutor.delete(sql, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public String getMaxSize() {
        return maxSize;
    }

    public String getPathname() {
        return path + File.separator + name;
    }

    public List<Item> getItems() {
        return items;
    }

    // TODO: Count size of the folder
    public String getSize() {
        return "0";
    }

    public static class Columns {

        public static final String ID = "RootID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String MAX_SIZE = "MaxSize";
    }

    @Override
    public String toString() {
        return "Root{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
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
                maxSize.equals(root.maxSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, maxSize);
    }
}
