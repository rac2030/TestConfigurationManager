/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import com.google.inject.Inject;

import static ch.racic.testing.cm.config.C.TestObject.TESTOBJECT_CONF;

/**
 * Created by rac on 06.04.15.
 */
public class TestObject {

    @Inject
    ConfigProvider cfg;

    public String getObjectConf() {
        return cfg.get(TESTOBJECT_CONF);
    }

}
