package org.practice.upgradeanalyzer.command;

import org.practice.upgradeanalyzer.command.executor.CommandExecutor;
import org.practice.upgradeanalyzer.command.executor.UpgradeAnalyzerCommandExecutor;

public class UpgradeAnalyzerCommand extends Command {
    @Override
    protected void initializeSystemProperties() {
        addSystemProperty("csvPath");
        addSystemProperty("dirToScan");
    }

    @Override
    public CommandExecutor getExecutor() {
        initializeSystemProperties();
        return UpgradeAnalyzerCommandExecutor.getInstance();
    }

    @Override
    public String getUsage() {
        return "java -jar .\\target\\upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -Dcommand=UpgradeAnalyzer -DcsvPath=<csv_file_path> -DdirToScan=<root_dir> UpgradeAnalyzerMain";
    }
}
