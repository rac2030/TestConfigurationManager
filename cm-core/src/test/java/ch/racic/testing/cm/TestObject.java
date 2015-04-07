/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import com.google.inject.Inject;

/**
 * Created by rac on 06.04.15.
 */
public class TestObject {

    public static final String TESTOBJECT_CONF = "testobject.conf";

    @Inject
    ConfigProvider cfg;

    public String getObjectConf() {
        return cfg.get(TESTOBJECT_CONF);
    }

}
