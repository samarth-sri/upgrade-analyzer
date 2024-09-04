package org.practice.upgradeanalyzer.csv.output;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MigrationTargetInventoryItem {

    private String changeItem;

    private List<String> targetFilesWithOccurrenceCounts = new ArrayList<>();

    public String getChangeItem() {
        return changeItem;
    }


    public List<String> getTargetFilesWithOccurrenceCounts() {
        return targetFilesWithOccurrenceCounts;
    }

    public void setTargetFilesWithOccurrenceCounts(List<String> targetFilesWithOccurrenceCounts) {
        this.targetFilesWithOccurrenceCounts = targetFilesWithOccurrenceCounts;
    }

    @Override
    public String toString() {
        return "MigrationTargetInventoryItem{" +
                "changeItem='" + changeItem + '\'' +
                ", targetFilesWithOccurrenceCounts=" + targetFilesWithOccurrenceCounts +
                '}';
    }

    private String convertFileOccurrencesToString(List<TargetFileWithOccurrence> fileWithOccurrences) {
        if (fileWithOccurrences == null || fileWithOccurrences.isEmpty()) {
            return "";
        }

        return fileWithOccurrences.stream()
                .map(TargetFileWithOccurrence::toString)
                .collect(Collectors.joining("| "));
    }

    public static class TargetFileWithOccurrence {
        private String relativePathOfFile;

        private int occurrenceCountInFile;

        public TargetFileWithOccurrence(String relativePathOfFile, int occurrenceCountInFile) {
            this.relativePathOfFile = relativePathOfFile;
            this.occurrenceCountInFile = occurrenceCountInFile;
        }

        public String getRelativePathOfFile() {
            return relativePathOfFile;
        }

        public int getOccurrenceCountInFile() {
            return occurrenceCountInFile;
        }

        @Override
        public String toString() {
            return "[" + relativePathOfFile + " (" + occurrenceCountInFile + ")]";
        }
    }
}
