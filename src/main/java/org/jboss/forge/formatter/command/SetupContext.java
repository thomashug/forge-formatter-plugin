package org.jboss.forge.formatter.command;

import org.jboss.forge.formatter.config.PredefinedConfig;
import org.jboss.forge.resources.Resource;

public class SetupContext {

    private final Resource<?> configFile;
    private final PredefinedConfig configName;
    private final boolean enableAutoFormat;
    private final boolean skipComments;

    public SetupContext(Resource<?> configFile, PredefinedConfig configName, boolean enableAutoFormat, boolean skipComments) {
        this.configFile = configFile;
        this.configName = configName;
        this.enableAutoFormat = enableAutoFormat;
        this.skipComments = skipComments;
    }
    
    public boolean isValidCombination() {
        boolean formatSet = configFile != null;
        boolean configSet = configName != null;
        return formatSet ^ configSet;
    }

    public Resource<?> getConfigFile() {
        return configFile;
    }

    public PredefinedConfig getConfigName() {
        return configName;
    }

    public boolean isEnableAutoFormat() {
        return enableAutoFormat;
    }

    public boolean isSkipComments() {
        return skipComments;
    }
}
