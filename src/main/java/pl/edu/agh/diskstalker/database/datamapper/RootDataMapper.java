package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.connection.QueryExecutor;
import pl.edu.agh.diskstalker.database.model.Root;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Singleton
public class RootDataMapper {

    @Inject
    private QueryExecutor queryExecutor;

    public Optional<Root> create(String name, String path, long maxSize, long maxTreeSize) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.NAME + ", " + Columns.PATH + ", " +
                Columns.MAX_SIZE + ", " + Columns.MAX_TREE_SIZE + ") VALUES (?, ?, ?, ?)";

        Object[] args = {name, path, maxSize, maxTreeSize};
        try {
            if (findByLocation(name, path).isPresent())
                return Optional.empty();

            int id = queryExecutor.createAndObtainId(sql, args);
            return findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Root> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;

        try {
            ResultSet rs = queryExecutor.read(sql);
            List<Root> resultList = new LinkedList<>();

            while (rs.next()) {
                resultList.add(new Root(rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getLong(Columns.MAX_SIZE),
                        rs.getLong(Columns.MAX_TREE_SIZE))
                );
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public Optional<Root> findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        Object[] value = {id};
        return find(value, sql);
    }

    public Optional<Root> findByLocation(String name, String path) {
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + Columns.NAME + " = (?) AND " + Columns.PATH + " = (?)";
        Object[] value = {name, path};
        return find(value, sql);
    }

    public Optional<Root> find(Object[] args, String sql) {
        try {
            ResultSet rs = queryExecutor.read(sql, args);

            if (!rs.isClosed()) {
                return Optional.of(new Root(
                        rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getLong(Columns.MAX_SIZE),
                        rs.getLong(Columns.MAX_TREE_SIZE)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE " + Columns.ID + " = (?)";
        Object[] value = {id};
        try {
            queryExecutor.delete(sql, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String TABLE_NAME = "Roots";

    private static class Columns {

        public static final String ID = "RootID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String MAX_SIZE = "MaxSize";

        public static final String MAX_TREE_SIZE = "MaxTreeSize";
    }
}
