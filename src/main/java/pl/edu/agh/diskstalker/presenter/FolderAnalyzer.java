package pl.edu.agh.diskstalker.presenter;

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

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        String nameWithType = file.getFileName().toString();
        String name = nameWithType.substring(0, nameWithType.lastIndexOf('.'));
        String type = nameWithType.substring(nameWithType.lastIndexOf('.'));
        String path = file.getParent() + File.separator;

        root.getItems().add(new Item(name, path, type, String.valueOf(attr.size()), root));

        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent() + File.separator;

        if (path.equals(root.getPath()) && name.equals(root.getName())) {
            return CONTINUE;
        }

        root.getItems().add(new Item(name, path, null, "0", root));

        return CONTINUE;
    }
}
