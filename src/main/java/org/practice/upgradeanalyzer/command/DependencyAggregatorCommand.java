package org.practice.upgradeanalyzer.command;

import org.practice.upgradeanalyzer.command.executor.CommandExecutor;
import org.practice.upgradeanalyzer.command.executor.DependencyAggregatorCommandExecutor;

public class DependencyAggregatorCommand extends Command {
    @Override
    protected void initializeSystemProperties() {
        addSystemProperty("depFileName");
        addSystemProperty("dirToScan");
    }

    @Override
    public CommandExecutor getExecutor() {
        initializeSystemProperties();
        return DependencyAggregatorCommandExecutor.getInstance();
    }

    @Override
    public String getUsage() {
        return "java -jar .\\target\\upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -Dcommand=DependencyAggregator -DdepFileName=<dependencies_file_name> -DdirToScan=<root_dir> UpgradeAnalyzerMain";
    }
}
