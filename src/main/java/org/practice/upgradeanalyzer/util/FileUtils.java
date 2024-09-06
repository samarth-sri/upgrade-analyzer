package org.practice.upgradeanalyzer.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    private FileUtils() {
        throw new AssertionError("constructor not allowed");
    }

    public static String getCurrentDirectoryNameOfFile(Path filePath) {
        // If the path is null or empty, return null
        if (filePath == null || filePath.getNameCount() == 0) {
            System.out.println("The path is empty or invalid.");
            return null;
        }
        // Get the parent directory of the file
        Path parentDir = filePath.getParent();
        // If the parent directory is null (e.g., when the file path is the root), return null
        if (parentDir == null) {
            System.out.println("No parent directory found for the given path.");
            return null;
        }
        // If the parent directory is null (e.g., when the file path is the root), return null
        if (parentDir == null) {
            System.out.println("No parent directory found for the given path.");
            return null;
        }

        // Get the name of the current directory (the last element of the parent path)
        Path currentDir = parentDir.getFileName();

        // Return the last element as the current directory name
        return currentDir != null ? currentDir.toString() : null;
    }

    public static boolean isValidFilePath(String filePath) {
        Path path = Path.of(filePath);
        return Files.exists(path) && Files.isRegularFile(path);
    }

    public static boolean isValidDirectoryPath(String directoryPath) {
        Path path = Paths.get(directoryPath);
        return Files.exists(path) && Files.isDirectory(path);
    }

    public static String getRelativePath(String basePath, Path childPath) {
        return Paths.get(basePath).relativize(childPath).toString();
    }
}
