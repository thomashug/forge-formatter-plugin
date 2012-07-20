package org.jboss.forge.formatter.config;

import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;

@SuppressWarnings("rawtypes")
public enum FormatterType {

    Java(JavaResource.class);
    
    private final Class<? extends Resource> resourceType;

    private FormatterType(Class<? extends Resource> resourceType) {
        this.resourceType = resourceType;
    }
    
    public static FormatterType fromResource(Resource<?> resource) {
        Class<? extends Resource> type = resource.getClass();
        for (FormatterType formatter : FormatterType.values()) {
            if (formatter.resourceType.equals(type)) {
                return formatter;
            }
        }
        return null;
    }
    
}
