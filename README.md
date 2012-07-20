# Forge Formatter Plugin

[![Build Status](https://buildhive.cloudbees.com/job/thomashug/job/forge-formatter-plugin/badge/icon)](https://buildhive.cloudbees.com/job/thomashug/job/forge-formatter-plugin/)

As much as [Forge](http://forge.github.com) is a great tool - the code it generates is 
usually not in your preferred code styling. This plugin is intended to help you getting 
source code nice and readable after generation, in the way you want it.

The formatter plugin currently supports only Java source formatting. Other file formats might
be added in a later version.

## Installation

The formatter plugin is currently installable from this github repository. Start Forge
and run

    forge git-plugin git://github.com/thomashug/forge-formatter-plugin.git
    
## Usage

Once you have the plugin installed you can simply execute

    formatter ${path_to_file}.java
    
This will format your Java file based on the Sun coding conventions.

### Formatting Options

The Forge formatter plugin supports several predefined formatter settings. To
use one of them, execute the formatter command with the following option:

    formatter ${path_to_file}.java --configName [Sun|Eclipse|JBoss]

Using this option each time you want to format a file is quite inconvenient. You can configure
your project to use a specific formatter profile every time you format a file with the
following setup command:

    formatter setup --configName [Sun|Eclipse|JBoss]

### Custom Formatter Settings

You can also use customer formatter settings by exporting a configuration from Eclipse.
Save the XML under your project and use the following command to set it as default for
your project:

    formatter setup ${path_to_config}.xml
    
All following formattings will use the custom formatter config file.

## Sample

Create a new project:

    [no project] projects $ new-project --named test
    
Create an Eclipse formatting file with your preferred coding style and copy it 
into your project root (or similar location):

    [test] test $ ls
    eclipse-formatter.xml    pom.xml                  src/                     
    [test] test $ formatter setup eclipse-formatter.xml
    Wrote test/src/main/resources/META-INF/forge.xml

Your formatter settings get stored in the forge.xml config file.
Create now e.g. a JPA entity and use the formatter:

    [test] test $ persistence setup --provider HIBERNATE --container JBOSS_AS7
    ...
    [test] test $ entity --named Test --package org.jboss.forge.model

Check the generated code:

    [test] Test.java $ ls .
    
And now format it:

    [test] Test.java $ formatter .

This should match now with the formatting conditions you've put into the 
Eclipse formatter file.
