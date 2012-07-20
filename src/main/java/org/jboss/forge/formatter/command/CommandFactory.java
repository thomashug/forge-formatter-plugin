package org.jboss.forge.formatter.command;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public class CommandFactory {

    @Inject
    private Instance<FormatCommand> format;
    
    @Inject
    private Instance<SetupCommand> setup;
    
    public FormatCommand format() {
        return format.get();
    }
    
    public SetupCommand setup() {
        return setup.get();
    }

}
