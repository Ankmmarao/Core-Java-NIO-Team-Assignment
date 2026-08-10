package com.iispl.nio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class CTSFolderWatcher {

    private final Path folderPath;
    private final WatchService watchService;

    public CTSFolderWatcher(Path folderPath) throws IOException {

        this.folderPath = folderPath;

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        watchService =
                FileSystems.getDefault().newWatchService();

        folderPath.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE
        );
    }

    public Path waitForNextXmlFile()
            throws InterruptedException {

        while (true) {

            WatchKey key = watchService.take();

            for (WatchEvent<?> event : key.pollEvents()) {

                WatchEvent.Kind<?> kind = event.kind();

                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> pathEvent =
                        (WatchEvent<Path>) event;

                Path fileName = pathEvent.context();

                if (kind == StandardWatchEventKinds.ENTRY_CREATE
                        && fileName.toString()
                                  .toLowerCase()
                                  .endsWith(".xml")) {

                    Path fullPath =
                            folderPath.resolve(fileName);

                    key.reset();

                    return fullPath;
                }
            }

            boolean valid = key.reset();

            if (!valid) {
                return null;
            }
        }
    }

    public void close() throws IOException {
        watchService.close();
    }
}