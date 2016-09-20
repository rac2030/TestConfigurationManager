/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import ch.racic.testing.cm.guice.ConfigModuleFactory;

/**
 * Created by pravin on 15.09.16.
 */

public class SystemPropertyTest {

    private static final Logger log = LogManager.getLogger(SystemPropertyTest.class);

    ConfigProvider cfg;
    ConfigEnvironment env;

    @BeforeClass
    public void beforeClass(ITestContext iTestContext) throws IOException {
        log.entry(iTestContext);
        System.setProperty("check.SysProp", "myCustomProp");
        cfg = new ConfigProvider(null, this.getClass());        
    }


    @Test
    public void systemPropertyTest() {
    	
        cfg.logAvailableProperties();                       
        Assert.assertEquals(cfg.get("check.SysProp"), "myCustomProp", "check.SysProp gets loaded from System Properties");

    }

}
