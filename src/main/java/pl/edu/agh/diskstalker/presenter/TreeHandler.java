package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import javafx.scene.control.TreeItem;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.util.List;

public class TreeHandler {

    @Inject
    private TreeBuilder treeBuilder;

    @Inject
    private MainViewController mainViewController;

    public void buildTree(Root root) {
        Item itemRoot = root.getItems().stream()
                .filter(item -> item.getPathname().equals(root.getPathname()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        TreeItem<Item> treeRoot = treeBuilder.buildTree(itemRoot);
        mainViewController.updateFolderTreeView(treeRoot);
    }

    public void updateRootList() {
        List<Root> roots = Root.findAll();
        mainViewController.updateFolderRootList(roots);
    }
}
