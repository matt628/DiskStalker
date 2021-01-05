package pl.edu.agh.diskstalker.presenter;

import javafx.scene.control.TreeView;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.util.List;

public class TreeHandler {
//    TODO is should connects root and tree builder
    TreeBuilder treeBuilder;
    MainViewController mainViewController;

    public TreeHandler(TreeBuilder treeBuilder, MainViewController mainViewController) {
        this.treeBuilder = treeBuilder;
        this.mainViewController = mainViewController;
    }

    public void buildTree(){



    }

    public TreeView<String> updateTree(Root root) {
        List<Item> items = root.getItems();

        return new TreeView<>();
    }
}
