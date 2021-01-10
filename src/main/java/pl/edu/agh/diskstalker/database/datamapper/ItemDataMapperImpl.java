package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class ItemDataMapperImpl implements ItemDataMapper {

    private final Map<Root, List<Item>> rootItems = new HashMap<>();

    @Override
    public void addItem(Root root, Item item) {
        List<Item> items = findAllByRoot(root);
        items.add(item);
        rootItems.put(root, items);
    }

    @Override
    public List<Item> findAllByRoot(Root root) {
        return rootItems.getOrDefault(root, new ArrayList<>());
    }

    @Override
    public void deleteAllByRoot(Root root) {
        rootItems.put(root, new ArrayList<>());
    }

    @Override
    public List<Item> getChildren(Item rootItem) {
        List<Item> rootChildren = findAllByRoot(rootItem.getRoot());
        return rootChildren.stream()
                .filter(item -> rootItem.isSubItem(item.getPath()))
                .collect(Collectors.toList());
    }

    @Override
    public Item getRootItem(Root root) {
        List<Item> items = findAllByRoot(root);
        System.out.println(items);
        return items.stream()
                .filter(item -> item.getPathname().equals(root.getPathname()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
