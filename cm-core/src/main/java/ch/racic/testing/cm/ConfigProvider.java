/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import ch.racic.testing.cm.annotation.ClassConfig;
import ch.racic.testing.cm.guice.ConfigModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.commons.exec.OS;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This ConfigProvider handles configuration management tailored for usage in TestNG. It maintains several layers of
 * properties which can override the previous layer.
 */
public class ConfigProvider {

    private static final Logger log = LogManager.getLogger(ConfigProvider.class);

    private ConfigProvider parentConfig;

    private ConfigEnvironment environment;
    private String clazz;

    private List<Module> guiceModules;

    public static final String CONFIG_BASE_FOLDER = "config";
    public static final String CONFIG_GLOBAL_BASE_FOLDER = "global";
    public static final String CONFIG_CLASS_FOLDER = "class";
    public static final String CONFIG_OS_FOLDER = "os";

    // External config keys
    public static final String CONFIG_BASE_FOLDER_SYSTEM_KEY = "configBaseDirectory";

    private AggregatedResourceBundle propsGlobal, propsEnv, propsGlobalClass, propsEnvClass, propsTestNG, propsCustomClass, propsOS;
    // Store arbitary objects to be passed on for injection
    private Map<String, Object> obj;

    private Properties systemProperties;

    private PropertyFileResolver propertyFileResolver;
    private String configBaseFolder, configGlobalBaseFolder, configEnvironmentBaseFolder;

