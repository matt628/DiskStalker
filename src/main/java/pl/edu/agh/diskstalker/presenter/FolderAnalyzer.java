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
        long size = attr.size();

        Item fileItem = new Item(name, path, type, size, root);

        root.addItem(fileItem);

        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent().toString();
        long size = root.getItems().stream()
                .filter(item -> item.isChild(path + File.separator + name))
                .filter(Item::isFile)
                .map(Item::getSize)
                .reduce(0L, Long::sum);

        Item folderItem = new Item(name, path, null, size, root);

        root.getItems().add(folderItem);

        return CONTINUE;
    }
}
