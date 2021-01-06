package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.controller.PopUpNotification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FolderAnalyzerHandler {

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private PopUpNotification popUpNotification;

    private List<WatchDirectory> watchDirectories;

    public FolderAnalyzerHandler() throws IOException {
        for(Root root : Root.findAll()){
            addWatchDirectory(root);
        }
    }

    public void analyzeAll() {
        List<Root> roots = Root.findAll();
        roots.forEach(this::analyzeRoot);
    }

    public void analyzeRoot(Root root) {
        Path startingDir = Paths.get(root.getPathname());
        root.getItems().clear();
        try {
            Files.walkFileTree(startingDir, new FolderAnalyzer(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        treeHandler.updateTree(root);
        if (exceedSpace(root)) {
            notifyByPopUp(root);
        }
    }

    private boolean  exceedSpace(Root root) {
        return root.getSize() > root.getMaxSize();
    }

    public void notifyByPopUp(Root root) {
        try {
            popUpNotification.displayTray("DiskStalker",
                    root.getPathname() + "exceeded the space limit of space");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addWatchDirectory(Root root) throws IOException {
        WatchDirectory watchDirectory = WatchDirectory.watch(root, this);
//        watchDirectory.stopWatching();
//        watchDirectories.add(watchDirectory);
    }
}
