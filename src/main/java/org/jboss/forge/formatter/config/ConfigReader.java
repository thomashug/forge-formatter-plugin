package org.jboss.forge.formatter.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigReader {

    public static enum PredefinedConfig {
        Sun, Eclipse
    }
    
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
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
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(in);
        NodeList profiles = doc.getDocumentElement().getElementsByTagName("profile");
        for (int i = 0; i < profiles.getLength(); i++) {
            Node profile = profiles.item(i);
            Node named = profile.getAttributes().getNamedItem("name");
            if (named.getTextContent().equals(name) || name == null) {
                NodeList settings = ((Element) profile).getElementsByTagName("setting");
                for (int j = 0; j < settings.getLength(); j++) {
                    Node setting = settings.item(j);
                    String key = setting.getAttributes().getNamedItem("id").getTextContent();
                    String value = setting.getAttributes().getNamedItem("value").getTextContent();
                    result.put(key, value);
                }
                break;
            }
        }
        return result;
    }
}
