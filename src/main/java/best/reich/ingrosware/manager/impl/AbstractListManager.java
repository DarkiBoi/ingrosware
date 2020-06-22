package best.reich.ingrosware.manager.impl;

import best.reich.ingrosware.manager.Manager;

import java.util.ArrayList;
import java.util.List;

/**
 * made for Ingros
 *
 * @author Brennan
 * @since 6/13/2020
 **/
public abstract class AbstractListManager<K> implements Manager {
    private List<K> registry = new ArrayList<>();

    public void add(K k) {
        registry.add(k);
    }

    public void remove(K k) {
        registry.remove(k);
    }

    public void clear() {
        registry.clear();
    }

    public int size() {
        return registry.size();
    }

    public boolean isEmpty() {
        return registry.isEmpty();
    }

    public List<K> getList() {
        return registry;
    }

    public K get(int index) {
        return registry.get(index);
    }
}
