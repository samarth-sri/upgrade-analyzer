package org.practice.upgradeanalyzer.command.executor;

import org.practice.upgradeanalyzer.command.UpgradeAnalyzerCommand;
import org.practice.upgradeanalyzer.csv.MigrationChecklistCSVReader;
import org.practice.upgradeanalyzer.csv.input.MigrationChecklistItem;
import org.practice.upgradeanalyzer.scanner.DirectoryScanner;
import org.practice.upgradeanalyzer.util.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class UpgradeAnalyzerCommandExecutor extends CommandExecutor<UpgradeAnalyzerCommand> {
    private static final UpgradeAnalyzerCommandExecutor INSTANCE = new UpgradeAnalyzerCommandExecutor();
    private UpgradeAnalyzerCommandExecutor() {}

    public static UpgradeAnalyzerCommandExecutor getInstance() {return INSTANCE;}
    @Override
    public void execute(UpgradeAnalyzerCommand command) throws Exception {
        super.execute(command);
        String csvFilePath = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        MigrationChecklistCSVReader csvReader = MigrationChecklistCSVReader.getInstance();
        try {
            Optional<List<MigrationChecklistItem>> migrationChecklistOpt = csvReader.readCSV(csvFilePath);
            migrationChecklistOpt.ifPresentOrElse(
                    list -> {
                        DirectoryScanner directoryScanner = DirectoryScanner.getInstance();
                        list.forEach(listItem -> {
                            try {
                                Consumer<Path> fileProcessor = p -> processFile(p, new String[]
                                        {listItem.getCipParent(), listItem.getCipChild()});
                                Predicate<Path> filterPredicate =
                                        p -> Files.isRegularFile(p) && p.toString().endsWith("." + listItem.getTargetFileType());
                                directoryScanner.scanDirectoryRecursively(
                                        directoryToScan, filterPredicate, fileProcessor);
                            } catch (Exception e) {
                                System.out.println("---- Caught below exception for change item: " + listItem.getChangeItem());
                                System.out.println();
                                e.printStackTrace();
                            }
                        });
                    }, // call file/path scanner
                    () -> {
                        System.out.println("The migration checklist returned has no valid rows for file scanner.");
                    }); // no migration checklist returned
        } catch (Exception e) {
            System.out.println("Application has caught an exception and is going to terminate.");
            throw e;
        }
    }

    @Override
    protected boolean validate(UpgradeAnalyzerCommand command) {
        String csvFilePath = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        if (!validateSystemProperty(command, command.getSystemPropertyKey(0)))
            return false;
        if (!validateSystemProperty(command, command.getSystemPropertyKey(1)))
            return false;
        if (!validateSystemPropertyForFile(command, command.getSystemPropertyKey(0), csvFilePath))
            return false;
        if (!validateSystemPropertyForDir(command, command.getSystemPropertyKey(1), directoryToScan))
            return false;
        System.out.println("CSV File Path: " + csvFilePath);
        System.out.println("Directory to Scan: " + directoryToScan);
        return true;
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
