package pl.edu.agh.diskstalker.presenter;

import javafx.application.Platform;
import pl.edu.agh.diskstalker.model.Root;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;


/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDirectory {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean trace;
    private volatile boolean closeWatcherThread;
    private final Root root;
    private final FolderAnalyzerHandler folderAnalyzerHandler;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDirectory(Root root, FolderAnalyzerHandler folderAnalyzerHandler) throws IOException {
        this.folderAnalyzerHandler = folderAnalyzerHandler;
        this.root = root;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        System.out.format("Scanning %s ...\n", root.getPath());
        registerAll(Paths.get(root.getPath()));
        System.out.println("Done.");

        // enable trace after initial registration
        this.trace = true;
    }

    public Root getRoot() {
        return root;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    private long getFileOrDirSize(File fileOrDir) {
        if(fileOrDir.isFile()){
            return fileOrDir.length();
        }else{
            long length = 0;
            for (File file : fileOrDir.listFiles()) {
                if (file.isFile())
                    length += file.length();
                else
                    length += getFileOrDirSize(file);
            }
            return length;
        }
    }
    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        while (!closeWatcherThread) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                System.err.println("Can't take key");
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                handleEvent(event, dir);
            }

            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    private void handleEvent(WatchEvent<?> event, Path dir){
        WatchEvent.Kind kind = event.kind();

        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

        if (Objects.equals(kind, ENTRY_DELETE)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s \n", event.kind().name(), child);
        } else if (Objects.equals(kind, ENTRY_MODIFY)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s %s\n", event.kind().name(), child, getFileOrDirSize(child.toFile()));
        }else if (Objects.equals(kind,ENTRY_CREATE)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s \n", event.kind().name(), child);
            try {
                if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                    registerAll(child);
                }
            } catch (IOException x) {
                // ignore to keep sample readbale
            }
        }
    }

    public void stopWatching(){
        try{
            watcher.close();
        }catch(IOException ioe){
        }
        closeWatcherThread = true;
    }



    public static WatchDirectory watch(Root root, FolderAnalyzerHandler handler) throws IOException {
        final WatchDirectory watchDir = new WatchDirectory(root, handler);
        watchDir.closeWatcherThread = false;
        new Thread(watchDir::processEvents, "DirWatcherThread").start();
        return watchDir;
    }

}
