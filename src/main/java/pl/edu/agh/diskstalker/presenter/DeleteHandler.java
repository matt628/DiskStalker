package pl.edu.agh.diskstalker.presenter;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class DeleteHandler {
    public DeleteHandler() {
    }

    /**
     * This method delete a folder and files in it. USE WITH CAUTION!
     * @param filePath file path in format "E://ExampleDirectory//";
     */
    public static void deleter(String filePath) throws IOException {
        //Creating the File object
        File file = new File(filePath);
        FileUtils.deleteDirectory(file);
        System.out.println("Files deleted........");
    }
}
