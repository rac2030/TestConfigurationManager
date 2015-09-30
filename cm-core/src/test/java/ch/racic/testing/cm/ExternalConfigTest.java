/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import ch.racic.testing.cm.annotation.ClassConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by rac on 30.09.15.
 */
@ClassConfig(SimpleTest.class)
public class ExternalConfigTest {

    private static final Logger log = LogManager.getLogger(ExternalConfigTest.class);

    ConfigProvider cfg;
    ConfigEnvironment env;

    @BeforeClass
    public void beforeClass(ITestContext iTestContext) throws IOException {
        log.entry(iTestContext);
        System.setProperty(ConfigProvider.CONFIG_BASE_FOLDER_SYSTEM_KEY, "externalconfig");
        cfg = new ConfigProvider(null, this.getClass());
        System.clearProperty(ConfigProvider.CONFIG_BASE_FOLDER_SYSTEM_KEY);
    }


    @Test
    public void simpleTest() {
        cfg.logAvailableProperties();
        log.debug("Config loaded from: " + cfg.get("config.test.loadedfrom"));
        Assert.assertEquals(cfg.CONFIG_BASE_FOLDER, "externalconfig", "base folder is set to the system property value");
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), "externalconfig/global/test.properties", "config.test.loadedfrom gets loaded from externalconfig/global");

    }

}
