package ch.racic.testing.cm.config;

/**
 * Created by rac on 03.05.15.
 */
public class C {

    private C() {
        throw new AssertionError();
    }

    public static class SimpleTest {
        public static final String CUSTOM_VARIABLE_FOR_SIMPLETEST = "custom.variable.for.SimpleTest";
    }

    public static class TestObject {
        public static final String TESTOBJECT_CONF = "testobject.conf";
    }
}
