package pl.edu.agh.diskstalker.presenter;

import org.apache.commons.io.FileUtils;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;

import java.nio.file.attribute.*;
import java.io.*;
import java.util.*;


/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDirectory {

    private final WatchService watcher;
    private final Map<WatchKey, Path> keys;
    private final boolean trace;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
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
     * Creates a WatchService and registers the given directory
     */
    WatchDirectory(Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey, Path>();
        System.out.format("Scanning %s ...\n", dir);
        registerAll(dir);
        System.out.println("Done.");

        // enable trace after initial registration
        this.trace = true;
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
        for (; ; ) {
            // wait for key to be signalled
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
                WatchEvent.Kind kind = event.kind();

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                if (kind == ENTRY_CREATE) {
                    //TODO: on create handler; child is type of Path
                    System.out.format("%s: %s \n", event.kind().name(), child);
                } else if (kind == ENTRY_DELETE) {
                    //TODO: on delete handler
                    System.out.format("%s: %s \n", event.kind().name(), child);
                } else if (kind == ENTRY_MODIFY) {
                    //TODO: on modify handler; child - type of Path; child.toFile().length() - size of file in bytes
                    System.out.format("%s: %s %s\n", event.kind().name(), child, getFileOrDirSize(child.toFile()));
                }

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (kind == ENTRY_CREATE) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //args[0] - String of full path to dir
        Path dir = Paths.get(args[0]);
        new WatchDirectory(dir).processEvents();
    }
}
