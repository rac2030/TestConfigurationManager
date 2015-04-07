/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm.guice;

import ch.racic.testing.cm.ConfigEnvironment;
import ch.racic.testing.cm.ConfigProvider;
import com.google.inject.Provider;

/**
 * Created by rac on 06.04.15.
 */
public class ConfigModuleProvider implements Provider<ConfigProvider> {

    private ConfigEnvironment env;
    private Class testClass;

    public ConfigModuleProvider(ConfigEnvironment env, Class testClass) {
        this.env = env;
        this.testClass = testClass;
    }

    public ConfigProvider get() {
        return new ConfigProvider(env, testClass);
    }


}
