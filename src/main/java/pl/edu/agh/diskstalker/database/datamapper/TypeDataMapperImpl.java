package pl.edu.agh.diskstalker.database.datamapper;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import pl.edu.agh.diskstalker.database.connection.QueryExecutor;
import pl.edu.agh.diskstalker.database.model.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Singleton
public class TypeDataMapperImpl implements TypeDataMapper {

    private final QueryExecutor queryExecutor;

    @Inject
    public TypeDataMapperImpl(QueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
        init();
    }

    private void init() {
        create(".jpg", "Image");
        create(".png", "Image");
        create(".pdf", "File");
        create(".txt", "File");
        create(".mp3", "Music");
        create(".mp4", "Video");
        create(".exe", "Application");
        create(".zip", "Archive");
        create("other", "Other");
        create("folder", "Folder");
    }

    @Override
    public Optional<Type> create(String extension, String description) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + Columns.EXTENSION + ", " + Columns.DESC + ") VALUES (?, ?)";
        Object[] args = { extension, description };

        try {
            if (findByExtension(extension).isPresent())
                return Optional.empty();

            int id = queryExecutor.createAndObtainId(sql, args);
            return findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Type> findAll() {
        String sql = "SELECT * FROM " + TABLE_NAME;

        try {
            ResultSet rs = queryExecutor.read(sql);
            List<Type> resultList = new LinkedList<>();

            while (rs.next()) {
                resultList.add(new Type(rs.getInt(Columns.ID),
                        rs.getString(Columns.EXTENSION),
                        rs.getString(Columns.DESC))
                );
            }
            return resultList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Type> findById(int id) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.ID + " = (?)";
        Object[] value = { id };
        return find(value, sql);
    }

    @Override
    public Optional<Type> findByExtension(String extension) {
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + Columns.EXTENSION + " = (?)";
        Object[] value = { extension };
        return find(value, sql);
    }

    @Override
    public Optional<Type> find(Object[] args, String sql) {
        try {
            ResultSet rs = queryExecutor.read(sql, args);

            if (!rs.isClosed()) {
                return Optional.of(new Type(
                        rs.getInt(Columns.ID),
                        rs.getString(Columns.EXTENSION),
                        rs.getString(Columns.DESC)
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
            queryExecutor.delete(sql, value);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Type getType(String extension) {
        return findByExtension(extension)
                .orElse(findByExtension("other")
                        .orElseThrow(NoSuchElementException::new));
    }
}
