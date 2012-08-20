package org.jboss.forge.formatter.format;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.formatter.config.FormatterType;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFilter;
import org.jboss.forge.resources.ResourceFlag;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;

@Formats(FormatterType.Java)
public class JavaFormatter implements Formatter {

    @Inject
    private Shell shell;

    @Override
    public void format(Resource<?> resource, FormatContext context) {
        List<Resource<?>> files = prepareResource(resource, context.isRecursive());
        for (Resource<?> file : files) {
            try {
                ShellMessages.info(shell, "Formatting " + file.getFullyQualifiedName());
                String code = IOUtils.toString(file.getResourceInputStream());
                CodeFormatter formatter = createFormatter(context);
                TextEdit textedit = formatter.format(formatterOptions(context), code, 0, code.length(), 0,
                        getLineEnding(context, code));
                IDocument doc = new Document(code);
                textedit.apply(doc);
                String formattedCode = doc.get();
                ((JavaResource) file).setContents(formattedCode);
            } catch (Exception e) {
                ShellMessages.error(shell, "Failed to format code: " + e.getMessage());
            }
        }
    }

    private List<Resource<?>> prepareResource(Resource<?> resource, boolean recursive) {
        List<Resource<?>> files = new LinkedList<Resource<?>>();
        if (resource.isFlagSet(ResourceFlag.Node)) {
            files.addAll(resource.listResources(new ResourceFilter() {
                @Override
                public boolean accept(Resource<?> resource) {
                    return resource.getName().endsWith(".java");
                }
            }));
            if (recursive) {
                List<Resource<?>> childFolders = resource.listResources(new ResourceFilter() {
                    @Override
                    public boolean accept(Resource<?> resource) {
                        return resource.isFlagSet(ResourceFlag.Node);
                    }
                });
                for (Resource<?> childFolder : childFolders) {
                    files.addAll(prepareResource(childFolder, true));
                }
            }
        } else {
            files.add(resource);
        }
        return files;
    }

    private String getLineEnding(FormatContext context, String fileDataString) {
        return context.getLineEnding().lineEndings(fileDataString);
    }

    private CodeFormatter createFormatter(FormatContext context) {
        return ToolFactory.createCodeFormatter(context.getFormattingOptions());
    }

    private int formatterOptions(FormatContext context) {
        if (context.isSkipComments()) {
            return CodeFormatter.K_COMPILATION_UNIT;
        } else {
            return CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS;
        }
    }

}
