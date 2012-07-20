package org.jboss.forge.formatter.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.maven.model.Plugin;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.jdt.core.JavaCore;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.parser.xml.Node;
import org.jboss.forge.parser.xml.XMLParser;
import org.jboss.forge.project.Project;
import org.jboss.forge.resources.FileResource;

public class ConfigReader extends BaseConfig {
    
    private static final String PREDEFINED_FORMATTERS = "predefined-formatters.xml";
    
    @Inject
    private Project project;
    
    public boolean isAutoFormatEnabled() {
        Node formatter = lookupFormatter(resolveForgeXml(project, false), false);
        if (formatter != null) {
            Node auto = formatter.getSingle(AUTOFORMAT_TAG);
            return auto != null ? Boolean.valueOf(auto.getText()) : false;
        }
        return false;
    }
    
    public boolean isSkipComments() {
        Node formatter = lookupFormatter(resolveForgeXml(project, false), false);
        if (formatter != null) {
            Node auto = formatter.getSingle(SKIP_COMMENTS_TAG);
            return auto != null ? Boolean.valueOf(auto.getText()) : false;
        }
        return false;
    }
    
    public Map<String, String> read() {
        try {
            FileResource<?> forgeXml = resolveForgeXml(project, false);
            Node formatter = lookupFormatter(forgeXml, FormatterType.Java, false);
            if (formatter != null) {
                String configName = formatter.getText();
                if (PredefinedConfig.exists(configName)) {
                    return readPredefined(PredefinedConfig.valueOf(configName));
                } else {
                    FileResource<?> configFile = (FileResource<?>) project.getProjectRoot().getChild(configName);
                    return read(configFile.getResourceInputStream());
                }
            }
            return readPredefined(PredefinedConfig.Sun);
        } catch (Exception e) {
            throw new RuntimeException("Failed reading config", e);
        }
    }

    public Map<String, String> readPredefined(PredefinedConfig configName) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(PREDEFINED_FORMATTERS);
            Map<String, String> result = read(stream, configName.name());
            return updateWithSettings(result);
        } catch (Exception e) {
            throw new RuntimeException("Failed reading config " + configName, e);
        }
    }
    
    private Map<String, String> updateWithSettings(Map<String, String> settings) {
        MavenCoreFacet maven = project.getFacet(MavenCoreFacet.class);
        for (Plugin plugin : maven.getMavenProject().getBuild().getPlugins()) {
            Xpp3Dom config = (Xpp3Dom) plugin.getConfiguration();
            Xpp3Dom source = config.getChild("source");
            if (source != null) {
                settings.put(JavaCore.COMPILER_SOURCE, source.getValue());
            }
            Xpp3Dom target = config.getChild("target");
            if (target != null) {
                settings.put(JavaCore.COMPILER_COMPLIANCE, target.getName());
                settings.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, target.getName());
            }
        }
        return settings;
    }
    
    private Map<String, String> read(InputStream in) throws Exception {
        return read(in, null);
    }
    
    private Map<String, String> read(InputStream in, String name) throws Exception {
        Map<String, String> result = new HashMap<String, String>();        
        Node node = XMLParser.parse(in);
        for (Node profile : node.get("profile")) {
            if (profile.getAttribute("name").equals(name) || name == null) {
                for (Node setting : profile.get("setting")) {
                    String key = setting.getAttribute("id");
                    String value = setting.getAttribute("value");
                    result.put(key, value);
                }
                break;
            }
        }
        return result;
    }

}
