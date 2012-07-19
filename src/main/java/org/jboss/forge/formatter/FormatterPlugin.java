package org.jboss.forge.formatter;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;
import org.jboss.forge.formatter.config.ConfigReader;
import org.jboss.forge.formatter.config.ConfigWriter;
import org.jboss.forge.formatter.config.LineEnding;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.Shell;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Command;
import org.jboss.forge.shell.plugins.DefaultCommand;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.Option;
import org.jboss.forge.shell.plugins.Plugin;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.plugins.RequiresProject;

@Alias("formatter")
@Help("Format source code")
@RequiresProject
@RequiresFacet(MavenCoreFacet.class)
public class FormatterPlugin implements Plugin {

    private LineEnding lineEnding = LineEnding.KEEP;

    @Inject
    private Shell shell;
    
    @Inject
    private ConfigReader reader;
    
    @Inject
    private ConfigWriter writer;

    @DefaultCommand
    public void format(Resource<?> file) {
        try {
            formatResource(file);
        } catch (Exception e) {
            ShellMessages.error(shell, "Could not format resource: " + e.getMessage());
        }
    }
    
    @Command(value = "setup", help = "Install a formatter file")
    public void setup(@Option(required = true) Resource<?> format) {
        try {
            writer.install(format);
        } catch (Exception e) {
            ShellMessages.error(shell, "Could not setup formatter: " + e.getMessage());
        }
    }

//    public void resourceChanged(@Observes PickupResource resource) {
//        formatResource(resource.getResource());
//    }

    private void formatResource(Resource<?> file) {
        try {
            if (file instanceof JavaResource) {
                ShellMessages.info(shell, "Formatting " + file.getFullyQualifiedName());
                String code = IOUtils.toString(file.getResourceInputStream());
                CodeFormatter formatter = createFormatter();
                TextEdit textedit = formatter.format(formatterOptions(), 
                        code, 0, code.length(), 0,
                        getLineEnding(code));
                IDocument doc = new Document(code);
                textedit.apply(doc);
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
        return reader.read();
    }
    
    private int formatterOptions() {
        return CodeFormatter.K_COMPILATION_UNIT | CodeFormatter.F_INCLUDE_COMMENTS;
    }

}
