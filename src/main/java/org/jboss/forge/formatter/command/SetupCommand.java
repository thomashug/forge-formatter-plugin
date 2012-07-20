package org.jboss.forge.formatter.command;

import javax.inject.Inject;

import org.jboss.forge.formatter.config.ConfigWriter;


public class SetupCommand {
    
    @Inject
    private ConfigWriter writer;

    public void setupFormatting(SetupContext context) {
        writer.installFormatter(context.getConfigFile(), context.getConfigName())
              .installAutoFormat(context.isEnableAutoFormat())
              .installSkipComments(context.isSkipComments());
    }
}
