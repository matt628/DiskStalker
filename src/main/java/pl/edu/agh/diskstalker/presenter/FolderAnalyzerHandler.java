package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import pl.edu.agh.diskstalker.controller.PopUpNotification;
import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.NotificationType;
import pl.edu.agh.diskstalker.database.model.Root;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.edu.agh.diskstalker.Main.APPLICATION_NAME;
import static pl.edu.agh.diskstalker.Main.NOTIFICATION_FREQUENCY_IN_HOURS;
import static pl.edu.agh.diskstalker.database.model.NotificationType.*;
import static pl.edu.agh.diskstalker.presenter.Converter.bytesToString;

public class FolderAnalyzerHandler {

    private final Map<NotificationType, LocalDateTime> lastNotifications = new HashMap<>();
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
        if (exceedSpace(root) && isTime(BY_ROOT_SIZE)) {
            showNotification(root.getPathname() + " exceeded the space limit of " + bytesToString(root.getMaxSize()));
            lastNotifications.put(BY_ROOT_SIZE, LocalDateTime.now());
        }
        if (exceedTreeSize(root) && isTime(BY_TREE_SIZE)) {
            showNotification(root.getPathname() + " exceeded the max number of files of " + bytesToString(root.getMaxTreeSize()));
            lastNotifications.put(BY_TREE_SIZE, LocalDateTime.now());
        }
        if (exceedFileSize(root) && isTime(BY_FILE_SIZE)) {
            showNotification(root.getPathname() + " has files which exceeded the space limit of " + bytesToString(root.getMaxFileSize()));
            lastNotifications.put(BY_FILE_SIZE, LocalDateTime.now());
        }
    }

    private boolean isTime(NotificationType type) {
        LocalDateTime lastNotification = lastNotifications.get(type);
        if (lastNotification == null) return true;

        Duration timeDiff = Duration.between(lastNotification, LocalDateTime.now());
        Duration notificationTime = Duration.ofHours(NOTIFICATION_FREQUENCY_IN_HOURS);

        return timeDiff.compareTo(notificationTime) > 0;
    }

    private boolean exceedSpace(Root root) {
        Item rootItem = itemDataMapper.getRootItem(root);
        return root.getMaxSize() != 0 && rootItem.getSize() > root.getMaxSize();
    }

    private boolean exceedTreeSize(Root root) {
        return root.getMaxTreeSize() != 0 && itemDataMapper.getTreeSize(root) > root.getMaxTreeSize();
    }

    private boolean exceedFileSize(Root root) {
        List<Item> items = itemDataMapper.findAllByRoot(root);
        return root.getMaxFileSize() != 0 && items.stream()
                .filter(Item::isFile)
                .anyMatch(item -> item.getSize() > root.getMaxFileSize());
    }

    public void showNotification(String message) {
        try {
            PopUpNotification.displayTray(APPLICATION_NAME, message);
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