    public static FilenameFilter propertiesFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            if (
            /** Filter locale bundles as we will construct the ResourceBundle with the default file **/
                    name.matches("^([a-zA-Z0-9.-]*\\.properties)$")
                    ) return true;
            else return false;
        }
    };

    public ConfigProvider(ConfigEnvironment environment, Class clazz) {
        this(null, environment, clazz, null);
    }

    public ConfigProvider(ConfigProvider parent, ConfigEnvironment environment, Class clazz, AggregatedResourceBundle testngParams) {
        this(parent, environment, clazz, testngParams, System.getProperties());
    }

    //copy constructor
    public ConfigProvider(ConfigProvider toCopy) {
        this.guiceModules = toCopy.guiceModules;
        this.parentConfig = toCopy.parentConfig;
        this.environment = toCopy.environment;
        this.clazz = toCopy.clazz;
        this.propsTestNG = toCopy.propsTestNG != null ? new AggregatedResourceBundle(toCopy.propsTestNG) : null;
        this.propsOS = toCopy.propsOS != null ? new AggregatedResourceBundle(toCopy.propsOS) : null;
        this.propsCustomClass = toCopy.propsCustomClass != null ? new AggregatedResourceBundle(toCopy.propsCustomClass) : null;
        this.propsEnvClass = toCopy.propsEnvClass != null ? new AggregatedResourceBundle(toCopy.propsEnvClass) : null;
        this.propsGlobalClass = toCopy.propsGlobalClass != null ? new AggregatedResourceBundle(toCopy.propsGlobalClass) : null;
        this.propsEnv = toCopy.propsEnv != null ? new AggregatedResourceBundle(toCopy.propsEnv) : null;
        this.propsGlobal = toCopy.propsGlobal != null ? new AggregatedResourceBundle(toCopy.propsGlobal) : null;
        this.systemProperties = toCopy.systemProperties;
        this.propertyFileResolver = toCopy.propertyFileResolver;
        this.configBaseFolder = toCopy.configBaseFolder;
        this.configGlobalBaseFolder = toCopy.configGlobalBaseFolder;
        this.configEnvironmentBaseFolder = toCopy.configEnvironmentBaseFolder;
        this.obj = toCopy.obj;
    }

    public ConfigProvider(ConfigProvider parent, ConfigEnvironment environment, Class clazz, AggregatedResourceBundle testngParams, Properties systemProperties) {
        this.systemProperties = systemProperties;
        this.parentConfig = parent;
        this.environment = environment;

        this.configBaseFolder = systemProperties.getProperty(CONFIG_BASE_FOLDER_SYSTEM_KEY, CONFIG_BASE_FOLDER);

        // Extract class name for loading if annotation present
        ClassConfig classConfig = (ClassConfig) clazz.getAnnotation(ClassConfig.class);
        if (classConfig != null && classConfig.value().length != 0) this.clazz = classConfig.value()[0].getSimpleName();
        else if (classConfig != null && !classConfig.fileName().contentEquals(""))
            this.clazz = classConfig.fileName().replace(".properties", "");
        else this.clazz = clazz.getSimpleName();
        loadProperties();
        propsTestNG = testngParams;
    }

    private void loadProperties() {
        try {
            this.propertyFileResolver = new PropertyFileResolver(configBaseFolder);
        } catch (RuntimeException e) {
            log.error("Configuration initialization error. Config folder not existing or not a directory", e);
            throw e;
        }
        this.configGlobalBaseFolder = CONFIG_GLOBAL_BASE_FOLDER;
        if (propertyFileResolver.resourceExists(configGlobalBaseFolder)) {
            propsGlobal = new AggregatedResourceBundle();
            for (String propertyFileName : propertyFileResolver.findAllBasePropertyFilenames(configGlobalBaseFolder)) {
                mergeOverride(configBaseFolder + "." + CONFIG_GLOBAL_BASE_FOLDER + "." + propertyFileName, environment, propsGlobal);
            }
        } else {
            log.warn("global folder not existing, can't load default values");
        }

        this.configEnvironmentBaseFolder = (environment != null) ? environment.getCode() : null;
        // Load environment base properties
        if (environment != null && !propertyFileResolver.resourceExists(configEnvironmentBaseFolder)) {
            log.error("Configuration initialisation error", new IOException("Environment specific configuration folder does not exist for " + environment));
        } else {
            propsEnv = new AggregatedResourceBundle();
            for (String propertyFileName : propertyFileResolver.findAllBasePropertyFilenames(configEnvironmentBaseFolder)) {
                mergeOverride(configBaseFolder + "." + environment.getCode() + "." + propertyFileName, environment, propsEnv);
            }
        }

        // Load global class properties
        if (configGlobalBaseFolder != null && propertyFileResolver.findAllBasePropertyFilenames(configGlobalBaseFolder + "/" + CONFIG_CLASS_FOLDER).contains(clazz)) {
            propsGlobalClass = new AggregatedResourceBundle();
            mergeOverride(configBaseFolder + "." + CONFIG_GLOBAL_BASE_FOLDER + "." + CONFIG_CLASS_FOLDER + "." + clazz, environment, propsGlobalClass);
        }

        // Load environment class properties
        if (configEnvironmentBaseFolder != null && clazz != null && propertyFileResolver.findAllBasePropertyFilenames(configEnvironmentBaseFolder + "/" + CONFIG_CLASS_FOLDER).contains(clazz)) {
            propsEnvClass = new AggregatedResourceBundle();
            mergeOverride(configBaseFolder + "." + environment.getCode() + "." + CONFIG_CLASS_FOLDER + "." + clazz, environment, propsEnvClass);
        }
        String detectedOS = getDetectedOS();
        propsOS = new AggregatedResourceBundle();
        if (detectedOS != null && propertyFileResolver.findAllBasePropertyFilenames(CONFIG_OS_FOLDER).contains(detectedOS)) {
            propsOS.mergeOverride(ResourceBundle.getBundle(configBaseFolder + "." + CONFIG_OS_FOLDER + "." + detectedOS));
        }
    }

    private static void mergeOverride(String propertyPath, ConfigEnvironment environment, AggregatedResourceBundle bundle) {
        log.debug("Loading PropertiesResourceBundle from " + propertyPath + ((environment != null && environment.getLocale() != null) ? " using locale " + environment.getLocale() : ""));
        if (environment != null && environment.getLocale() != null) {
            bundle.mergeOverride(ResourceBundle.getBundle(propertyPath, environment.getLocale()));
        } else {
            bundle.mergeOverride(ResourceBundle.getBundle(propertyPath));
        }
    }

    private String getDetectedOS() {
        if (OS.isFamilyWindows()) {
            return "windows";
        } else if (OS.isFamilyUnix()) {
            return "linux";
        } else if (OS.isFamilyMac()) {
            return "mac";
        } else {
            return null;
        }
    }

    public ConfigEnvironment getEnvironment() {
        return environment;
    }

    public String getClazz() {
        return clazz;
    }

    /**
     * Get the property value for this key, throws an exception if key is not existing.
     *
     * @param key
     * @return
     * @throws NoSuchElementException
     */
    public String get(String key) {
        return getRequired(key);
    }

    /**
     * Get the property value for this key, return null if it's not existing.
     *
     * @param key
     * @return value
     */
    public String getOptional(String key) {
        return get(key, null);
    }

    /**
     * Get the property value for this key, returns the given defaultValue if it's not existing.
     *
     * @param key
     * @param defaultValue
     * @return value
     */
    public String get(String key, String defaultValue) {
        if (parentConfig != null && parentConfig.contains(key)) {
            log.debug("Retrieved property [" + key + "] from parent config");
            return parentConfig.get(key);
        }
        if (propsTestNG != null && propsTestNG.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from TestNG Parameters");
            return propsTestNG.getString(key);
        } else if (propsOS != null && propsOS.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from OS properties");
            return propsOS.getString(key);
        } else if (propsCustomClass != null && propsCustomClass.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from custom set class properties");
            return propsCustomClass.getString(key);
        } else if (propsEnvClass != null && propsEnvClass.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from Environment class properties");
            return propsEnvClass.getString(key);
        } else if (propsGlobalClass != null && propsGlobalClass.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from Global class properties");
            return propsGlobalClass.getString(key);
        } else if (propsEnv != null && propsEnv.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from Environment properties");
            return propsEnv.getString(key);
        } else if (propsGlobal != null && propsGlobal.containsKey(key)) {
            log.debug("Retrieved property [" + key + "] from Global properties");
            return propsGlobal.getString(key);
        } else {
            log.warn("Property [" + key + "] has not been found, returning default value");
            return defaultValue;
        }
    }

    /**
     * Get the property value for this key, throws an exception if key is not existing.
     *
     * @param key
     * @return
     * @throws NoSuchElementException
     */
    public String getRequired(String key) throws NoSuchElementException {
        if (contains(key))
            return get(key, null);
        else
            throw new NoSuchElementException("Key [" + key + "] does not exist");
    }

    /**
     * Check if the key is somewhere in the properties chain on all layers including parent.
     *
     * @param key
     * @return
     */
    public boolean contains(String key) {
        if (parentConfig != null && parentConfig.contains(key)) return true;
        if (propsTestNG != null && propsTestNG.containsKey(key)) return true;
        if (propsOS != null && propsOS.containsKey(key)) return true;
        if (propsCustomClass != null && propsCustomClass.containsKey(key)) return true;
        if (propsEnvClass != null && propsEnvClass.containsKey(key)) return true;
        if (propsGlobalClass != null && propsGlobalClass.containsKey(key)) return true;
        if (propsEnv != null && propsEnv.containsKey(key)) return true;
        if (propsGlobal != null && propsGlobal.containsKey(key)) return true;
        // 404 no property found
        return false;
    }

    /**
     * Loads a special class properties file which overrides all the layers except the TestNG and OS parameter layer.
     *
     * @param custom Properties object
     */
    public void loadCustomClassProperties(Properties custom) {
        if (propsCustomClass == null) propsCustomClass = new AggregatedResourceBundle();
        propsCustomClass.mergeOverride(custom);
    }

    public void loadCustomClassProperties(String propertyFilename) {
        if (StringUtils.isBlank(propertyFilename)) {
            throw new IllegalArgumentException("Provided propertyFilename must not be blank");
        }
        propertyFilename = StringUtils.removeEnd(propertyFilename, ".properties");
        boolean found = false;

        // Load global class properties
        if (configGlobalBaseFolder != null && propertyFileResolver.findAllBasePropertyFilenames(configGlobalBaseFolder + "/" + CONFIG_CLASS_FOLDER).contains(propertyFilename)) {
            propsGlobalClass = propsGlobalClass == null ? new AggregatedResourceBundle() : propsGlobalClass;
            mergeOverride(configBaseFolder + "." + CONFIG_GLOBAL_BASE_FOLDER + "." + CONFIG_CLASS_FOLDER + "." + propertyFilename, environment, propsGlobal);
            found = true;
        }

        // Load environment class properties
        if (configEnvironmentBaseFolder != null && propertyFilename != null && propertyFileResolver.findAllBasePropertyFilenames(configEnvironmentBaseFolder + "/" + CONFIG_CLASS_FOLDER).contains(propertyFilename)) {
            propsEnvClass = propsEnvClass == null ? new AggregatedResourceBundle() : propsEnvClass;
            mergeOverride(configBaseFolder + "." + environment.getCode() + "." + CONFIG_CLASS_FOLDER + "." + propertyFilename, environment, propsEnvClass);
            found = true;
        }
        if (!found) {
            throw new IllegalArgumentException("Provided named property file has not been found: " + propertyFilename);
        }
    }

    /**
     * Log all the properties by category, starting with the lowest layer.
     */
    public void logAvailableProperties() {
        if (parentConfig != null) {
            log.info("Parent properties start");
            parentConfig.logAvailableProperties();
            log.info("Parent properties end");
        }
        logProperties("TestNG parameters", propsTestNG);
        logProperties("OS", propsOS);
        logProperties("Custom class", propsCustomClass);
        if (environment != null) logProperties("Environment class " + environment, propsEnvClass);
        logProperties("Global class", propsGlobalClass);
        if (environment != null) logProperties("Environment " + environment, propsEnv);
        logProperties("Global", propsGlobal);
    }

    private void logProperties(String title, AggregatedResourceBundle props) {
        if (props == null) return;
        log.info("CM Properties available from " + title);
        for (String key : props.keySet())
            log.info("\tKey[" + key + "], Value[" + props.getString(key) + "]");
    }

    public List<Module> getGuiceModules() {
        if (parentConfig != null) {
            List<Module> modules = parentConfig.getGuiceModules();
            modules.addAll(guiceModules);
            return modules;
        } else if (guiceModules != null) {
            return guiceModules;
        } else {
            return new ArrayList<Module>();
        }
    }

    public ConfigProvider addGuiceModule(Module... modules) {
        if (guiceModules == null) {
            // initialize
            guiceModules = new ArrayList<Module>(Arrays.asList(modules));
        } else {
            guiceModules.addAll(Arrays.asList(modules));
        }
        return this;
    }

    public ConfigProvider addGuiceModule(List<Module> modules) {
        if (guiceModules == null) {
            // initialize
            guiceModules = modules;
        } else {
            guiceModules.addAll(modules);
        }
        return this;
    }

    public ConfigProvider addUberGuiceModule(List<Module> modules) {
        if (parentConfig == null) {
            addGuiceModule(modules);
        } else {
            parentConfig.addUberGuiceModule(modules);
        }
        return this;
    }

    /**
     * Add an arbitary object to retrieve it later in this or in one of the childs.
     *
     * @param key
     * @param o
     */
    public void addObj(String key, Object o) {
        if (obj == null) obj = new ConcurrentHashMap<String, Object>();
        obj.put(key, o);
    }

    /**
     * Add an arbitary object to the uber parent to retrieve it later in this or in one other child of the whole config
     * tree.
     *
     * @param key
     * @param o
     */
    public void addUberObj(String key, Object o) {
        if (parentConfig != null) parentConfig.addUberObj(key, o);
        // We are the uber parent so lets handle this
        if (obj == null) obj = new ConcurrentHashMap<String, Object>();
        obj.put(key, o);
    }

    /**
     * Get back an arbitary object
     *
     * @param key
     * @return
     */
    public Object getObj(String key) {
        if (obj != null && obj.containsKey(key)) return obj.get(key);
        // Not in the local one, let's check if we have a parent and delegate the request
        if (parentConfig != null) return parentConfig.getObj(key);
        // Seems like we are the uber parent but don't have it so return null
        return null;
    }

    /**
     * Create an instance of a class using the config injector and any guice modules given in parameters. It will use
     * this ConfigProvider as parent, that means that all the properties from this object will override the properties
     * it loads from it's own class files.
     *
     * @param type
     * @param modules ...
     * @param <T>
     * @return object instance
     */
    public <T> T create(Class<T> type, Module... modules) {
        List<Module> mList = new ArrayList<Module>(Arrays.asList(modules));
        mList.addAll(getGuiceModules());
        mList.add(new ConfigModule(this, environment, type, propsTestNG));
        Injector injector = com.google.inject.Guice.createInjector(mList);
        return injector.getInstance(type);
    }
}