/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm.guice;

import ch.racic.testing.cm.AggregatedResourceBundle;
import ch.racic.testing.cm.ConfigEnvironment;
import ch.racic.testing.cm.ConfigProvider;
import com.google.inject.Module;
import com.google.inject.Provider;

import java.util.List;

/**
 * Created by rac on 06.04.15.
 */
public class ConfigModuleProvider implements Provider<ConfigProvider> {

    private final AggregatedResourceBundle testngParams;
    private final ConfigEnvironment env;
    private final Class testClass;
    private final ConfigProvider parent;
    private List<Module> guiceModules;

    public ConfigModuleProvider(ConfigProvider parent, ConfigEnvironment env, Class testClass, AggregatedResourceBundle testngParams) {
        this.parent = parent;
        this.env = env;
        this.testClass = testClass;
        this.testngParams = testngParams;
    }

    public ConfigModuleProvider setGuiceModules(List<Module> guiceModules) {
        this.guiceModules = guiceModules;
        return this;
    }

    public ConfigProvider get() {
        return new ConfigProvider(parent, env, testClass, testngParams).setGuiceModules(guiceModules);
    }


}
