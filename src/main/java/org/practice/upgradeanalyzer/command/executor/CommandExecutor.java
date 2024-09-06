package org.practice.upgradeanalyzer.command.executor;

import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.practice.upgradeanalyzer.command.Command;
import org.practice.upgradeanalyzer.csv.config.CustomColumnPositionStrategy;
import org.practice.upgradeanalyzer.csv.output.Dependency;
import org.practice.upgradeanalyzer.util.FileUtils;

import java.io.Writer;

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
        //if (systemPropKey == null || systemPropKey.isEmpty()) {
        if(!System.getProperties().keySet().contains(systemPropKey)) {
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

    protected <T> StatefulBeanToCsv getBeanToCsvInstance(Writer writer, Class<T> clazz) {
        MappingStrategy<T> strategy = new CustomColumnPositionStrategy();
        strategy.setType(clazz);
        StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(strategy).build();
        return beanToCsv;
    }

}
