package org.practice.upgradeanalyzer.command;

import java.util.List;
import org.practice.upgradeanalyzer.command.executor.CommandExecutor;
import org.practice.upgradeanalyzer.command.executor.CodeScannerCommandExecutor;

public class CodeScannerCommand extends Command {
    @Override
    protected void initializeSystemProperties() {
        addSystemProperty("csvPath");
        addSystemProperty("dirToScan");
    }

    @Override
    public CommandExecutor getExecutor() {
        initializeSystemProperties();
        return CodeScannerCommandExecutor.getInstance();
    }

    @Override
    public String getUsage() {
        return "java -jar .\\target\\upgrade-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar -Dcommand=CodeScanner -DcsvPath=<csv_file_path> -DdirToScan=<root_dir> UpgradeAnalyzerMain";
    }
}
