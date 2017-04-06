package storage;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.ConcurrentMap;

public class Storage {
    static public final long MAX_SIZE = 10L;

    /**
     * Contains the stored objects. Thread-safe.
     */
    private ConcurrentMap<String, Object> cache;

    /**
     * The maximum number of objects in the cache. Volatile ensure the thread-safe property,
     * otherwise writes and reads of long and double values are not always atomic.
     */
    private volatile long maxSize;

    /**
     * Storage constructor.
     * @param size The maximum number of objects to keep in memory.
     * @throws IllegalArgumentException When size is <= 0.
     */
    public Storage(long size) throws IllegalArgumentException {
        setMaxSize(size);
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .<String, Object>build().asMap();
    }

    /**
     * Storage constructor. Keeps up to 10 objects in memory.
     */
    public Storage() {
        this(MAX_SIZE);
    }

    /**
     * Get the cache.
     * @return The thread-safe map representing the cache.
     */
    public ConcurrentMap<String, Object> getCache() {
        return cache;
    }

    /**
     * Get the maximum number of objects in the cache.
     * @return The maximum number of objects in the cache.
     */
    public long getMaxSize() {
        return maxSize;
    }

    /**
     * Set the maximum number of objects in the cache.
     * @param size The maximum number of objects to keep in memory.
     * @throws IllegalArgumentException When size is <= 0.
     */
    public void setMaxSize(Long size) throws IllegalArgumentException {
        if (size <= 0L) {
            throw new IllegalArgumentException("Invalid size. The size of the cache must be >= 1.");
        }
        maxSize = size;
    }

    /**
     * Store an object in the storage.
     * @param o The object to store.
     * @param key The key corresponding to the object.
     * @throws DuplicatedKeyException When the key is already used.
     */
    public synchronized void store(Object o, String key) throws DuplicatedKeyException {
        if (cache.containsKey(key)) {
            throw new DuplicatedKeyException(key);
        }
        cache.put(key, o);
    }

    /**
     * Get an object from the storage.
     * @param key The key corresponding to the object we want.
     * @return The object corresponding to the given key.
     * @throws NonExistentKeyException When the key is not in the cache
     */
    public synchronized Object get(String key) throws NonExistentKeyException {
        if (!cache.containsKey(key)) {
            throw new NonExistentKeyException(key);
        }
        return cache.get(key);
    }

    /**
     * Remove an object from the storage.
     * @param key The key corresponding to the object to remove.
     * @throws NonExistentKeyException When the key is not in the cache.
     */
    public synchronized void remove(String key) throws NonExistentKeyException {
        if (!cache.containsKey(key)) {
            throw new NonExistentKeyException(key);
        }
        cache.remove(key);
    }
}