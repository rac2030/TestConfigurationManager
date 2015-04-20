/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import ch.racic.testing.cm.annotation.ClassConfig;
import ch.racic.testing.cm.guice.ConfigModuleFactory;
import com.google.inject.Inject;
import org.apache.commons.exec.OS;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Created by rac on 05.04.15.
 */
@Guice(moduleFactory = ConfigModuleFactory.class)
@ClassConfig(fileName = "SimpleTest.properties")
public class CustomPropertiesTest {

    private static final Logger log = LogManager.getLogger(CustomPropertiesTest.class);

    @Inject
    ConfigProvider cfg;

    @Test
    @Parameters({"environment.code", "environment.locale"})
    public void simpleTest(@Optional String environmentCodeIn, @Optional String environmentLocale) {
        cfg.logAvailableProperties();
        log.debug("Config loaded from: " + cfg.get("config.test.loadedfrom"));
        String environmentCode = environmentCodeIn;
        if (environmentCode == null || environmentCode.contentEquals("envBlaBla")) environmentCode = "global";
        String environmentLocaleAppendix = "";
        if (environmentLocale != null) environmentLocaleAppendix = "_" + environmentLocale;
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), environmentCode + "/test" + environmentLocaleAppendix + ".properties", "config.test.loadedfrom gets overwritten by env folder or taken from global if no environment is specified");
        Assert.assertEquals(cfg.get("config.test.global"), "global", "config.test.global gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env"), environmentCode, "config.test.env gets overwritten by env folder or taken from global if no environment is specified");
        Assert.assertEquals(cfg.get("config.test.global.class"), "SimpleTest.global", "config.test.global.class gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env.class"), "SimpleTest." + environmentCode, "config.test.env.class gets overwritten by env folder or taken from global if no environment is specified");
        Assert.assertEquals(cfg.get("environment.code"), environmentCodeIn, "Value from config is same as value directly from parameter injected by TestNG");
    }

    @Test
    public void injectInObject() {
        TestObject to = cfg.create(TestObject.class);
        Assert.assertEquals(to.getObjectConf(), "from global", "TestObject could load its class properties from global");
    }

    @Test
    public void osSpecific() {
        // detect current OS for comparsion
        String osExpected = null;
        if (OS.isFamilyWindows()) osExpected = "Windows";
        else if (OS.isFamilyUnix()) osExpected = "Linux";
        else if (OS.isFamilyMac()) osExpected = "Mac";

        Assert.assertNotEquals(cfg.get("detected.os"), "None", "OS property has been loaded and is not default");
        Assert.assertEquals(cfg.get("detected.os"), osExpected, "OS property contains the value we expect from the current runtime system");
    }


}
