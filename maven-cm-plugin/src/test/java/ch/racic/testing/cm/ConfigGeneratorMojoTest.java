package ch.racic.testing.cm;

import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.resources.TestResources;
import org.junit.Rule;
import org.junit.Test;

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

    @Rule
    public TestResources resources = new TestResources();


    @Test
    public void testSomething()
            throws Exception {
        /*File pom = rule.getTestFile("src/test/resources/unit/project-to-test/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        MyMojo myMojo = (MyMojo) rule.lookupMojo("touch", pom);
        assertNotNull(myMojo);
        myMojo.execute();

        ...*/
        //TODO How to execute the mojo and check if all files are generated correctly?
    }
}