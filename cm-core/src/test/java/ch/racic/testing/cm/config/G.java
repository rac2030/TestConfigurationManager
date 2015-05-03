package ch.racic.testing.cm.config;

/**
 * Created by rac on 03.05.15.
 */
public class G {

    private G() {
        throw new AssertionError();
    }

    public static class test {
        public static final String CONFIG_TEST_LOADEDFROM = "config.test.loadedfrom";
        public static final String CONFIG_TEST_GLOBAL = "config.test.global";
        public static final String CONFIG_TEST_ENV = "config.test.env";
        public static final String CONFIG_TEST_GLOBAL_CLASS = "config.test.global.class";
        public static final String CONFIG_TEST_ENV_CLASS = "config.test.env.class";
        public static final String DETECTED_OS = "detected.os";
    }

    public static class test2 {
        public static final String CONFIG_TEST2_LOADEDFROM = "config.test2.loadedfrom";
        public static final String CONFIG_TEST2_GLOBAL = "config.test2.global";
        public static final String CONFIG_TEST2_ENV = "config.test2.env";
        public static final String CONFIG_TEST2_GLOBAL_CLASS = "config.test2.global.class";
        public static final String CONFIG_TEST2_ENV_CLASS = "config.test2.env.class";
    }
}
