package best.reich.ingrosware.manager.impl;

import best.reich.ingrosware.manager.Manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public abstract class AbstractMapManager<K, V> implements Manager {
    private final Map<K, V> registry = new HashMap<>();

    public void put(K k, V v) {
        registry.put(k , v);
    }

    public void remove(K k) {
        registry.remove(k);
    }

    public void clear() {
        registry.clear();
    }

    public Collection<V> getValues() {
        return registry.values();
    }

    public Collection<K> getKeySet() {
        return registry.keySet();
    }

    public Set<Map.Entry<K, V>> getEntry() {
        return registry.entrySet();
    }

    public Map<K, V> getRegistry() {
        return registry;
    }
}
