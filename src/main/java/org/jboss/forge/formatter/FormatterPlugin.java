package org.jboss.forge.formatter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.formatter.config.ConfigReader;
import org.jboss.forge.formatter.config.ConfigReader.PredefinedConfig;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresProject;

@Alias("formatter")
@Help("Format source code")
@RequiresProject
public class FormatterPlugin implements Plugin {

    private LineEnding lineEnding = LineEnding.KEEP;

    @Inject
    private Shell shell;
    
    @Inject
    private ConfigReader reader;

    @DefaultCommand
    public void format(Resource<?> file) {
        formatResource(file);
    }

//    public void resourceChanged(@Observes PickupResource resource) {
//        formatResource(resource.getResource());
//    }

    private void formatResource(Resource<?> file) {
        try {
            if (file instanceof JavaResource) {
                shell.print("Formatting " + file.getFullyQualifiedName());
                String code = IOUtils.toString(file.getResourceInputStream());
                CodeFormatter formatter = createFormatter();
                TextEdit te = formatter.format(CodeFormatter.K_COMPILATION_UNIT, code, 0, code.length(), 0,
                        getLineEnding(code));

                IDocument doc = new Document(code);
                te.apply(doc);
                String formattedCode = doc.get();
                ((JavaResource) file).setContents(formattedCode);
            }
        } catch (Exception e) {
            ShellMessages.error(shell, "Failed to format code: " + e.getMessage());
        }
    }

    private String getLineEnding(String fileDataString) {
        return lineEnding.lineEndings(fileDataString);
    }

    private CodeFormatter createFormatter() {
        return ToolFactory.createCodeFormatter(getFormattingOptions());
    }

    private Map<String, String> getFormattingOptions() {
        Map<String, String> options = new HashMap<String, String>();
        options = reader.readPredefined(PredefinedConfig.Sun);
        
        // TODO: Extract from Project...
        options.put(JavaCore.COMPILER_SOURCE, "1.6");
        options.put(JavaCore.COMPILER_COMPLIANCE, "1.6");
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, "1.6");
        return options;
    }

}
