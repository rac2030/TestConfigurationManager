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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by pravin on 15.09.16.
 */

public class SystemPropertyTest {

    private static final Logger log = LogManager.getLogger(SystemPropertyTest.class);
    public static final String CHECK_SYS_PROP = "check.SysProp";
    public static final String MY_CUSTOM_PROP = "myCustomProp";

    ConfigProvider cfg;
    ConfigEnvironment env;

    @BeforeClass
    public void beforeClass(ITestContext iTestContext) throws IOException {
        log.entry(iTestContext);
        System.setProperty(CHECK_SYS_PROP, MY_CUSTOM_PROP);
        cfg = new ConfigProvider(null, this.getClass());
    }

    @AfterClass
    public void tearDown() {
        System.clearProperty(CHECK_SYS_PROP);
    }


    @Test
    public void systemPropertyTest() {
    	
        cfg.logAvailableProperties();
        Assert.assertEquals(cfg.get(CHECK_SYS_PROP), MY_CUSTOM_PROP, "check.SysProp gets loaded from System Properties");

    }

}
