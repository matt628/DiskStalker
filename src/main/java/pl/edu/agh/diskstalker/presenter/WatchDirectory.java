package pl.edu.agh.diskstalker.presenter;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javafx.application.Platform;
import pl.edu.agh.diskstalker.database.model.Root;

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
    private final Root root;
    private final FolderAnalyzerHandler folderAnalyzerHandler;
    private volatile boolean closeWatcherThread;

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDirectory(Root root, FolderAnalyzerHandler folderAnalyzerHandler) throws IOException {
        this.folderAnalyzerHandler = folderAnalyzerHandler;
        this.root = root;
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        System.out.format("Scanning %s ...\n", root.getPathname());
        registerAll(Paths.get(root.getPathname()));
        System.out.println("Done.");

        // enable trace after initial registration
        this.trace = true;
    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    public static WatchDirectory watch(Root root, FolderAnalyzerHandler handler) throws IOException {
        final WatchDirectory watchDir = new WatchDirectory(root, handler);
        watchDir.closeWatcherThread = false;

        Completable.fromRunnable(watchDir::processEvents)
                .subscribeOn(Schedulers.io())
                .subscribe();

        return watchDir;
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

    private void handleEvent(WatchEvent<?> event, Path dir) {
        WatchEvent.Kind kind = event.kind();

        WatchEvent<Path> ev = cast(event);
        Path name = ev.context();
        Path child = dir.resolve(name);

        if (Objects.equals(kind, ENTRY_DELETE)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s \n", event.kind().name(), child);
        } else if (Objects.equals(kind, ENTRY_MODIFY)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s\n", event.kind().name(), child);
        } else if (Objects.equals(kind, ENTRY_CREATE)) {
            Platform.runLater(() -> folderAnalyzerHandler.analyzeRoot(root));
            System.out.format("%s: %s \n", event.kind().name(), child);
            try {
                if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                    registerAll(child);
                }
            } catch (IOException x) {
                // ignore to keep sample readable
            }
        }
    }

    public void stopWatching() {
        try {
            watcher.close();
        } catch (IOException ioe) {
            System.out.println("This is it");
        }
        closeWatcherThread = true;
    }
}
