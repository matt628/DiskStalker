package pl.edu.agh.diskstalker.presenter;

import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FolderAnalyzer extends SimpleFileVisitor<Path> {

    private final Root root;

    public FolderAnalyzer(Root root) {
        this.root = root;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        String nameWithType = file.getFileName().toString();
        String name = nameWithType.substring(0, nameWithType.lastIndexOf('.'));
        String type = nameWithType.substring(nameWithType.lastIndexOf('.'));
        String path = file.getParent().toString();

        root.getItems().add(new Item(name, path, type, String.valueOf(attr.size()), root));
        System.out.println("file: " + name + " " + path + " " + type);

        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent().toString();

        root.getItems().add(new Item(name, path, null, "0", root));
        System.out.println("folder: " + name + " " + path);

        return CONTINUE;
    }

    public static void main(String[] args) {
        Root root = new Root(0, "folder", "C:\\Users\\vladi\\Desktop", "100");
        Path startingDir = Paths.get(root.getPathname());
        root.getItems().clear();
        try {
            Files.walkFileTree(startingDir, new FolderAnalyzer(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
