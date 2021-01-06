package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;

public class FolderDetailsHandler {

    @Inject
    private TreeHandler treeHandler;

    public void unsubscribeFromRoot(Root root) {
        Root.deleteById(root.getId());
        treeHandler.updateRootList();
        SoundEffects.playSound("delete_surprise.wav");
        System.out.println("Unsubscribed from " + root.getName());
    }

    public void deleteRoot(String filePath) throws IOException {
        File file = new File(filePath);
        FileUtils.deleteDirectory(file);
        SoundEffects.playSound("delete_surprise.wav");
        System.out.println("Folder deleted........");
    }

    public void cleanRoot(String directoryPath) throws IOException{
        File directory = new File(directoryPath);
        FileUtils.cleanDirectory(directory);
        System.out.println("Folder cleaned........");
    }
}
