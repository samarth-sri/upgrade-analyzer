package org.practice.upgradeanalyzer.csv.output;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dependency {
    @CsvBindByName(column = "GroupId")
    @CsvBindByPosition(position = 0)
    private String groupId;

    @CsvBindByName(column = "ArtifactId")
    @CsvBindByPosition(position = 1)
    private String artifactId;

    @CsvBindByName(column = "Version")
    @CsvBindByPosition(position = 2)
    private String version;

    @CsvBindByName(column = "Scope")
    @CsvBindByPosition(position = 3)
    private String scope;

    @CsvBindByName(column = "Modules")
    @CsvBindByPosition(position = 4)
    private String modules;

    private List<String> moduleList;

    public Dependency(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
        moduleList = new ArrayList<>();
    }

    public void addModule(String moduleName) {
        moduleList.add(moduleName);
        this.modules = String.join(" | ", moduleList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dependency that)) return false;
        return groupId.equals(that.groupId) && artifactId.equals(that.artifactId) && version.equals(that.version) && scope.equals(that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, version, scope);
    }

    @Override
    public String toString() {
        return "Dependency{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                ", scope='" + scope + '\'' +
                ", modules='" + modules + '\'' +
                '}';
    }

}
