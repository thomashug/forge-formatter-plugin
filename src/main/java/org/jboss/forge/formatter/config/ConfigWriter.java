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

    public void install(Resource<?> formatter, PredefinedConfig configName, boolean enableAutoFormat) {
        FileResource<?> forgeXml = resolveForgeXml(project, true);
        Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
        installFormatterType(formatter, configName, forge);
        installAutoFormat(enableAutoFormat, forge);
        forgeXml.setContents(XMLParser.toXMLInputStream(forge));
    }

    private void installFormatterType(Resource<?> formatter, PredefinedConfig configName, Node forge) {
        Node formatterType = lookupFormatter(forge, FormatterType.Java, true);
        if (formatter != null) {
            String rootPath = project.getProjectRoot().getFullyQualifiedName();
            String formatterPath = formatter.getFullyQualifiedName();
            if (formatterPath.startsWith(rootPath)) {
                formatterPath = formatterPath.substring(rootPath.length() + 1);
            }
            formatterType.text(formatterPath);
        } else {
            formatterType.text(configName.name());
        }
    }
    
    private void installAutoFormat(boolean enableAutoFormat, Node forge) {
        Node formatter = lookupFormatter(forge, true);
        formatter.createChild(AUTOFORMAT_TAG).text(String.valueOf(enableAutoFormat));
    }

}
