package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Root;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FolderDetailsHandler {

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private RootDataMapper rootDataMapper;

    public void unsubscribeFromRoot(Root root) {
//        FolderAnalyzerHandler.stopWatchDirectory(root);
        rootDataMapper.deleteById(root.getId());
        treeHandler.updateRootList();
        SoundEffects.playSound("delete_surprise.wav");
        System.out.println("Unsubscribed from " + root.getName());
    }

    public void deleteRoot(String filePath) {
        File file = new File(filePath);
        try {
            FileUtils.deleteDirectory(file);
            SoundEffects.playSound("delete_surprise.wav");
            System.out.println("Folder deleted........");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanRoot(String directoryPath){
        File directory = new File(directoryPath);
        try {
            FileUtils.cleanDirectory(directory);
            SoundEffects.playSound("delete_surprise.wav");
            System.out.println("Folder cleaned........");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRoot(String name, String path, long maxSize) {
        Optional<Root> rootToDelete = rootDataMapper.findByLocation(name, path);
        rootToDelete.ifPresent(value -> rootDataMapper.deleteById(value.getId()));

        rootDataMapper.create(name, path, maxSize);

        // TODO: Uncomment when FolderDetailsController will get FolderDetailsHandler
//        treeHandler.updateRootList();
    }
}
