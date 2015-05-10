package ch.racic.testing.cm;

import org.junit.Rule;
import org.junit.Test;

import java.io.File;

public class ConfigGeneratorMojoTest {

    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() throws Throwable {
        }

        @Override
        protected void after() {
        }
    };

    @Test
    public void testSomething()
            throws Exception {
        File pom = rule.getTestFile("src/test/resources/unit/project-to-test/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        MyMojo myMojo = (MyMojo) rule.lookupMojo("touch", pom);
        assertNotNull(myMojo);
        myMojo.execute();

        ...
    }
}