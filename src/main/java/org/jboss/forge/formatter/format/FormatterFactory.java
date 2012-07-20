package org.jboss.forge.formatter.format;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.jboss.forge.formatter.config.FormatterType;
import org.jboss.forge.resources.Resource;

public class FormatterFactory {
    
    @Inject
    @Formats(FormatterType.Java)
    private Instance<Formatter> formatter;

    public Formatter createFormatter(Resource<?> resource) {
//        FormatsLiteral formats = new FormatsLiteral(FormatterType.fromResource(resource));
        return formatter.get();
    }

    public boolean existsFor(Resource<?> resource) {
//        FormatsLiteral formats = new FormatsLiteral(FormatterType.fromResource(resource));
//        return !formatter.select(formats).isUnsatisfied();
        return !formatter.isUnsatisfied();
    }

}
