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
     * @return
     */
    public Class[] value() default {};

    /**
     * Custom properties file name relative to the config class folder.
     * @return
     */
    public String fileName() default "";
}
