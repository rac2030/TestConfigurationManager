/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm.guice;

import com.google.inject.Module;
import org.testng.IModuleFactory;
import org.testng.ITestContext;

/**
 * Created by rac on 06.04.15.
 */
public class ConfigModuleFactory implements IModuleFactory {
    public Module createModule(ITestContext context, Class<?> testClass) {
        return new ConfigModule(context, testClass);
    }
}
