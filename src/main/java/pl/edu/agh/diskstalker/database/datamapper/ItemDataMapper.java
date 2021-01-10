package pl.edu.agh.diskstalker.database.datamapper;

import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.util.List;

public interface ItemDataMapper {

    void addItem(Root root, Item item);

    List<Item> findAllByRoot(Root root);

    void deleteAllByRoot(Root root);

    List<Item> getChildren(Item rootItem);
}
