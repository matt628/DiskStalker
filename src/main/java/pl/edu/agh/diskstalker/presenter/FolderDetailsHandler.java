package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class FolderDetailsHandler {

    private final Logger logger = Logger.getGlobal();

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private FolderAnalyzerHandler analyzerHandler;

    @Inject
    private StatisticsHandler statisticsHandler;

    @Inject
    private RootDataMapper rootDataMapper;

    public void unsubscribeFromRoot(Root root) {
        analyzerHandler.stopWatchDirectory(root);
        rootDataMapper.deleteById(root.getId());
        treeHandler.updateRootList();
        treeHandler.cleanTree();
        treeHandler.cleanProgressBar();
        statisticsHandler.cleanStatistics();
        logger.info("Unsubscribed from " + root.getName());
    }

    public void deleteRoot(Root root) {
        File file = new File(root.getPathname());
        try {
            unsubscribeFromRoot(root);
            FileUtils.deleteDirectory(file);
            logger.info("Folder " + root.getPathname() + " deleted");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanRoot(Root root) {
        File directory = new File(root.getPathname());
        try {
            FileUtils.cleanDirectory(directory);
            logger.info("Folder " + root.getPathname() + " cleaned");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRoot(String name, String path, long maxSize, long maxTreeSize) {
        Optional<Root> rootToDelete = rootDataMapper.findByLocation(name, path);
        rootToDelete.ifPresent(value -> rootDataMapper.deleteById(value.getId()));

        Optional<Root> optionalRoot = rootDataMapper.create(name, path, maxSize, maxTreeSize);

        optionalRoot.ifPresent(root -> {
            analyzerHandler.analyzeRoot(root);
            analyzerHandler.addWatchDirectory(root);
            treeHandler.updateRootList();
        });
    }

    public void deleteItem(Item item) {
        File file = new File(item.getPathname());
        if (file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
                logger.info("Folder " + item.getPathname() + " deleted");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (file.isFile()) {
            if (!file.delete()) {
                logger.info("File " + item.getPathname() + " deleted");
            }
        }
    }


}
