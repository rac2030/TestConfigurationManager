/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm.guice;

import ch.racic.testing.cm.ConfigEnvironment;
import ch.racic.testing.cm.ConfigProvider;
import com.google.inject.AbstractModule;
import org.testng.ITestContext;
import org.testng.xml.XmlTest;

import java.util.Locale;

/**
 * Created by rac on 06.04.15.
 */
public class ConfigModule extends AbstractModule {

    private ConfigEnvironment env;
    private Class testClass;

    public static final String ENVIRONMENT_NAME = "environment.name";
    public static final String ENVIRONMENT_DESCRIPTION = "environment.description";
    public static final String ENVIRONMENT_CODE = "environment.code";
    public static final String ENVIRONMENT_LOCALE = "environment.locale";

    /**
     * Constructor for the guice module to be used outside of TestNG
     *
     * @param env
     * @param testClass
     */
    public ConfigModule(ConfigEnvironment env, Class<?> testClass) {
        this.env = env;
        this.testClass = testClass;
    }

    /**
     * Constructor to be used from the @see ConfigModuleFactory for TestNG
     *
     * @param context
     * @param testClass
     */
    public ConfigModule(ITestContext context, Class<?> testClass) {
        XmlTest test = context.getCurrentXmlTest();
        String envName = test.getParameter(ENVIRONMENT_NAME);
        String envDesc = test.getParameter(ENVIRONMENT_DESCRIPTION);
        String envCode = test.getParameter(ENVIRONMENT_CODE);
        String envLocale = test.getParameter(ENVIRONMENT_LOCALE);
        Locale locale = Locale.forLanguageTag(envLocale);
        if (envCode != null) {
            env = new ConfigEnvironment(envName, envDesc, envCode, locale);
        }

        this.testClass = testClass;
    }

    @Override
    protected void configure() {
        bind(ConfigProvider.class).toProvider(new ConfigModuleProvider(env, testClass));
    }
}
