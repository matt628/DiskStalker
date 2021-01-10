package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.executor.QueryExecutor;
import pl.edu.agh.diskstalker.database.model.Root;

import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Singleton
public class RootDataMapperImpl implements RootDataMapper {

    private final List<Root> roots;

    {
        roots = findAll();
    }

    @Override
    public Optional<Root> create(String name, String path, long maxSize) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.NAME + ", " + Columns.PATH + ", " +
                Columns.MAX_SIZE + ") VALUES (?, ?, ?)";

        Object[] args = { name, path, maxSize };
        try {
            if (findByLocation(name, path).isPresent())
                return Optional.empty();

            int id = QueryExecutor.createAndObtainId(sql, args);
            return findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Root> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;

        try{
            ResultSet rs = QueryExecutor.read(sql);
            List<Root> resultList = new LinkedList<>();

            while(rs.next()){
                resultList.add(new Root(rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getLong(Columns.MAX_SIZE))
                );
            }
            return resultList;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Root> findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        return find(value, sql);
    }

    @Override
    public Optional<Root> findByLocation(String name, String path) {
        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + Columns.NAME + " = (?) AND " + Columns.PATH + " = (?)";
        Object[] value = { name, path };
        return find(value, sql);
    }

    @Override
    public Optional<Root> find(Object[] args, String sql) {
        try {
            ResultSet rs = QueryExecutor.read(sql, args);

            if(!rs.isClosed()) {
                return Optional.of(new Root(
                        rs.getInt(Columns.ID),
                        rs.getString(Columns.NAME),
                        rs.getString(Columns.PATH),
                        rs.getLong(Columns.MAX_SIZE)
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM " + TABLE_NAME +
                " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        try {
            QueryExecutor.delete(sql, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
