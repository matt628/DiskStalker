package pl.edu.agh.diskstalker.presenter;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.List;

public class TreeBuilder {
    public TreeBuilder() {
    } // dependency injection this should be a singleton

    public TreeView<String> buildTree(String path) { // this string should be change to input folder
        // tree view
        TreeView<String> treeView = new TreeView<>();

        //main root
        TreeItem<String> mainRoot = new TreeItem<>(path);

        buildChildrenTree(mainRoot);

        return treeView;
    }

    private void buildChildrenTree(TreeItem<String> parent) {
        List<String> children = getChildren(parent.getValue());
        for (var child : children) {
            TreeItem<String> childItem = new TreeItem<>(child);
            buildChildrenTree(childItem);
            childItem.getChildren().add(childItem);
        }
    }

    private List<String> getChildren(String path) {

        // TODO: Create getChildren method

        return null;
//        return item.getChildren();
//        return Item.getChildren(path).stream().map(Item::getPathname).collect(Collectors.toList());
    }
}
