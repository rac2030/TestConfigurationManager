package ch.racic.testing.cm;


import ch.racic.testing.cm.annotation.ClassConfig;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Properties;

@ClassConfig(fileName = "testCmConfigInsideJar")
public class InsideJarPropertiesTest {

    @Test
    @Parameters({"environment.code", "environment.locale" })
    public void checkRuntimeProperties(@Optional String environmentCodeIn, @Optional String environmentLocale) throws Exception {
        Properties props = new Properties();
        props.put(ConfigProvider.CONFIG_BASE_FOLDER_SYSTEM_KEY, "customConfigFolder");

        Locale locale = environmentLocale != null ? Locale.forLanguageTag(environmentLocale) : null;
        ConfigEnvironment env = environmentCodeIn != null ? new ConfigEnvironment(null, null, environmentCodeIn, locale) : null;
        ConfigProvider cfg = new ConfigProvider(null, env, this.getClass(), null, props);
        Assert.assertEquals(cfg.get("config.insideJar.myvalue.global.class.only"), "global.class", "Property has been read successfully from global class config properties inside a jar file");

        String environmentCode = environmentCodeIn;
        if (environmentCode == null || environmentCode.contentEquals("envBlaBla")) environmentCode = "global";
        String environmentLocaleAppendix = "";
        if (environmentLocale != null) environmentLocaleAppendix = "_" + environmentLocale;

        if ("env1".equals(environmentCode)) {
            Assert.assertEquals(cfg.get("config.insideJar.myvalue.env1.class.only"), "env1.class", "Property has been read successfully from env class config properties inside a jar file");
        }
        Assert.assertEquals(cfg.get("config.test.loadedfrom"), environmentCode + "/insideJarTest" + environmentLocaleAppendix + ".properties", "config.test.loaded.from gets overwritten by env folder or taken from global if no environment is specified");
    }
}