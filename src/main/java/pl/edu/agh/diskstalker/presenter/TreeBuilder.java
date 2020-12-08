package pl.edu.agh.diskstalker.presenter;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import pl.edu.agh.diskstalker.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TreeBuilder {
    public TreeBuilder() {
    } // dependency injection this should be a singelton

    void buildTree(String path){ // this string should be change to input folder
        // tree view
        TreeView<String> treeView = new TreeView<>();

        //main root
        TreeItem<String> mainRoot = new TreeItem<>(path);

        buildChildrenTree(mainRoot);

    }

    void buildChildrenTree(TreeItem<String> parent){
        List<String> children = getChildren(parent.getValue());
        for(var child : children){
            TreeItem<String> childItem = new TreeItem<>(child);
            buildChildrenTree(childItem);
            childItem.getChildren().add(childItem);
        }
    }

    List<String> getChildren(String path){
        return Item.getChildren(path).stream().map(Item::getPathname).collect(Collectors.toList());
    }



}
