package org.practice.upgradeanalyzer.scanner;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DirectoryScanner {
    private static final DirectoryScanner instance = new DirectoryScanner();

    private DirectoryScanner() {
    }

    public static DirectoryScanner getInstance() {
        return instance;
    }

    // Use Predicate, Consumer as args to make it abstract
    public void scanDirectoryRecursively(String directoryPath, Predicate<Path> filterPredicate, Consumer<Path> fileProcessor) throws Exception {
        Path path = Paths.get(directoryPath);
        try (Stream<Path> paths = Files.walk(path, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            paths
                    .filter(filterPredicate)
                    .forEach(fileProcessor);
        } catch (IOException e) {
            System.out.println("Caught IOException scanning the directory: " + directoryPath);
            System.out.println();
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Caught Exception while scanning the directory: " + directoryPath);
            System.out.println();
            e.printStackTrace();
        }
    }

}
