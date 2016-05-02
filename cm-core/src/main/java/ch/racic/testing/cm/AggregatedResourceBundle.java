/*
 * Copyleft (c) 2015. This code is for learning purposes only.
 * Do whatever you like with it but don't take it as perfect code.
 * //Michel Racic (http://rac.su/+)//
 */

package ch.racic.testing.cm;

import java.util.*;

/**
 * A {@link java.util.ResourceBundle} whose content is aggregated from multiple source bundles. Code taken from
 * hibernate-validator source
 *
 * @author Gunnar Morling
 */
public class AggregatedResourceBundle extends ResourceBundle {
    private final Map<String, Object> contents = new HashMap<String, Object>();

    /**
     * Creates a new AggregatedResourceBundle.
     *
     * @param bundles A list of source bundles, which shall be merged into one aggregated bundle. The newly created
     *                bundle will contain all keys from all source bundles. In case a key occurs in multiple source
     *                bundles, the value will be taken from the first bundle containing the key.
     */
    public AggregatedResourceBundle(List<ResourceBundle> bundles) {
        if (bundles != null) {

            for (ResourceBundle bundle : bundles) {
                mergeOverride(bundle);
            }
        }
    }

    public AggregatedResourceBundle() {
    }

    public AggregatedResourceBundle(AggregatedResourceBundle toCopy) {
        if (toCopy!= null && toCopy.contents!=null) {
            this.contents.putAll(toCopy.contents);
        }
    }

    /**
     * Merge the new bundle into the existing while overriding already existing keys.
     *
     * @param bundle
     */
    public synchronized void mergeOverride(ResourceBundle bundle) {
        Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            String oneKey = keys.nextElement();
                contents.put(oneKey, bundle.getObject(oneKey));
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return new IteratorEnumeration<String>(contents.keySet().iterator());
    }

    @Override
    public boolean containsKey(String key){
        return contents.containsKey(key);
    }

    @Override
    public Set<String> keySet(){
        return contents.keySet();
    }

    @Override
    protected Object handleGetObject(String key) {
        return contents.get(key);
    }

    /**
     * Merge the new bundle into the existing while overriding already existing keys.
     *
     * @param params
     */
    public synchronized void mergeOverride(Map<String, String> params) {
        contents.putAll(params);
    }

    public synchronized void mergeOverride(Properties props) {
        for (String key : props.stringPropertyNames()) {
            contents.put(key, props.getProperty(key));
        }
    }

    /**
     * An {@link java.util.Enumeration} implementation, that wraps an {@link java.util.Iterator}. Can be used to
     * integrate older APIs working with enumerations with iterators.
     *
     * @param <T> The enumerated type.
     * @author Gunnar Morling
     */
    private static class IteratorEnumeration<T> implements Enumeration<T> {

        private final Iterator<T> source;

        /**
         * Creates a new IterationEnumeration.
         *
         * @param source The source iterator. Must not be null.
         */
        public IteratorEnumeration(Iterator<T> source) {

            if (source == null) {
                throw new IllegalArgumentException("Source must not be null");
            }

            this.source = source;
        }

        public boolean hasMoreElements() {
            return source.hasNext();
        }

        public T nextElement() {
            return source.next();
        }
    }
}
