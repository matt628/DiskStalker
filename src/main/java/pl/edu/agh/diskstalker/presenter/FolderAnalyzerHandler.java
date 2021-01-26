package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import pl.edu.agh.diskstalker.controller.PopUpNotification;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FolderAnalyzerHandler {

    private final List<WatchDirectory> watchDirectories = new ArrayList<>();
    @Inject
    private RootDataMapper rootDataMapper;
    @Inject
    private ItemDataMapper itemDataMapper;
    @Inject
    private TreeHandler treeHandler;
    @Inject
    private StatisticsHandler statisticsHandler;

    public void stopWatchDirectory(Root root) {
        for (WatchDirectory w : watchDirectories) {
            if (w.getRoot().equals(root)) {
                watchDirectories.remove(w);
                w.stopWatching();
            }
        }
    }

    public void analyzeAll() {
        List<Root> roots = rootDataMapper.findAll();
        roots.forEach(this::analyzeRoot);
    }

    public void analyzeRoot(Root root) {
        Path startingDir = Paths.get(root.getPathname());
        itemDataMapper.deleteAllByRoot(root);
        try {
            Files.walkFileTree(startingDir, new FolderAnalyzer(itemDataMapper, root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeHandler.buildTree(root);
        statisticsHandler.updateStatistics(root);

        notifyUser(root);
    }

    private void notifyUser(Root root) {
        if (exceedSpace(root)) {
            showNotification(root.getPathname() + " exceeded the space limit of " + root.getMaxSize());
        }
        if (exceedTreeSize(root)) {
            showNotification(root.getPathname() + " exceeded the max number of files of " + root.getMaxTreeSize());
        }
        if (exceedFileSize(root)) {
            showNotification(root.getPathname() + " has files which exceeded the space limit of " + root.getMaxFileSize());
        }
    }

    private boolean exceedSpace(Root root) {
        Item rootItem = itemDataMapper.getRootItem(root);
        return rootItem.getSize() > root.getMaxSize();
    }

    private boolean exceedTreeSize(Root root) {
        return itemDataMapper.getTreeSize(root) > root.getMaxTreeSize();
    }

    private boolean exceedFileSize(Root root) {
        List<Item> items = itemDataMapper.findAllByRoot(root);
        return items.stream()
                .filter(Item::isFile)
                .anyMatch(item -> item.getSize() > root.getMaxFileSize());
    }

    public void showNotification(String message) {
        try {
            PopUpNotification.displayTray("DiskStalker", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDirectories() {
        for (Root root : rootDataMapper.findAll()) {
            addWatchDirectory(root);
        }
    }

    public void addWatchDirectory(Root root) {
        try {
            WatchDirectory watchDirectory = WatchDirectory.watch(root, this);
            watchDirectories.add(watchDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
