package org.jboss.forge.formatter.format;

import org.jboss.forge.resources.Resource;

public interface Formatter {

    void format(Resource<?> resource, FormatContext context);

}
