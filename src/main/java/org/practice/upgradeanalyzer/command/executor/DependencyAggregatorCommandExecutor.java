package org.practice.upgradeanalyzer.command.executor;

import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.practice.upgradeanalyzer.command.DependencyAggregatorCommand;
import org.practice.upgradeanalyzer.csv.config.CustomColumnPositionStrategy;
import org.practice.upgradeanalyzer.csv.output.Dependency;
import org.practice.upgradeanalyzer.scanner.DirectoryScanner;
import org.practice.upgradeanalyzer.util.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DependencyAggregatorCommandExecutor extends CommandExecutor<DependencyAggregatorCommand> {
    private static final DependencyAggregatorCommandExecutor INSTANCE = new DependencyAggregatorCommandExecutor();

    private DependencyAggregatorCommandExecutor() {
    }

    public static DependencyAggregatorCommandExecutor getInstance() {
        return INSTANCE;
    }

    @Override
    protected boolean validate(DependencyAggregatorCommand command) {
        String dependencyFileName = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        if (!validateSystemProperty(command, command.getSystemPropertyKey(0)))
            return false;
        if (!validateSystemProperty(command, command.getSystemPropertyKey(1)))
            return false;
        if (!validateSystemPropertyForDir(command, command.getSystemPropertyKey(1), directoryToScan))
            return false;
        System.out.println("Dep File Path: " + dependencyFileName);
        System.out.println("Directory to Scan: " + directoryToScan);
        return true;
    }

    @Override
    public void execute(DependencyAggregatorCommand command) throws Exception {
        super.execute(command);
        Set<Dependency> dependencies = new HashSet<>();
        String dependencyFileName = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        try {
            // Traverse the directory tree and find all files named "dependencies.txt"
            DirectoryScanner directoryScanner = DirectoryScanner.getInstance();
            Consumer<Path> fileProcessor = path -> readDependenciesFromFile(path, dependencies);
            Predicate<Path> filterPredicate =
                    path -> Files.isRegularFile(path) && path.getFileName().toString().equals(dependencyFileName);
            directoryScanner.scanDirectoryRecursively(
                    directoryToScan, filterPredicate, fileProcessor);
            Writer writer = new FileWriter("Dependencies.csv");
            getBeanToCsvInstance(writer, Dependency.class).write(dependencies.stream().toList());
            writer.close();
        } catch (IOException e) {
            System.out.println("Application has caught an IOException and is going to terminate.");
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("Application has caught an Exception and is going to terminate.");
            e.printStackTrace();
            throw e;
        }
        // Print out the unique dependencies
        System.out.println("Unique Dependencies Found:");
        dependencies.forEach(System.out::println);
    }

    private void readDependenciesFromFile(Path filePath, Set<Dependency> dependencies) {
        // Get the module name (directory name containing "dependencies.txt")
        String moduleName = FileUtils.getCurrentDirectoryNameOfFile(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            boolean startReading = false;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Skip empty lines
                }

                if (line.contains("The following files have been resolved:")) {
                    startReading = true;
                    continue; // Skip the line that contains the header
                }
                if (line.toLowerCase().contains("none")) {
                    startReading = false;
                    break; // Skip the file as no valid content is present in it.
                }

                if (startReading) {
                    String[] depArr = line.trim().split(":");
                    Dependency newDependency = new Dependency(depArr[0], depArr[1], depArr[3], depArr[4]);
                    dependencies.stream()
                            .filter(dep -> dep.equals(newDependency))
                            .findFirst()
                            .ifPresentOrElse(
                                    // If the dependency exists, add the current module to its moduleList
                                    existingDependency -> existingDependency.addModule(moduleName),
                                    // If the dependency doesn't exist, add it the set
                                    () -> {
                                        newDependency.addModule(moduleName);
                                        dependencies.add(newDependency);
                                    }
                            );
                }
            }
        } catch (IOException e) {
            System.err.println("IOException reading file: " + filePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception reading file: " + filePath);
            e.printStackTrace();
        }
    }

}
