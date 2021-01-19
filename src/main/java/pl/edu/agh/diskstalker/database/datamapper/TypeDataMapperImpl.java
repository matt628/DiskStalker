package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDataMapperImpl implements TypeDataMapper {

    private final Map<Root, List<Type>> rootTypes = new HashMap<>();

    @Override
    public void addType(Root root, Type type) {
        List<Type> types = findAllByRoot(root);
        if (types.contains(type)) return;
        types.add(type);
        rootTypes.put(root, types);
    }

    @Override
    public List<Type> findAllByRoot(Root root) {
        return rootTypes.getOrDefault(root, new ArrayList<>());
    }

    @Override
    public void deleteAllByRoot(Root root) {
        rootTypes.put(root, new ArrayList<>());
    }
}
