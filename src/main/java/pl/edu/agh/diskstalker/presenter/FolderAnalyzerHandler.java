package pl.edu.agh.diskstalker.presenter;

import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;
import pl.edu.agh.diskstalker.controller.PopUpNotification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FolderAnalyzerHandler {

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
    }

    public void notifyByPopUp(String rootPath) {
        try {
            PopUpNotification.displayTray("DiskStalker", rootPath + "exceeded the space limit of space");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
