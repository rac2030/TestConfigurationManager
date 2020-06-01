/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rac on 05.04.15.
 */
public class SimpleTest {

    private static final Logger log = LogManager.getLogger(SimpleTest.class);

    public ConfigProvider getCfg() throws IOException {
        ConfigEnvironment env = new ConfigEnvironment("Test environment 1", "Just for testing the config provider", "env1");
        return new ConfigProvider(env, this.getClass());
    }

    @Test
    public void simpleTest() throws Exception {
        ConfigProvider cfg = getCfg();
        log.debug("Config loaded from: " + cfg.get("config.test.loadedfrom"));
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), "env1/test.properties", "config.test.loadedfrom gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global"), "global", "config.test.global gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env"), "env1", "config.test.env gets overwritten by env folder");
        Assert.assertEquals(cfg.get("config.test.global.class"), "SimpleTest.global", "config.test.global.class gets not overwritten");
        Assert.assertEquals(cfg.get("config.test.env.class"), "SimpleTest.env1", "config.test.env.class gets overwritten by env folder");
    }

    @Test
    public void readOnDemandTest() throws Exception {
        ConfigProvider cfg = getCfg();
        Assert.assertEquals(cfg.getOptional("runtime.loaded"), null, "property should not yet be available");
        cfg.loadCustomClassProperties("OnDemandTest");
        Assert.assertEquals(cfg.getOptional("runtime.loaded"), "env1/class/OnDemandTest.properties", "property should become available");
    }

    @Test
    public void copyConstructorTest() throws Exception {
        ConfigProvider cfg = getCfg();
        Assert.assertEquals(cfg.getOptional("runtime.loaded"), null, "property should not yet be available");
        ConfigProvider cfgCopy = new ConfigProvider(cfg);
        cfgCopy.loadCustomClassProperties("OnDemandTest");
        Assert.assertEquals(cfgCopy.getOptional("runtime.loaded"), "env1/class/OnDemandTest.properties", "property should become available in copied config");
        Assert.assertEquals(cfg.getOptional("runtime.loaded"), null, "property should remain unavailable in original config");
    }

    @Test
    public void prefixTest() throws Exception {
        ConfigProvider cfg = getCfg();
        // Expected properties in SimpleTest.properties
        Map<String, String> expectedFull = new HashMap<String, String>();
        expectedFull.put("co.vid.test1", "test1");
        expectedFull.put("co.vid.test2", "test2");
        expectedFull.put("co.vid.test3", "test3");
        Map<String, String> expectedTrim = new HashMap<String, String>();
        expectedTrim.put("test1", "test1");
        expectedTrim.put("test2", "test2");
        expectedTrim.put("test3", "test3");

        //Get List from prefix
        Map<String, String> actualFull = cfg.getStringWithPrefix("co.vid.");
        Assert.assertEquals(actualFull, expectedFull, "List with prefix");

        //Get List from prefix and trim it away
        Map<String, String> actualTrim = cfg.getStringWithPrefixTrim("co.vid.");
        Assert.assertEquals(actualTrim, expectedTrim, "List with prefix trimmed away");

        //Get List from prefix and trim it away
        Map<String, String> actualEmptyPrefix = cfg.getStringWithPrefix("");
        // As system variables can be different, we check only if the ones from the properties are there
        Assert.assertEquals(actualEmptyPrefix.get("config.test.global.class"), "SimpleTest.global", "Property as expected");
        Assert.assertEquals(actualEmptyPrefix.get("config.test.env.class"), "SimpleTest.env1", "Property as expected");
        Assert.assertEquals(actualEmptyPrefix.get("custom.variable.for.SimpleTest"), "dingdong", "Property as expected");
        Assert.assertEquals(actualEmptyPrefix.get("co.vid.test1"), "test1", "Property as expected");
        Assert.assertEquals(actualEmptyPrefix.get("co.vid.test2"), "test2", "Property as expected");
        Assert.assertEquals(actualEmptyPrefix.get("co.vid.test3"), "test3", "Property as expected");
    }
}
