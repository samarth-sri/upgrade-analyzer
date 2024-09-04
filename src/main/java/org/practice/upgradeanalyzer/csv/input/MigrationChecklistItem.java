package org.practice.upgradeanalyzer.csv.input;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;

import java.util.Objects;

public class MigrationChecklistItem {

    @CsvBindByName(column = "ChangeItem", required = true)
    private String changeItem;

    @CsvBindByName(column = "ChangeType", required = true)
    private String changeType;

    @CsvBindByName(column = "TargetVersion", required = true)
    private String targetVersion;

    @CsvIgnore
    @CsvBindByName(column = "SuggestedFix")
    private String suggestedFix;

    @CsvBindByName(column = "TargetFileType")
    private String targetFileType;

    @CsvBindByName(column = "IsMandatory", required = true)
    private String isMandatory;

    @CsvBindByName(column = "ChangeIdentificationPattern-Parent")
    private String cipParent;

    @CsvBindByName(column = "ChangeIdentificationPattern-Child")
    private String cipChild;

    @CsvIgnore
    @CsvBindByName(column = "OpenRewriteRecipe")
    private String openRewriteRecipe;

    public String getChangeItem() {
        return changeItem;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getTargetVersion() {
        return targetVersion;
    }

    public String getSuggestedFix() {
        return suggestedFix;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public String getCipParent() {
        return cipParent;
    }

    public String getCipChild() {
        return cipChild;
    }

    public String getTargetFileType() {
        return targetFileType;
    }
    public String getOpenRewriteRecipe() {
        return openRewriteRecipe;
    }

    public void setChangeItem(String changeItem) {
        this.changeItem = changeItem;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public void setTargetVersion(String targetVersion) {
        this.targetVersion = targetVersion;
    }

    public void setSuggestedFix(String suggestedFix) {
        this.suggestedFix = suggestedFix;
    }

    public void setTargetFileType(String targetFileType) {
        this.targetFileType = targetFileType;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public void setCipParent(String cipParent) {
        this.cipParent = cipParent;
    }

    public void setCipChild(String cipChild) {
        this.cipChild = cipChild;
    }

    public void setOpenRewriteRecipe(String openRewriteRecipe) {
        this.openRewriteRecipe = openRewriteRecipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MigrationChecklistItem that)) return false;
        return changeItem.equals(that.changeItem) && changeType.equals(that.changeType) && targetVersion.equals(that.targetVersion) && targetFileType.equals(that.targetFileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(changeItem, changeType, targetVersion, targetFileType);
    }

    @Override
    public String toString() {
        return "MigrationChecklistItem{" +
                "changeItem='" + changeItem + '\'' +
                ", changeType='" + changeType + '\'' +
                ", targetVersion='" + targetVersion + '\'' +
                ", suggestedFix='" + suggestedFix + '\'' +
                ", targetFileType='" + targetFileType + '\'' +
                ", isMandatory='" + isMandatory + '\'' +
                ", cipParent='" + cipParent + '\'' +
                ", cipChild='" + cipChild + '\'' +
                ", openRewriteRecipe='" + openRewriteRecipe + '\'' +
                '}';
    }
}
