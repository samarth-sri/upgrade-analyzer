package org.practice.upgradeanalyzer.command.executor;

import org.apache.commons.lang3.StringUtils;
import org.practice.upgradeanalyzer.command.CodeScannerCommand;
import org.practice.upgradeanalyzer.csv.MigrationChecklistCSVReader;
import org.practice.upgradeanalyzer.csv.input.MigrationChecklistItem;
import org.practice.upgradeanalyzer.csv.output.MigrationTargetInventoryItem;
import org.practice.upgradeanalyzer.scanner.DirectoryScanner;
import org.practice.upgradeanalyzer.util.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeScannerCommandExecutor extends CommandExecutor<CodeScannerCommand> {
    private static final CodeScannerCommandExecutor INSTANCE = new CodeScannerCommandExecutor();

    private CodeScannerCommandExecutor() {
    }

    public static CodeScannerCommandExecutor getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(CodeScannerCommand command) throws Exception {
        super.execute(command);
        String csvFilePath = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        MigrationChecklistCSVReader csvReader = MigrationChecklistCSVReader.getInstance();
        try {
            Optional<List<MigrationChecklistItem>> migrationChecklistOpt = csvReader.readCSV(csvFilePath);
            migrationChecklistOpt.ifPresentOrElse(list -> {
                        DirectoryScanner directoryScanner = DirectoryScanner.getInstance();
                        List<MigrationTargetInventoryItem> targetInventory = new ArrayList<>();
                        list.forEach(checklistItem -> {
                            MigrationTargetInventoryItem inventoryItem = new MigrationTargetInventoryItem(checklistItem.getChangeItem());
                            try {
                                Predicate<Path> filterPredicate = p -> Files.isRegularFile(p) && p.toString().endsWith("." + checklistItem.getTargetFileType());
                                Consumer<Path> fileProcessor = p -> processFile(p, new String[]{checklistItem.getCipParent(), checklistItem.getCipChild(), checklistItem.getCipChildSibling()}, inventoryItem, directoryToScan);
                                directoryScanner.scanDirectoryRecursively(directoryToScan, filterPredicate, fileProcessor);
                                targetInventory.add(inventoryItem);
                            } catch (Exception e) {
                                System.out.println("---- Caught below exception for change item: " + checklistItem.getChangeItem());
                                System.out.println();
                                e.printStackTrace();
                            }
                        });
                        try {
                            Writer writer = new FileWriter("TargetInventory.csv");
                            getBeanToCsvInstance(writer, MigrationTargetInventoryItem.class).write(targetInventory);
                            writer.close();
                        } catch (Exception e) {
                            System.out.println("Caught Exception during writing of TargetInventory.csv");
                            e.printStackTrace();
                        }
                    }, // call file/path scanner
                    () -> {
                        System.out.println("The migration checklist returned has no valid rows for file scanner.");
                    }); // no migration checklist returned
        } catch (Exception e) {
            System.out.println("Application has caught an exception and is going to terminate.");
            throw e;
        }
    }

    private void processFile(Path filePath, String[] patterns, MigrationTargetInventoryItem inventoryItem, String dirToScan) {
        Pattern cipParent = Pattern.compile(patterns[0]);
        Pattern cipChild = StringUtils.isEmpty(patterns[1]) ? null : Pattern.compile(patterns[1]);
        Pattern cipChildSibling = StringUtils.isEmpty(patterns[2]) ? null : Pattern.compile(patterns[2]);
        long cipParentCount = 0;
        long cipChildCount = 0;
        long cipChildSiblingCount = 0;
        try {
            // Read the entire file content as a stream of lines
            String content = Files.readString(filePath);
            // Check for parent pattern occurrences and count
            Matcher matcherParent = cipParent.matcher(content);
            Matcher matcherChild = cipChild == null ? null : cipChild.matcher(content);
            Matcher matcherChildSibling = cipChildSibling == null ? null : cipChildSibling.matcher(content);
            while (matcherParent.find()) {
                cipParentCount++;
            }
            if (matcherChild != null) {
                while (matcherChild.find()) {
                    cipChildCount++;
                }
                if (matcherChildSibling != null) {
                    while (matcherChildSibling.find()) {
                        cipChildSiblingCount++;
                    }
                }
            }

            if (cipParentCount > 0) {
                // only 1 pattern needs to be searched and recorded
                if (cipChild == null) {
                    // child pattern is null proceed with parent only
                    inventoryItem.addTargetFileWithOccurrence(FileUtils.getRelativePath(dirToScan, filePath), cipParentCount);
                } else {
                    if (cipChildCount > 0) {
                        // if child sibling is not present, both parent and child must have non-zero counts
                        if (cipChildSibling == null) {
                            inventoryItem.addTargetFileWithOccurrence(FileUtils.getRelativePath(dirToScan, filePath), cipChildCount);
                        } else {
                            if (cipChildSiblingCount > 0) {
                                // all three present
                                inventoryItem.addTargetFileWithOccurrence(FileUtils.getRelativePath(dirToScan, filePath), cipChildSiblingCount);
                            } else {
                                // child sibling pattern yields 0 results
                                System.out.println("Skipping for ChangeItem: " + inventoryItem.getChangeItem() + " for " + filePath + " as CIP Child Sibling count is 0");
                            }
                        }
                    } else {
                        System.out.println("Skipping for ChangeItem: " + inventoryItem.getChangeItem() + " for " + filePath + " as CIP Child count is 0");
                    }
                }
            } else {
                System.out.println("Skipping for ChangeItem: " + inventoryItem.getChangeItem() + " for " + filePath + " as CIP Parent count is 0");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filePath);
            e.printStackTrace();
        }
    }

    @Override
    protected boolean validate(CodeScannerCommand command) {
        String csvFilePath = command.getSystemPropertyValue(command.getSystemPropertyKey(0));
        String directoryToScan = command.getSystemPropertyValue(command.getSystemPropertyKey(1));
        if (!validateSystemProperty(command, command.getSystemPropertyKey(0))) return false;
        if (!validateSystemProperty(command, command.getSystemPropertyKey(1))) return false;
        if (!validateSystemPropertyForFile(command, command.getSystemPropertyKey(0), csvFilePath)) return false;
        if (!validateSystemPropertyForDir(command, command.getSystemPropertyKey(1), directoryToScan)) return false;
        System.out.println("CSV File Path: " + csvFilePath);
        System.out.println("Directory to Scan: " + directoryToScan);
        return true;
    }

}
