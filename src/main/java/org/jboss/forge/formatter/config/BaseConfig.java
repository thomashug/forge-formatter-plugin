package org.jboss.forge.formatter.config;

import org.jboss.forge.parser.xml.Node;
import org.jboss.forge.parser.xml.XMLParser;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.ResourceFacet;
import org.jboss.forge.resources.FileResource;

public abstract class BaseConfig {

    static final String FORMATTER_TAG = "formatter";
    static final String AUTOFORMAT_TAG = "autoFormatEnabled";
    static final String SKIP_COMMENTS_TAG = "skipComments";

    FileResource<?> resolveForgeXml(Project project, boolean createIfMissing) {
        FileResource<?> forgeXml = (FileResource<?>) project.getProjectRoot().getChild(
                "src/main/resources/META-INF/forge.xml");
        if (forgeXml.exists()) {
            return forgeXml;
        }
        if (createIfMissing) {
            return project.getFacet(ResourceFacet.class)
                    .createResource("<forge/>".toCharArray(), "META-INF/forge.xml");
        }
        return forgeXml;
    }
    
    Node lookupFormatter(FileResource<?> forgeXml, boolean createIfMissing) {
        if (forgeXml.exists()) {
            Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
            return lookupFormatter(forge, createIfMissing);
        }
        return null;
    }
    
    Node lookupFormatter(FileResource<?> forgeXml, FormatterType type, boolean createIfMissing) {
        if (forgeXml.exists()) {
            Node forge = XMLParser.parse(forgeXml.getResourceInputStream());
            return lookupFormatter(forge, type, createIfMissing);
        }
        return null;
    }
    
    Node lookupFormatter(Node document, boolean createIfMissing) {
        Node formatter = document.getSingle(FORMATTER_TAG);
        if (formatter == null && createIfMissing) {
            formatter = document.createChild(FORMATTER_TAG);
        }
        return formatter;
    }
    
    Node lookupFormatter(Node document, FormatterType type, boolean createIfMissing) {
        Node formatter = lookupFormatter(document, createIfMissing);
        if (formatter == null) {
            return null;
        }
        String typeName = type.name().toLowerCase();
        Node formatterType = formatter.getSingle(typeName);
        if (formatterType == null) {
            formatterType = formatter.createChild(typeName);
        }
        return formatterType;
    }
}
