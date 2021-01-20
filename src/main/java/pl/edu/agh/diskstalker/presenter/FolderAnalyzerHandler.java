package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import pl.edu.agh.diskstalker.controller.PopUpNotification;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.TypeDataMapper;
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
    private TypeDataMapper typeDataMapper;
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
            Files.walkFileTree(startingDir, new FolderAnalyzer(itemDataMapper, typeDataMapper, root));
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeHandler.updateTree(root);
        statisticsHandler.updateStatistics(root);

        if (exceedSpace(root)) {
            notifyByPopUp(root);
        }
    }

    private boolean exceedSpace(Root root) {
        Item rootItem = itemDataMapper.getRootItem(root);
        return rootItem.getSize() > root.getMaxSize();
    }

    public void notifyByPopUp(Root root) {
        try {
            PopUpNotification.displayTray("DiskStalker",
                    root.getPathname() + "exceeded the space limit of space");
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
