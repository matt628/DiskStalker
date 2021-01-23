package pl.edu.agh.diskstalker.presenter;

import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

public class FolderAnalyzer extends SimpleFileVisitor<Path> {

    private final ItemDataMapper itemDataMapper;
    private final Root root;

    public FolderAnalyzer(ItemDataMapper itemDataMapper, Root root) {
        this.itemDataMapper = itemDataMapper;
        this.root = root;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        String name = file.getFileName().toString();
        String path = file.getParent().toString();

        String typeString = getTypeString(name);
        Type type = Type.getType(typeString);

        long size = attr.size();

        Item fileItem = new Item(name, path, type, size, root);
        itemDataMapper.addItem(fileItem);

        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent().toString();

        Type type = Type.getType("folder");
        long size = getDirSize(path, name);

        Item folderItem = new Item(name, path, type, size, root);
        itemDataMapper.addItem(folderItem);

        return CONTINUE;
    }

    private String getTypeString(String nameWithType) {
        return containsType(nameWithType) ?
                nameWithType.substring(nameWithType.lastIndexOf('.')) : "";
    }

    private boolean containsType(String nameWithType) {
        return nameWithType.contains(".");
    }

    private long getDirSize(String path, String name) {
        return itemDataMapper.findAllByRoot(root).stream()
                .filter(item -> item.isChild(path + File.separator + name))
                .filter(Item::isFile)
                .map(Item::getSize)
                .reduce(0L, Long::sum);
    }
}
