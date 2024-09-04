package org.practice.upgradeanalyzer.command.executor;

import org.practice.upgradeanalyzer.command.Command;

public abstract class CommandExecutor<T extends Command> {
    public void execute(T command) throws Exception {
        if (!validate(command)) throw new IllegalArgumentException("Input system properties supplied are invalid.");
    }

    protected abstract boolean validate(T command);

    protected void printUsage(String usageStr) {
        System.out.println("Usage:");
        System.out.println(usageStr);
    }

}
