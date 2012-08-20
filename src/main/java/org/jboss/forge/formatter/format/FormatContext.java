package org.jboss.forge.formatter.format;

import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.formatter.config.ConfigReader;
import org.jboss.forge.formatter.config.LineEnding;
import org.jboss.forge.formatter.config.PredefinedConfig;

public class FormatContext {

    @Inject
    private ConfigReader reader;

    private PredefinedConfig configName;
    private Boolean skipComments;
    private LineEnding lineEnding = LineEnding.AUTO;

    private boolean recursive;

    public boolean isAutoformatEnabled() {
        return reader.isAutoFormatEnabled();
    }

    public boolean isSkipComments() {
        return skipComments == null ? reader.isSkipComments() : skipComments.booleanValue();
    }

    public Map<String, String> getFormattingOptions() {
        return configName == null ? reader.read() : reader.readPredefined(configName);
    }

    public FormatContext withConfigName(PredefinedConfig configName) {
        this.configName = configName;
        return this;
    }

    public FormatContext withLineEnding(LineEnding lineEnding) {
        this.lineEnding = lineEnding;
        return this;
    }

    public FormatContext skipComments(Boolean skipComments) {
        this.skipComments = skipComments;
        return this;
    }

    public PredefinedConfig getConfigName() {
        return configName;
    }

    public LineEnding getLineEnding() {
        return lineEnding;
    }

    public void recursive(boolean recursive) {
        this.recursive = recursive;
    }

    public boolean isRecursive() {
        return recursive;
    }

}
