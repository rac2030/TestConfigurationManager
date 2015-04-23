/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm.annotation;

import java.lang.annotation.*;

/**
 * Created by rac on 06.04.15.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ClassConfig {
    /**
     * Class name corresponding to properties name in config class folder.
     *
     * @return class corresponding to the properties file
     */
    public Class[] value() default {};

    /**
     * Custom properties file name relative to the config class folder.
     * If you are using directories, it is needed that you follow the ResourceBundle notation which uses a dot '.' as
     * folder separator. You can leave out the .properties at the end as it will get stripped away anyway in order
     * to load a specific language bundle (using the locale and the standard loading mechanism of ResourceBundle.
     *
     * @return relative file name
     */
    public String fileName() default "";
}
