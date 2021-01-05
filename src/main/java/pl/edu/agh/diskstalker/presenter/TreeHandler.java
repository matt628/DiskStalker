package pl.edu.agh.diskstalker.presenter;

import javafx.scene.control.TreeView;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

public class TreeHandler {
    private final TreeBuilder treeBuilder;
    private final MainViewController mainViewController;

    // TODO: Juice injection
    public TreeHandler(TreeBuilder treeBuilder, MainViewController mainViewController) {
        this.treeBuilder = treeBuilder;
        this.mainViewController = mainViewController;
    }

    public TreeView<Item> buildTree(Root root) {
        Item itemRoot = root.getItems().stream()
                .filter(item -> item.getPathname().equals(root.getPathname()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        return treeBuilder.buildTree(itemRoot);
    }
}
