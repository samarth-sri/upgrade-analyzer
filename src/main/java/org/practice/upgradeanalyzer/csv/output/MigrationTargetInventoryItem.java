package org.practice.upgradeanalyzer.csv.output;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvIgnore;

import java.util.ArrayList;
import java.util.List;

public class MigrationTargetInventoryItem {

    @CsvBindByName(column = "ChangeItem")
    @CsvBindByPosition(position = 0)
    private String changeItem;

    @CsvBindByName(column = "TargetFilesWithOccurrenceCounts")
    @CsvBindByPosition(position = 1)
    private String targetFilesWithOccurrenceCountsStr;

    @CsvIgnore
    private List<String> targetFilesWithOccurrenceCounts;

    public String getChangeItem() {
        return changeItem;
    }

    public MigrationTargetInventoryItem(String changeItem) {
        this.changeItem = changeItem;
        this.targetFilesWithOccurrenceCounts = new ArrayList<>();
    }

    public void addTargetFileWithOccurrence(String relativePathOfFile, String occurrenceCountInFile) {
        this.targetFilesWithOccurrenceCounts.add(relativePathOfFile + " (" + occurrenceCountInFile + ")");
        this.targetFilesWithOccurrenceCountsStr = String.join(" | ", targetFilesWithOccurrenceCounts);
    }

    @Override
    public String toString() {
        return "MigrationTargetInventoryItem{" +
                "changeItem='" + changeItem + '\'' +
                ", targetFilesWithOccurrenceCountsStr=" + targetFilesWithOccurrenceCountsStr +
                '}';
    }
}
