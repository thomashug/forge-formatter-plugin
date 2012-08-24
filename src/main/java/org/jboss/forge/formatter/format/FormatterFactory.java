package org.jboss.forge.formatter.format;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.forge.formatter.config.FormatterType;
import org.jboss.forge.resources.Resource;

public class FormatterFactory {
    
    @Inject @Any
    private Instance<Formatter> formatter;

    public Formatter createFormatter(Resource<?> resource) {
        FormatsLiteral formats = new FormatsLiteral(FormatterType.fromResource(resource));
        return formatter.select(formats).get();
    }

    public boolean existsFor(Resource<?> resource) {
        FormatterType type = FormatterType.fromResource(resource);
        if (type == null) {
            return false;
        }
        FormatsLiteral formats = new FormatsLiteral(type);
        return !formatter.select(formats).isUnsatisfied();
    }

}
