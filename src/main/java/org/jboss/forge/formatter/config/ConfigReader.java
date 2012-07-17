package org.jboss.forge.formatter.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.jboss.forge.parser.xml.Node;
import org.jboss.forge.parser.xml.XMLParser;

public class ConfigReader {

    public Map<String, String> readPredefined(PredefinedConfig configName) {
        try {
            InputStream stream = getClass().getClassLoader().getResourceAsStream("predefined-formatters.xml");
            Map<String, String> result = read(stream, configName.name());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Failed reading config" + configName, e);
        }
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
            }
        }
        return result;
    }
}
