package org.practice.upgradeanalyzer.command;

import org.practice.upgradeanalyzer.command.executor.CommandExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    private List<String> systemProperties = new ArrayList<>();

    public boolean addSystemProperty(String prop) {
        return systemProperties.add(prop);
    }

    public String getSystemPropertyKey(int keyIndex) {
        return systemProperties.get(keyIndex);
    }

    public String getSystemPropertyValue(String key) {
        return System.getProperty(key);
    }

    protected abstract void initializeSystemProperties();

    public abstract CommandExecutor getExecutor();

    public abstract String getUsage();
}
