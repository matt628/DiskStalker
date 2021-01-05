package pl.edu.agh.diskstalker.presenter;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.diskstalker.model.Item;

import java.util.List;

public class TreeBuilder {

    public TreeView<Item> buildTree(Item item) {
        TreeView<Item> treeView = new TreeView<>();
        TreeItem<Item> mainRoot = new TreeItem<>(item);
        buildChildrenTree(mainRoot);
        treeView.setRoot(mainRoot);

        return treeView;
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
        return item.getChildren();
    }
}
