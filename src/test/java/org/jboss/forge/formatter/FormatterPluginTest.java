package org.jboss.forge.formatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
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
    public void testFormat() throws Exception {
        initializeJavaProject();
        getShell().execute("formatter " + javaSource.getAbsolutePath());
        System.out.println(IOUtils.toString(new FileInputStream(javaSource)));
    }

    @Before
    public void setup() throws IOException {
        javaSource = new File("./target/TestClass.java");
        File testFile = new File("src/test/java/test/TestClass.java");
        if (javaSource.exists()) {
            FileUtils.deleteQuietly(javaSource);
        }
        FileUtils.copyFile(testFile, javaSource);
    }
}
