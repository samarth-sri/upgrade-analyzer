package org.practice.upgradeanalyzer.scanner;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DirectoryScanner {
    private static final DirectoryScanner instance = new DirectoryScanner();

    private DirectoryScanner() {
    }

    public static DirectoryScanner getInstance() {
        return instance;
    }

    public void scanDirectoryRecursively(String fileType, String directoryPath, String[] patterns) throws Exception {
        Path path = Paths.get(directoryPath);
        try (Stream<Path> paths = Files.walk(path, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
            paths.filter(p -> Files.isRegularFile(p) && p.toString().endsWith("." + fileType))
                    .forEach(p -> processFile(p, patterns));
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
    private void processFile(Path file, String[] patterns) {
        /*Pattern CIPParent = Pattern.compile(patterns[0]);
        Pattern CIPChild = StringUtils.isEmpty(patterns[1]) ? null: Pattern.compile(patterns[1]);*/
        /*try {
            // Use pattern matching to count occurrences
            long occurrenceCount = Files.lines(file)
                    .flatMap(line -> pattern.matcher(line).results().stream())
                    .count();

            if (occurrenceCount > 0) {
                patternOccurrences.put(file.toString(), (int) occurrenceCount);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + file);
            e.printStackTrace();
        }*/
    }
}
