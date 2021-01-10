package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import javafx.scene.control.TreeItem;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.util.List;

public class TreeHandler {

    @Inject
    private TreeBuilder treeBuilder;

    @Inject
    private RootDataMapper rootDataMapper;

    @Inject
    ItemDataMapper itemDataMapper;

    @Inject
    private MainViewController mainViewController;

    public void updateTree(Root root) {
        Item itemRoot = itemDataMapper.findAllByRoot(root).stream()
                .filter(item -> item.getPathname().equals(root.getPathname()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        TreeItem<Item> treeRoot = treeBuilder.buildTree(itemRoot);
        mainViewController.updateFolderTreeView(treeRoot);
    }

    public void updateRootList() {
        List<Root> roots = rootDataMapper.findAll();
        mainViewController.updateFolderRootList(roots);
    }
}
