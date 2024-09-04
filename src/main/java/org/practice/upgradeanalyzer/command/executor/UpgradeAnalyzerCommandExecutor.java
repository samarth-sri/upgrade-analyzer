package org.practice.upgradeanalyzer.command.executor;

import org.practice.upgradeanalyzer.command.UpgradeAnalyzerCommand;
import org.practice.upgradeanalyzer.csv.MigrationChecklistCSVReader;
import org.practice.upgradeanalyzer.csv.input.MigrationChecklistItem;
import org.practice.upgradeanalyzer.scanner.DirectoryScanner;
import org.practice.upgradeanalyzer.util.FileUtils;

import java.util.List;
import java.util.Optional;

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
                                directoryScanner.scanDirectoryRecursively(
                                        listItem.getTargetFileType(), directoryToScan, new String[]
                                                {listItem.getCipParent(), listItem.getCipChild()});
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
        String csvFilePath = System.getProperty(command.getSystemPropertyKey(0));
        String directoryToScan = System.getProperty(command.getSystemPropertyKey(1));
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            System.out.println("Error: 'csvFilePath' system property is missing or empty.");
            printUsage(command.getUsage());
            return false;
        }
        if (directoryToScan == null || directoryToScan.isEmpty()) {
            System.out.println("Error: 'dirToScan' system property is missing or empty.");
            printUsage(command.getUsage());
            return false;
        }
        // Validate the CSV file path
        if (!FileUtils.isValidFilePath(csvFilePath)) {
            System.out.println("Error: The provided 'csvPath' is not a valid file path: " + csvFilePath);
            printUsage(command.getUsage());
            return false;
        }
        // Validate the directory path
        if (!FileUtils.isValidDirectoryPath(directoryToScan)) {
            System.out.println("Error: The provided 'dirToScan' is not a valid directory path: " + directoryToScan);
            printUsage(command.getUsage());
            return false;
        }
        System.out.println("CSV File Path: " + csvFilePath);
        System.out.println("Directory to Scan: " + directoryToScan);
        return true;
    }

}
