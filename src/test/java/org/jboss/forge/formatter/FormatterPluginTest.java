package org.jboss.forge.formatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.forge.project.Project;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;

public class FormatterPluginTest extends AbstractShellTest {

    private File javaSource;

    @Deployment
    public static JavaArchive getDeployment() {
        return AbstractShellTest.getDeployment().addPackages(true, FormatterPlugin.class.getPackage());
    }
    
    @Test
    public void should_use_predefined_formatter() throws Exception {
        // given
        initializeJavaProject();
        
        // when
        getShell().execute("formatter " + javaSource.getAbsolutePath());
        
        // then
        compare("src/test/resources/reference/TestClass_sun.java");
    }
    
    @Test
    public void should_use_customer_formatter() throws Exception {
        // given
        Project p = initializeJavaProject();
        File formatter = new File("src/test/resources/files/eclipse-formatter.xml");
        File target = new File(p.getProjectRoot().getFullyQualifiedName() + "/eclipse-formatter.xml");
        FileUtils.copyFile(formatter, target);
        
        // when
        getShell().execute("formatter setup eclipse-formatter.xml");
        getShell().execute("formatter " + javaSource.getAbsolutePath());
        
        // then
        compare("src/test/resources/reference/TestClass_eclipse.java");
    }

    @Before
    public void setup() throws IOException {
        javaSource = new File("./target/TestClass.java");
        File testFile = new File("src/test/resources/classes/TestClass.java");
        if (javaSource.exists()) {
            FileUtils.deleteQuietly(javaSource);
        }
        FileUtils.copyFile(testFile, javaSource);
    }
    
    void compare(String referenceName) throws IOException {
        String content = IOUtils.toString(new FileInputStream(javaSource));
        String reference = IOUtils.toString(new FileInputStream(new File(referenceName)));
        System.out.println("Reference: " + referenceName);
        System.out.println(content);
        Assert.assertEquals(reference, content);
    }
}
