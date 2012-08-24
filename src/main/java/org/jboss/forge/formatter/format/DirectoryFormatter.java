package org.jboss.forge.formatter.format;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.formatter.config.FormatterType;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFilter;
import org.jboss.forge.resources.ResourceFlag;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;

@Formats(FormatterType.Directory)
public class DirectoryFormatter implements Formatter {
    
    @Inject
    private FormatterFactory factory;
    
    @Inject
    private Shell shell;

    @Override
    public void format(Resource<?> resource, FormatContext context) {
        List<Resource<?>> files = prepareResource(resource, context.isRecursive());
        for (Resource<?> file : files) {
            try {
                factory.createFormatter(file).format(file, context);
            } catch (Exception e) {
                ShellMessages.error(shell, "Failed to format code: " + e.getMessage());
            }
        }
    }
    
    private List<Resource<?>> prepareResource(Resource<?> resource, final boolean recursive) {
        List<Resource<?>> files = new LinkedList<Resource<?>>();
        if (resource.isFlagSet(ResourceFlag.Node)) {
            files.addAll(resource.listResources(new ResourceFilter() {
                @Override
                public boolean accept(Resource<?> resource) {
                    return factory.existsFor(resource) && (!recursive ? !(resource instanceof DirectoryResource) : true);
                }
            }));
        } else {
            files.add(resource);
        }
        return files;
    }

}
