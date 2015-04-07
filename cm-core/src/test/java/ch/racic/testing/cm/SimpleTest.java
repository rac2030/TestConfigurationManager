/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by rac on 05.04.15.
 */
public class SimpleTest {

    private static final Logger log = LogManager.getLogger(SimpleTest.class);

    ConfigProvider cfg;
    ConfigEnvironment env;

    @BeforeClass
    public void beforeClass(ITestContext iTestContext) throws IOException {
        log.entry(iTestContext);
        env = new ConfigEnvironment("Test environment 1", "Just for testing the config provider", "env1");
        cfg = new ConfigProvider(env, this.getClass());
    }


    @Test
    public void simpleTest() {
        cfg.logAvailableProperties();
        log.debug("Config loaded from: " + cfg.get("config.test.loadedfrom"));
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), "env1/test.properties", "config.test.loadedfrom gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global"), "global", "config.test.global gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env"), "env1", "config.test.env gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global.class"), "SimpleTest.global", "config.test.global.class gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env.class"), "SimpleTest.env1", "config.test.env.class gets overwritten by env folder");

    }

}
