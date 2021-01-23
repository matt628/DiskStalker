package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ItemDataMapper {

    private final Map<Root, List<Item>> rootItems = new HashMap<>();

    public void addItem(Root root, Item item) {
        List<Item> items = findAllByRoot(root);
        items.add(item);
        rootItems.put(root, items);
    }

    public List<Item> findAllByRoot(Root root) {
        return rootItems.getOrDefault(root, new ArrayList<>());
    }

    public void deleteAllByRoot(Root root) {
        rootItems.put(root, new ArrayList<>());
    }

    public List<Item> getChildren(Item rootItem) {
        List<Item> rootChildren = findAllByRoot(rootItem.getRoot());
        return rootChildren.stream()
                .filter(rootItem::isClosestParent)
                .collect(Collectors.toList());
    }

    public Item getRootItem(Root root) {
        List<Item> items = findAllByRoot(root);
        return items.stream()
                .filter(item -> item.getPathname().equals(root.getPathname()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }
}
