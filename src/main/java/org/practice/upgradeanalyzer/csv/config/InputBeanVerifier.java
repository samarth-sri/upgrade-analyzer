package org.practice.upgradeanalyzer.csv.config;

import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;
import org.practice.upgradeanalyzer.csv.input.MigrationChecklistItem;

public class InputBeanVerifier implements BeanVerifier<MigrationChecklistItem> {

    private InputBeanVerifier() {
    }

    public static final InputBeanVerifier instance = new InputBeanVerifier();

    public static InputBeanVerifier getInstance() {
        return instance;
    }

    // Verify/filter only mandatory java file csv objects
    @Override
    public boolean verifyBean(MigrationChecklistItem item) throws CsvConstraintViolationException {
        return item.getChangeType().contains("API")
                && item.getIsMandatory().equalsIgnoreCase("yes")
                && item.getTargetFileType().equalsIgnoreCase("java")
                && !item.getCipParent().isBlank();
    }
}
