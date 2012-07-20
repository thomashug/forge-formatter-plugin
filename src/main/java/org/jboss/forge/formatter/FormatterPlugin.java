package org.jboss.forge.formatter;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.New;
import javax.inject.Inject;

import org.jboss.forge.formatter.command.CommandFactory;
import org.jboss.forge.formatter.command.SetupContext;
import org.jboss.forge.formatter.config.PredefinedConfig;
import org.jboss.forge.formatter.format.FormatContext;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.events.ResourceCreated;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.PipeOut;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;

@Alias("formatter")
@Help("Format source code")
@RequiresProject
@RequiresFacet(MavenCoreFacet.class)
public class FormatterPlugin implements Plugin {

    @Inject
    private Shell shell;

    @Inject
    private CommandFactory commands;
    
    @Inject @New
    private FormatContext context;
    
    @DefaultCommand(help = "Format a source file")
    public void format(
            @Option(required = true) Resource<?> file,
            @Option(name = "configName") PredefinedConfig configName,
            @Option(name = "skipComments", flagOnly = true) boolean skipComments,
            PipeOut out) {
        try {
            context.withConfigName(configName)
                   .skipComments(skipComments ? Boolean.TRUE : null);
            commands.format().format(file, context);
        } catch (Exception e) {
            ShellMessages.error(shell, "Could not format resource: " + e.getMessage());
        }
    }

    @Command(value = "setup", help = "Install a formatter file")
    public void setup(@Option(help = "Formatter file") Resource<?> format,
            @Option(name = "configName") PredefinedConfig configName,
            @Option(name = "enableAutoFormat", flagOnly = true) boolean enableAutoFormat,
            @Option(name = "skipComments", flagOnly = true) boolean skipComments,
            PipeOut out) {
        try {
            SetupContext context = new SetupContext(format, configName, enableAutoFormat, skipComments);
            if (!context.isValidCombination()) {
                ShellMessages.error(shell, "Either set a config file or a predefined config.");
            } else {
                commands.setup().setupFormatting(context);
            }
        } catch (Exception e) {
            ShellMessages.error(shell, "Could not setup formatter: " + e.getMessage());
        }
    }

    void resourceCreated(@Observes ResourceCreated resource) {
        if (context.isAutoformatEnabled()) {
            commands.format().format(resource.getResource(), context);
        }
    }


}
