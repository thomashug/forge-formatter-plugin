package org.jboss.forge.formatter.format;

import javax.enterprise.util.AnnotationLiteral;

import org.jboss.forge.formatter.config.FormatterType;

@SuppressWarnings("all")
public class FormatsLiteral extends AnnotationLiteral<Formats> implements Formats {

    private static final long serialVersionUID = 1L;
    
    private final FormatterType resourceType;
    
    public FormatsLiteral(FormatterType resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public FormatterType value() {
        return resourceType;
    }

}
