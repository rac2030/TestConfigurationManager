package ch.racic.testing.cm;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.*;

public class PropertyFileResolver extends PathMatchingResourcePatternResolver {

    private static final Logger log = LogManager.getLogger(PathMatchingResourcePatternResolver.class);

    private final Resource configRootRes;

    private final Set<String> allLocales;

    public PropertyFileResolver(String configRoot) {
        super();
        try {
            String rootDirPattern = "classpath*:" + configRoot;

            Resource[] res = getResources(rootDirPattern);
            if (res.length == 0) {
                throw new RuntimeException("There is no resource matching root pattern: " + rootDirPattern);
            }

            for (Resource r : res) {
                log.debug("Discovered root configuration resource: " + r.toString());
            }

            configRootRes = res[0];
            log.debug("Root configuration resource set to: " + configRootRes.toString());

            //stores all JVM Locales only once, used to return only base property names
            allLocales = new HashSet<String>();
            for (Locale loc : Locale.getAvailableLocales()) {
                allLocales.add(loc.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean resourceExists(String subDirectory) {
        if (subDirectory == null) {
            return false;
        }
        try {
            subDirectory = StringUtils.removeEnd(subDirectory, "/");
            if (isJarResource(configRootRes)) {
                subDirectory += "/";
                return !doFindPathMatchingJarResources(configRootRes, subDirectory).isEmpty();
            } else {
                return !doFindPathMatchingFileResources(configRootRes, subDirectory).isEmpty();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public List<String> findAllBasePropertyFilenames(String subDirectory) {
        try {
            String subPattern = StringUtils.removeEnd(subDirectory, "/");
            subPattern += "/*.properties";
            TreeSet<String> properties = new TreeSet<String>();
            Set<Resource> res = null;
            if (isJarResource(configRootRes)) {
                res = doFindPathMatchingJarResources(configRootRes, subPattern);
            } else {
                res = doFindPathMatchingFileResources(configRootRes, subPattern);
            }

            for (Resource r : res) {
                String baseName = StringUtils.removeEnd(r.getFilename(), ".properties");

                String loc = StringUtils.substringAfterLast(baseName, "_");
                //omit properties which are locale specific, return base names
                if (StringUtils.isBlank(loc) || !allLocales.contains(loc)) {
                    properties.add(baseName);
                }
            }
            return new ArrayList<String>(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}