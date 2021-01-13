package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;

import java.util.List;

public class TreeBuilder {

    @Inject
    private ItemDataMapper itemDataMapper;

    public TreeItem<Item> buildTree(Item item) {
        TreeItem<Item> root = new TreeItem<>(item);
        buildChildrenTree(root);
        return root;
    }

    private void buildChildrenTree(TreeItem<Item> parent) {
        List<Item> children = getChildren(parent.getValue());
        for (Item child : children) {
            TreeItem<Item> childItem = new TreeItem<>(child);
            parent.getChildren().add(childItem);
            buildChildrenTree(childItem);
        }
    }

    private List<Item> getChildren(Item item) {
        return itemDataMapper.getChildren(item);
    }
}
