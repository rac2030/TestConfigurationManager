/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import java.util.Locale;

/**
 * Created by rac on 05.04.15.
 */
public class ConfigEnvironment {
    private String name;
    private String description;
    private String code;
    private Locale locale;

    public ConfigEnvironment() {
    }

    public ConfigEnvironment(String name, String description, String code) {
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public ConfigEnvironment(String name, String description, String code, Locale locale) {
        this.name = name;
        this.description = description;
        this.code = code;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

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

    public String getCode() {
        return code;
    }

    public ConfigEnvironment setCode(String code) {
        this.code = code;
        return this;
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
