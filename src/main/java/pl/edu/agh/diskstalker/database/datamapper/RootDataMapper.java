package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Root;

import java.util.List;
import java.util.Optional;

public interface RootDataMapper {

    Optional<Root> create(String name, String path, long maxSize);

    List<Root> findAll();

    Optional<Root> findById(int id);

    Optional<Root> findByLocation(final String name, final String path);

    Optional<Root> find(Object[] args, String sql);

    void deleteById(int id);

    String TABLE_NAME = "Roots";

    class Columns {

        public static final String ID = "RootID";

        public static final String NAME = "Name";

        public static final String PATH = "Path";

        public static final String MAX_SIZE = "MaxSize";
    }
}
