package pl.edu.agh.diskstalker.analyzer;

import pl.edu.agh.diskstalker.model.Item;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Optional;

import static java.nio.file.FileVisitResult.*;

public class FolderAnalyzer extends SimpleFileVisitor<Path> {

    private final Root root;

    public FolderAnalyzer(Root root) {
        this.root = root;
    }

    // Print information about
    // each type of file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        if (attr.isRegularFile()) {
            String nameWithType = file.getFileName().toString();
            String name = nameWithType.substring(0, nameWithType.lastIndexOf('.'));
            String type = nameWithType.substring(nameWithType.lastIndexOf('.'));
            String path = file.getParent() + File.separator;

            Item.create(name, path, type, String.valueOf(attr.size()), root);
        }
        return CONTINUE;
    }

    // Print each directory visited.
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

        System.out.format("Directory: %s%n", dir.toString());
        return CONTINUE;
    }

    public static void main(String[] args) {
        Path startingDir = Paths.get("C:\\Users\\vladi\\Desktop\\TestFolder");
        Optional<Root> root = Root.create("TestFolder", "C:\\Users\\vladi\\Desktop\\", "3453", "354645");
        if (root.isPresent()) {
            FolderAnalyzer fa = new FolderAnalyzer(root.get());
            try {
                Files.walkFileTree(startingDir, fa);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
