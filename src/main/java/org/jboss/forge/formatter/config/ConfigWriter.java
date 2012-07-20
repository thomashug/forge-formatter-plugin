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

    public ConfigWriter installFormatter(Resource<?> formatter, PredefinedConfig configName) {
        FileResource<?> forgeXml = resolveForgeXml(project, true);
        Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
        installFormatterType(formatter, configName, forge);
        forgeXml.setContents(XMLParser.toXMLInputStream(forge));
        return this;
    }
    
    public ConfigWriter installAutoFormat(boolean enableAutoFormat) {
        FileResource<?> forgeXml = resolveForgeXml(project, true);
        Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
        installFlag(AUTOFORMAT_TAG, enableAutoFormat, forge);
        forgeXml.setContents(XMLParser.toXMLInputStream(forge));
        return this;
    }
    
    public ConfigWriter installSkipComments(boolean skipComments) {
        FileResource<?> forgeXml = resolveForgeXml(project, true);
        Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
        installFlag(SKIP_COMMENTS_TAG, skipComments, forge);
        forgeXml.setContents(XMLParser.toXMLInputStream(forge));
        return this;
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
    
    private void installFlag(String tag, boolean flag, Node forge) {
        Node formatter = lookupFormatter(forge, true);
        formatter.createChild(tag).text(String.valueOf(flag));
    }

}
