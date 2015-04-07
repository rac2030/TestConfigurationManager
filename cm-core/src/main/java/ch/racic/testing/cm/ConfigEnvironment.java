/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import java.util.Locale;

/**
 * Wrapper for environment data, the code is used as identifier and must be set trough the constructor.
 */
public class ConfigEnvironment {
    private String name;
    private String description;
    private final String code;
    private Locale locale;

    /**
     * Construct the environment with the minimal identity which is the key. The key identifies this environments folder
     * name.
     *
     * @param code
     */
    public ConfigEnvironment(String code) {
        this(null, null, code, null);
    }

    /**
     * Construct the environment giving it a human readable name (which can be used for reporting) and a description.
     * Everything except the key is optional and can be null.
     *
     * @param name
     * @param description
     * @param code
     */
    public ConfigEnvironment(String name, String description, String code) {
        this(name, description, code, null);
    }

    /**
     * Construct the environment giving it a human readable name (which can be used for reporting) and a description.
     * The optional locale is used to identify a localized version of a property file. Everything except the key is
     * optional and can be null.
     *
     * @param name
     * @param description
     * @param code
     * @param locale
     */
    public ConfigEnvironment(String name, String description, String code, Locale locale) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.locale = locale;
    }

    /**
     * Get the display name of this environment.
     *
     * @return display name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the display name of this environment.
     *
     * @param name
     * @return this
     */
    public ConfigEnvironment setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ConfigEnvironment setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get the environment folder name.
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    public Locale getLocale() {
        return locale;
    }

    public ConfigEnvironment setLocale(Locale locale) {
        this.locale = locale;
        return this;
    }

    @Override
    public String toString() {
        return "ConfigEnvironment{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
