package pl.edu.agh.diskstalker.model;

import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Item {

    public static final String TABLE_NAME = "Items";

    private final int id;

    private final String name;

    private final String path;

    private final String type;

    private final String size;

    private final Root root;

    public Item(int id, String name, String path, String type, String size, Root root) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
        this.root = root;
    }

    public static Optional<Item> create(final String name, final String path, final String type, final String size, final Root root) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.NAME + ", " + Columns.PATH + ", " +
                Columns.TYPE + ", " + Columns.SIZE + ", " + Columns.ROOT + ") VALUES (?, ?, ?, ?, ?)";

        Object[] args = { name, path, type, size, root.getId() };

        try {
            if (findByLocation(name, path).isPresent())
                return Optional.empty();

            int id = QueryExecutor.createAndObtainId(sql, args);
            return Item.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Item> findById(final int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        return find(value, sql);
    }

    public static List<Item> findByPath(final String path) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.PATH + " = (?)";
        Object[] value = { path };

        try{
            ResultSet rs = QueryExecutor.read(sql, value);
            List<Item> resultList = new LinkedList<>();

            while(rs.next()){
                resultList.add(new Item(rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getString(Columns.TYPE),
                        rs.getString(Columns.SIZE),
                        Root.findById(rs.getInt(Columns.ROOT)).orElseThrow(SQLException::new)
                ));
            }
            return resultList;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static Optional<Item> findByLocation(final String name, final String path) {
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + Columns.NAME + " = (?) AND " + Columns.PATH + " = (?)";
        Object[] value = { name, path };
        return find(value, sql);
    }

    public static Optional<Item> find(Object[] args, String sql) {
        try {
            ResultSet rs = QueryExecutor.read(sql, args);
            return Optional.of(new Item(
                    rs.getInt(Columns.ID),
                    rs.getString(Columns.NAME),
                    rs.getString(Columns.PATH),
                    rs.getString(Columns.TYPE),
                    rs.getString(Columns.SIZE),
                    Root.findById(rs.getInt(Columns.ROOT)).orElseThrow(SQLException::new)
            ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static List<Item> getChildren(String pathname) {
        return findByPath(pathname + File.separator);
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
        return path + '/' + name;
    }

    public static class Columns {

        public static final String ID = "ItemID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String TYPE = "Type";

        public static final String SIZE = "Size";

        public static final String ROOT = "RootID";
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", size='" + size + '\'' +
                ", rootID=" + root.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id == item.id &&
                name.equals(item.name) &&
                path.equals(item.path) &&
                Objects.equals(type, item.type) &&
                size.equals(item.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, type, size);
    }
}
