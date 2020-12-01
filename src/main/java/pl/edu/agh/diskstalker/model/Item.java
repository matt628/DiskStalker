package pl.edu.agh.diskstalker.model;

import pl.edu.agh.diskstalker.executor.QueryExecutor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class Item {

    public static final String TABLE_NAME = "Items";

    private final int id;

    private final String name;

    private final String path;

    private final String type;

    private final String size;

    private Integer parentId;

    public Item(int id, String name, String path, String type, String size) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.type = type;
        this.size = size;
    }

    public static Optional<Item> create(final String path, final String type, final String size, final Item parent) {
//        String sql = "INSERT INTO " + TABLE_NAME + " ("
//                + Columns.PATH + ", " + Columns.TYPE + ", " + Columns.SIZE + ", " + Columns.PARENT +
//                ") VALUES (?, ?, ?)";

        String sql = "INSERT INTO Items (Path, Type, Size) VALUES (?, ?, ?);";

        Integer parentId = null;
        if (parent != null) parentId = parent.getId();

        Object[] args = { path, type, size };

        try {
            int id = QueryExecutor.createAndObtainId(sql, args);
            return Item.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<Item> findById(final int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        return find(id, sql);
    }

    public static Optional<Item> find(Object value, String sql) {
        Object[] args = { value };
        try {
            ResultSet rs = QueryExecutor.read(sql, args);
//            return Optional.of(new Item(
//                    rs.getInt(Columns.ID),
//                    rs.getString(Columns.PATH),
//                    rs.getString(Columns.TYPE),
//                    rs.getString(Columns.SIZE)
////                    rs.getInt(Columns.PARENT))
//            );
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

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public int getParentId() {
        return parentId;
    }

    public static class Columns {

        public static final String ID = "ItemID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String TYPE = "Type";

        public static final String SIZE = "Size";

        public static final String PARENT = "ParentID";

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
                size.equals(item.size) &&
                Objects.equals(parentId, item.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, path, type, size, parentId);
    }
}
