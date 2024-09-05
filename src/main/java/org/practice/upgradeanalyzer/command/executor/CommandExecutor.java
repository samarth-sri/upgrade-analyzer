package org.practice.upgradeanalyzer.command.executor;

import org.practice.upgradeanalyzer.command.Command;
import org.practice.upgradeanalyzer.command.UpgradeAnalyzerCommand;
import org.practice.upgradeanalyzer.util.FileUtils;

public abstract class CommandExecutor<T extends Command> {
    public void execute(T command) throws Exception {
        if (!validate(command)) throw new IllegalArgumentException("Input system properties supplied are invalid.");
    }

    protected abstract boolean validate(T command);

    protected void printUsage(String usageStr) {
        System.out.println("Usage:");
        System.out.println(usageStr);
    }

    protected boolean validateSystemProperty(Command command, String systemPropKey) {
        if (systemPropKey == null || systemPropKey.isEmpty()) {
            System.out.println("Error: '" + systemPropKey + "' system property is missing or empty.");
            printUsage(command.getUsage());
            return false;
        }
        return true;
    }

    protected boolean validateSystemPropertyForFile(Command command, String systemPropKey, String systemPropValue) {
        if (!FileUtils.isValidFilePath(systemPropValue)) {
            System.out.println("Error: The provided '" + systemPropKey + "' is not a valid file path: " + systemPropValue);
            printUsage(command.getUsage());
            return false;
        }
        return true;
    }

    protected boolean validateSystemPropertyForDir(Command command, String systemPropKey, String systemPropValue) {
        if (!FileUtils.isValidDirectoryPath(systemPropValue)) {
            System.out.println("Error: The provided '" + systemPropKey + "' is not a valid directory path: " + systemPropValue);
            printUsage(command.getUsage());
            return false;
        }
        return true;
    }

}
