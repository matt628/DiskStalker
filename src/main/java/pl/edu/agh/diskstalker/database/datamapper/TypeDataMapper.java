package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.util.List;

public interface TypeDataMapper {

    void addType(Root root, Type type);

    List<Type> findAllByRoot(Root root);

    void deleteAllByRoot(Root root);
}
