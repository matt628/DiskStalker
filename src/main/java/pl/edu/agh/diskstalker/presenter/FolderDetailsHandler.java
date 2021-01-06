package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class FolderDetailsHandler {

    @Inject
    private TreeHandler treeHandler;

    public void unsubscribeFromRoot(Root root) {
        Root.deleteById(root.getId());
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

    public void updateRoot(Root root) {
        Optional<Root> rootToDelete = Root.findByLocation(root.getName(), root.getPath());
        rootToDelete.ifPresent(value -> Root.deleteById(value.getId()));

        Root.create(root.getName(), root.getPath(), root.getMaxSize());
        treeHandler.updateRootList();
    }
}
