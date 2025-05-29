package ru.otus.cachehw;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

    private final Map<K, V> cache = new WeakHashMap<>();

    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);

        notifyAllListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        var value = cache.remove(key);

        notifyAllListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        var value = cache.get(key);

        notifyAllListeners(key, value, "get");

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

    private void notifyAllListeners(K key, V value, String action) {
        listeners.forEach(l -> {
            try {
                l.notify(key, value, action);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
