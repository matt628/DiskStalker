package pl.edu.agh.diskstalker.presenter;

import pl.edu.agh.diskstalker.database.datamapper.ItemDataMapper;
import pl.edu.agh.diskstalker.database.datamapper.TypeDataMapper;
import pl.edu.agh.diskstalker.database.model.Item;
import pl.edu.agh.diskstalker.database.model.Root;
import pl.edu.agh.diskstalker.database.model.Type;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FolderAnalyzer extends SimpleFileVisitor<Path> {

    private final ItemDataMapper itemDataMapper;
    private final TypeDataMapper typeDataMapper;
    private final Root root;

    public FolderAnalyzer(ItemDataMapper itemDataMapper, TypeDataMapper typeDataMapper, Root root) {
        this.itemDataMapper = itemDataMapper;
        this.typeDataMapper = typeDataMapper;
        this.root = root;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        String nameWithType = file.getFileName().toString();

        String name = nameWithType.substring(0, nameWithType.lastIndexOf('.'));
        String typeString = nameWithType.substring(nameWithType.lastIndexOf('.'));
        String path = file.getParent().toString();

        Type type = typeDataMapper.getType(typeString);
        long size = attr.size();

        Item fileItem = new Item(name, path, type, size, root);

        itemDataMapper.addItem(root, fileItem);

        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        String name = dir.getFileName().toString();
        String path = dir.getParent().toString();

        Type type = typeDataMapper.getType("folder");
        long size = getDirSize(path, name);


        Item folderItem = new Item(name, path, type, size, root);

        itemDataMapper.addItem(root, folderItem);

        return CONTINUE;
    }

    private long getDirSize(String path, String name) {
        return itemDataMapper.findAllByRoot(root).stream()
                .filter(item -> item.isChild(path + File.separator + name))
                .filter(Item::isFile)
                .map(Item::getSize)
                .reduce(0L, Long::sum);
    }
}
