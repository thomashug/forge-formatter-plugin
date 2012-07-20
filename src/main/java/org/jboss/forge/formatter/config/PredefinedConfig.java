package org.jboss.forge.formatter.config;

public enum PredefinedConfig {

    Sun, Eclipse, JBoss;
    
    public static boolean exists(String name) {
        for (PredefinedConfig conf : PredefinedConfig.values()) {
            if (conf.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
}
