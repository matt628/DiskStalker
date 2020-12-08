package pl.edu.agh.diskstalker.analyzer;

import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FolderAnalyzer extends SimpleFileVisitor<Path> {

    private final Root root;

    public FolderAnalyzer(Root root) {
        this.root = root;
    }

    // Save regular files and symbolic links to the database
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        String nameWithType = file.getFileName().toString();
        String name = nameWithType.substring(0, nameWithType.lastIndexOf('.'));
        String type = nameWithType.substring(nameWithType.lastIndexOf('.'));
        String path = file.getParent() + File.separator;

        Item.create(name, path, type, String.valueOf(attr.size()), root);
        return CONTINUE;
    }

    // Save directories to the database
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent() + File.separator;

        if (path.equals(root.getPath()) && name.equals(root.getName())) {
            return CONTINUE;
        }

        Item.create(name, path, null, "0", root);
        return CONTINUE;
    }

//    This was quick testing, bigger tests are coming
//
//    public static void main(String[] args) {
//        Path startingDir = Paths.get("C:\\Users\\vladi\\Desktop\\TestFolder");
//        Optional<Root> root = Root.create("TestFolder", "C:\\Users\\vladi\\Desktop\\", "3453", "354645");
//        if (root.isPresent()) {
//            FolderAnalyzer fa = new FolderAnalyzer(root.get());
//            try {
//                Files.walkFileTree(startingDir, fa);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
