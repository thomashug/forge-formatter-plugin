package org.jboss.forge.formatter.config;

import javax.inject.Inject;

import org.jboss.forge.parser.xml.Node;
import org.jboss.forge.parser.xml.XMLParser;
import org.jboss.forge.project.Project;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;

public class ConfigWriter extends BaseConfig {
    
    @Inject
    private Project project;

    public void install(Resource<?> formatter) {
        FileResource<?> forgeXml = resolveForgeXml(project, true);
        Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
        Node form = lookupFormatter(forge, FormatterType.Java, true);
        String rootPath = project.getProjectRoot().getFullyQualifiedName();
        String formatterPath = formatter.getFullyQualifiedName();
        if (formatterPath.startsWith(rootPath)) {
            formatterPath = formatterPath.substring(rootPath.length() + 1);
        }
        form.text(formatterPath);
        forgeXml.setContents(XMLParser.toXMLInputStream(forge));
    }

}
