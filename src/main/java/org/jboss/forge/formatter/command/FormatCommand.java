package org.jboss.forge.formatter.command;

import javax.inject.Inject;

import org.jboss.forge.formatter.format.FormatContext;
import org.jboss.forge.formatter.format.FormatterFactory;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;

public class FormatCommand {

    @Inject
    private Shell shell;
    
    @Inject
    private FormatterFactory factory;

    public void format(Resource<?> resource, FormatContext context) {
        if (factory.existsFor(resource)) {
            factory.createFormatter(resource).format(resource, context);
        } else {
            ShellMessages.warn(shell, "No formatter for resource " + resource + ", skipping");
        }
    }

}
