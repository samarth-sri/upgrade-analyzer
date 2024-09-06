package org.practice.upgradeanalyzer;

import org.apache.commons.lang3.StringUtils;
import org.practice.upgradeanalyzer.command.Command;
import org.practice.upgradeanalyzer.command.DependencyAggregatorCommand;
import org.practice.upgradeanalyzer.command.executor.CommandExecutor;
import org.practice.upgradeanalyzer.command.CodeScannerCommand;

public class UpgradeAnalyzerMain {

    public static void main(String[] args) {
        String commandName = System.getProperty("command");
        if (StringUtils.isEmpty(commandName)) {
            System.out.println("Missing required system property: " + "'command'");
            System.exit(1);
        }
        Command command = getCommand(commandName);
        CommandExecutor<Command> commandExecutor = command.getExecutor();
        try {
            commandExecutor.execute(command);
        } catch (Exception e) {
            System.out.println("Caught Exception while executing command: " + commandName);
            System.out.println();
            e.printStackTrace();
        }
    }

    private static Command getCommand(String commandName) {
        switch (commandName) {
            case "CodeScanner":
                return new CodeScannerCommand();
            case "DependencyAggregator":
                return new DependencyAggregatorCommand();
            default:
                return null;
        }
    }

}