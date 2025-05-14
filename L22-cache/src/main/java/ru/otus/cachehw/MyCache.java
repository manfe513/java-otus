package ru.otus.cachehw;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();

    private final Set<HwListener<K,V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);

        listeners.forEach(l -> l.notify(key, value, "put"));
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);

        listeners.forEach(l -> l.notify(key, value, "remove"));
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);
        listeners.forEach(l -> l.notify(key, value, "get"));
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
