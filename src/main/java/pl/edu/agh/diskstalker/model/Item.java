package pl.edu.agh.diskstalker.model;

import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
            Optional<Item> itemOptional = Item.findById(id);
            itemOptional.ifPresent(item -> item.getRoot().addChild(item));
            return itemOptional;
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

    // TODO Make this code look better by extracting some code to methods
    public static List<Item> getChildren(String pathname) {
        String path = getPathFromPathname(pathname);
        String name = getNameFromPathname(pathname);
        return Item.findByLocation(name, path)
                .map(value -> value.getRoot().getChildren().stream().filter(i -> i.isChild(pathname)).collect(Collectors.toList()))
                .orElseGet(() -> Root.findByLocation(name, path).map(Root::getChildren).orElse(null));
    }

    private static String getNameFromPathname(String pathname) {
        return pathname.substring(pathname.lastIndexOf('/'), pathname.lastIndexOf('.') - 1);
    }

    private static String getPathFromPathname(String pathname) {
        return pathname.substring(0, pathname.lastIndexOf('/'));
    }

    private boolean isChild(String pathname) {
            return path.equals(pathname);
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
