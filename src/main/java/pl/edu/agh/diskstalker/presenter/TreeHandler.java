package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.control.TreeItem;
import pl.edu.agh.diskstalker.controller.MainViewController;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TreeHandler {

    private final Map<Root, TreeItem<Item>> trees = new HashMap<>();

    @Inject
    private ItemDataMapper itemDataMapper;
    @Inject
    private TreeBuilder treeBuilder;
    @Inject
    private RootDataMapper rootDataMapper;
    @Inject
    private MainViewController mainViewController;

    public void updateRootList() {
        List<Root> roots = rootDataMapper.findAll();
        mainViewController.updateFolderRootList(roots);
    }

    public void updateTree(Root root) {
        TreeItem<Item> treeRoot = trees.get(root);
        mainViewController.updateFolderTreeView(treeRoot);
    }

    public void buildTree(Root root) {
        Item itemRoot = itemDataMapper.getRootItem(root);
        TreeItem<Item> treeRoot = treeBuilder.buildTree(itemRoot);
        trees.put(root, treeRoot);
        mainViewController.updateFolderTreeView(treeRoot);
        updateProgressBar(itemRoot, root);
    }

    private void updateProgressBar(Item itemRoot, Root root) {
        double progress = (double) itemRoot.getSize() / (double) root.getMaxSize();
        mainViewController.updateProgressBarView(progress);
    }

    public void cleanTree() {
        mainViewController.updateFolderTreeView(null);
    }

    public void cleanProgressBar() {
        mainViewController.updateProgressBarView(0);
    }
}
