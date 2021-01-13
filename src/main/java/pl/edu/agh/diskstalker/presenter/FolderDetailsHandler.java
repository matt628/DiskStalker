package pl.edu.agh.diskstalker.presenter;

import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import pl.edu.agh.diskstalker.database.datamapper.RootDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;

import java.io.File;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class FolderDetailsHandler {

    @Inject
    private TreeHandler treeHandler;

    @Inject
    private FolderAnalyzerHandler analyzerHandler;

    @Inject
    private RootDataMapper rootDataMapper;


    public void unsubscribeFromRoot(Root root) {
        analyzerHandler.stopWatchDirectory(root);
        rootDataMapper.deleteById(root.getId());
        treeHandler.updateRootList();
        SoundEffects.playSound("delete_surprise.wav");
        System.out.println("Unsubscribed from " + root.getName());
    }

    public void deleteRoot(Root root) {
        File file = new File(root.getPathname());
        try {
            FileUtils.deleteDirectory(file);
            SoundEffects.playSound("delete_surprise.wav");
            System.out.println("Folder deleted........");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cleanRoot(Root root) {
        File directory = new File(root.getPathname());
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
        treeHandler.updateRootList();
    }

    public void deleteItem(Item item){
        // FileUtils
        File file = new File(item.getPathname2());
        if(file.isDirectory()) {
            try {
                FileUtils.deleteDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(file.isFile()){
            if(!file.delete()) {
                System.out.println("File was not deleted succesfully");
            }
        }
    }


}
