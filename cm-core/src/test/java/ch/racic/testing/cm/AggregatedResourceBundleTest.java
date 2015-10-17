package ch.racic.testing.cm;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

import java.util.ResourceBundle;

public class AggregatedResourceBundleTest {

    @Test
    public void testMergeOverride() throws Exception {
        AggregatedResourceBundle resourceBundle = new AggregatedResourceBundle();
        assertEquals(0, resourceBundle.keySet().size());

        ResourceBundle testResBundle = ResourceBundle.getBundle(ConfigProvider.CONFIG_BASE_FOLDER + "." + ConfigProvider.CONFIG_GLOBAL_BASE_FOLDER + ".test");
        ResourceBundle test2ResBundle = ResourceBundle.getBundle(ConfigProvider.CONFIG_BASE_FOLDER + "." + ConfigProvider.CONFIG_GLOBAL_BASE_FOLDER + ".test2");
        assertEquals(6, testResBundle.keySet().size());
        assertEquals(5, test2ResBundle.keySet().size());

        resourceBundle.mergeOverride(testResBundle);
        assertEquals(6, resourceBundle.keySet().size());

        resourceBundle.mergeOverride(test2ResBundle);
        assertEquals(11, resourceBundle.keySet().size());
    }
}