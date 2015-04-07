/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import ch.racic.testing.cm.annotation.ClassConfig;
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
@ClassConfig(SimpleTest.class)
public class InjectTest {

    private static final Logger log = LogManager.getLogger(InjectTest.class);

    @Inject
    ConfigProvider cfg;

    @Test
    public void simpleTest() {
        cfg.logAvailableProperties();
        log.debug("Config loaded from: " + cfg.get("config.test.loadedfrom"));
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), "env1/test_de.properties", "config.test.loadedfrom gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global"), "global", "config.test.global gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env"), "env1", "config.test.env gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global.class"), "SimpleTest.global", "config.test.global.class gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env.class"), "SimpleTest.env1", "config.test.env.class gets overwritten by env folder");

    }

    @Test
    public void injectInObject() {
        TestObject to = cfg.create(TestObject.class);
        Assert.assertEquals(to.getObjectConf(), "from global", "TestObject could load its class properties from global");
    }


}
