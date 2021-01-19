package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Type;

import java.util.List;
import java.util.Optional;

public interface TypeDataMapper {

    Optional<Type> create(String extension, String description);

    List<Type> findAll();

    Optional<Type> findById(int id);

    Optional<Type> findByExtension(String extension);

    Optional<Type> find(Object[] args, String sql);

    void deleteById(int id);

    Type getType(String typeString);

    String TABLE_NAME = "Types";

    class Columns {

        public static final String ID = "TypeID";

        public static final String EXTENSION = "Extension";

        public static final String DESC = "Description";
    }
}
