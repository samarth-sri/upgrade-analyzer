package org.practice.upgradeanalyzer.csv;

import com.opencsv.bean.CsvToBeanBuilder;
import org.practice.upgradeanalyzer.csv.config.InputBeanVerifier;
import org.practice.upgradeanalyzer.csv.input.MigrationChecklistItem;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class MigrationChecklistCSVReader {
    private MigrationChecklistCSVReader() {
    }
    private static final MigrationChecklistCSVReader instance = new MigrationChecklistCSVReader();

    public static MigrationChecklistCSVReader getInstance() {
        return instance;
    }

    public Optional<List<MigrationChecklistItem>> readCSV(String csvFilePath) throws Exception {
        List<MigrationChecklistItem> migrationCheckList;
        try(FileReader reader = new FileReader(csvFilePath,  StandardCharsets.UTF_8)) {
            migrationCheckList = new CsvToBeanBuilder<MigrationChecklistItem>(reader)
                    .withType(MigrationChecklistItem.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(InputBeanVerifier.getInstance())
                    .build().parse();
            migrationCheckList.stream().forEach(i -> System.out.println(i));
        } catch (IOException e) {
            System.out.println("---- Caught IOException ----");
            System.out.println();
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.out.println("---- Caught Exception ----");
            System.out.println();
            e.printStackTrace();
            throw e;
        }
        return Optional.ofNullable(migrationCheckList);
    }

}
