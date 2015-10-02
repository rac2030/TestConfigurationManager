/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import ch.racic.testing.cm.guice.ConfigModuleFactory;
import com.google.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * Created by rac on 05.04.15.
 */
@Guice(moduleFactory = ConfigModuleFactory.class)
public class ArbitaryObjectTest {

    private static final Logger log = LogManager.getLogger(ArbitaryObjectTest.class);

    private static final TestObjectDummy testObject = new TestObjectDummy("dingdong");
    private static final String TEST_OBJECT_KEY = "test.object";
    private static final String TEST_OBJECT_KEY_2 = "test.object.2";

    @Inject
    ConfigProvider cfg;

    @Test
    public void setObjectTest() {
        cfg.addObj(TEST_OBJECT_KEY, testObject);
        Assert.assertEquals(cfg.getObj(TEST_OBJECT_KEY), testObject, "Should be exactly the same object");
    }

    @Test(dependsOnMethods = {"setObjectTest"})
    public void injectInObjectAndAccess() {
        TestObject to = cfg.create(TestObject.class);
        Assert.assertEquals(to.getConfigProvider().getObj(TEST_OBJECT_KEY), testObject, "Should be exactly the same object as inserted in first test");
    }

    @Test
    public void injectInObjectAndSetUber() {
        TestObject to = cfg.create(TestObject.class);
        TestObject to2 = cfg.create(TestObject.class);
        to.getConfigProvider().addUberObj(TEST_OBJECT_KEY_2, testObject);
        Assert.assertEquals(to2.getConfigProvider().getObj(TEST_OBJECT_KEY_2), testObject, "Should be exactly the same object in to2 as inserted as uber object");
        Assert.assertEquals(cfg.getObj(TEST_OBJECT_KEY_2), testObject, "Should be exactly the same object in uber cfg as inserted as uber object");
    }

    private static class TestObjectDummy {
        private final String dummyText;

        private TestObjectDummy(String dummy) {
            dummyText = dummy;
        }
    }
}
