package org.jboss.forge.formatter.format;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.formatter.config.FormatterType;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;

@Formats(FormatterType.Java)
public class JavaFormatter implements Formatter {
    
    @Inject
    private Shell shell;

    @Override
    public void format(Resource<?> resource, FormatContext context) {
        try {
            ShellMessages.info(shell, "Formatting " + resource.getFullyQualifiedName());
            String code = IOUtils.toString(resource.getResourceInputStream());
            CodeFormatter formatter = createFormatter(context);
            TextEdit textedit = formatter.format(formatterOptions(context), code, 0, 
                    code.length(), 0, getLineEnding(context, code));
            IDocument doc = new Document(code);
            textedit.apply(doc);
            String formattedCode = doc.get();
            ((JavaResource) resource).setContents(formattedCode);
        } catch (Exception e) {
            ShellMessages.error(shell, "Failed to format code: " + e.getMessage());
        }
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
