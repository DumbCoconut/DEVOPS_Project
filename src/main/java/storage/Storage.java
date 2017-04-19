package storage;

import com.google.common.cache.CacheBuilder;
import storage.exceptions.DuplicatedKeyException;
import storage.exceptions.NonExistentKeyException;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Storage {
    /**
     * The maximum number of objects to keep in the cache.
     */
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
     * Random generator
     */
    private Random random;
    /**
     * Storage constructor.
     *
     * @param size The maximum number of objects to keep in memory.
     * @throws IllegalArgumentException When size is <= 0.
     */
    public Storage(long size) throws IllegalArgumentException {
        setMaxSize(size);
        random = new Random();
        cache = CacheBuilder.newBuilder()
                            .maximumSize(maxSize)
                            .<String, Object>build()
                            .asMap();
    }

    /**
     * Storage constructor. Keeps up to 10 objects in memory.
     */
    public Storage() {
        this(MAX_SIZE);
    }

    /**
     * Get the cache.
     *
     * @return The thread-safe map representing the cache.
     */
    public ConcurrentMap<String, Object> getCache() {
        return cache;
    }

    /**
     * Get the maximum number of objects in the cache.
     *
     * @return The maximum number of objects in the cache.
     */
    public long getMaxSize() {
        return maxSize;
    }

    /**
     * Set the maximum number of objects in the cache.
     *
     * @param size The maximum number of objects to keep in memory.
     * @throws IllegalArgumentException When size is <= 0.
     */
    public void setMaxSize(Long size) throws IllegalArgumentException {
        if (size <= 0L) {
            throw new IllegalArgumentException("Invalid size. The size of the cache must be >= 1.");
        }
        maxSize = size;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                              STRINGS AND INTEGERS                                              */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    /**
     * Store an object in the storage.
     *
     * @param key The key corresponding to the object.
     * @param o   The object to store.
     * @throws DuplicatedKeyException When the key is already used.
     */
    public synchronized void store(String key, Object o) throws DuplicatedKeyException {
        if (cache.containsKey(key)) {
            throw new DuplicatedKeyException(key);
        }
        if (o instanceof String) {
            try {
                int i = Integer.parseInt((String) o);
                cache.put(key, i);
            } catch (NumberFormatException e) {
                cache.put(key, o);
            }
        } else {
            cache.put(key, o);
        }
    }

    /**
     * Get an object from the storage.
     *
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
     *
     * @param key The key corresponding to the object to remove.
     * @throws NonExistentKeyException When the key is not in the cache.
     */
    public synchronized void remove(String key) throws NonExistentKeyException {
        if (!cache.containsKey(key)) {
            throw new NonExistentKeyException(key);
        }
        cache.remove(key);
    }

    /**
     * Replace the old value of key by a new value.
     *
     * @param key   The key holding the old value.
     * @param value The new value.
     */
    public synchronized void replace(String key, Object value) {
        if (value instanceof String) {
            try {
                int i = Integer.parseInt((String) value);
                cache.replace(key, i);
            } catch (NumberFormatException e) {
                cache.replace(key, value);
            }
        } else {
            cache.replace(key, value);
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                      LISTS                                                     */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    public synchronized boolean lPush(String key, Object value) {
        return insertHelper(key, value, -1);
    }

    public synchronized boolean rPush(String key, Object value) {
        return insertHelper(key, value, 0);
    }

    private boolean insertHelper(String key, Object value, int index) {
        boolean success;
        if (!cache.containsKey(key)) {
            ArrayList<Object> list = new ArrayList<>();
            list.add(value);
            cache.put(key, list);
            success = true;
        } else {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                /* unchecked cast, we can't use instanceof ArrayList<Object> because of type erasure.
                 * there might be some kind of work around, but we know for sure that we'll only have
                 * ArrayList of objects. Best thing would most likely to change the design a bit, but
                 * lack of time and we'll just assume that nobody will never ever change the code of
                 * storage in a way that it adds ArrayList that are not containing objects. */
                if (index >= 0) {
                    ((ArrayList) l).add(index, value);
                } else {
                    ((ArrayList) l).add(value);
                }
                success = true;
            } else {
                success = false;
            }
        }
        return success;
    }

    public synchronized Object lPop(String key) {
        return removeHelper(key, false);
    }

    public synchronized Object rPop(String key) {
        return removeHelper(key, true);
    }

    private Object removeHelper(String key, boolean first) {
        Object removed = null;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList && !(((ArrayList) l).isEmpty())) {
                if (first) {
                    removed = ((ArrayList) l).remove(0);
                } else {
                    removed = ((ArrayList) l).remove(((ArrayList) l).size() - 1);
                }
            }
        }
        return removed;
    }

    public synchronized Object lindex(String key, int index) {
        Object o = null;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                if (index >= 0 && index < ((ArrayList) l).size()) {
                    o = ((ArrayList) l).get(index);
                } else {
                    o = "";
                }
            }
        }
        return o;
    }

    public synchronized int llen(String key) {
        int len = 0;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                len = ((ArrayList) l).size();
            } else {
                len = -1;
            }
        }
        return len;
    }

    public synchronized boolean lset(String key, int index, Object value) {
        boolean success = false;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                int len = ((ArrayList) l).size();
                if (index >= 0 && index < len) {
                    /* unchecked cast, we can't use instanceof ArrayList<Object> because of type erasure.
                     * there might be some kind of work around, but we know for sure that we'll only have
                     * ArrayList of objects. Best thing would most likely to change the design a bit, but
                     * lack of time and we'll just assume that nobody will never ever change the code of
                     * storage in a way that it adds ArrayList that are not containing objects. */
                    ((ArrayList) l).set(index, value);
                    success = true;
                }
            }
        }
        return success;
    }

    public synchronized ArrayList<Object> lrange(String key, int start, int end) {
        ArrayList<Object> range = new ArrayList<>();
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                end = end + 1;
                int len = ((ArrayList) l).size();
                if (end > len) {
                    end = len;
                }
                if (!(start > len - 1 || start > end || start < 0)) {
                    range.addAll(((ArrayList) l).subList(start, end));
                }
            } else {
                range = null;
            }
        }
        return range;
    }

    public synchronized int lrem(String key, int count, Object value) {
        int nbRemoved = 0;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                if (count == 0) {
                    /* unchecked cast, we can't use instanceof ArrayList<Object> because of type erasure.
                     * there might be some kind of work around, but we know for sure that we'll only have
                     * ArrayList of objects. Best thing would most likely to change the design a bit, but
                     * lack of time and we'll just assume that nobody will never ever change the code of
                     * storage in a way that it adds ArrayList that are not containing objects. */
                    ((ArrayList) l).removeIf(v -> v.equals(value));
                } else {
                    AtomicInteger c = new AtomicInteger(0);
                    /* unchecked cast, we can't use instanceof ArrayList<Object> because of type erasure.
                     * there might be some kind of work around, but we know for sure that we'll only have
                     * ArrayList of objects. Best thing would most likely to change the design a bit, but
                     * lack of time and we'll just assume that nobody will never ever change the code of
                     * storage in a way that it adds ArrayList that are not containing objects. */
                    ((ArrayList) l).removeIf(v -> v.equals(value) && c.get() < count && c.getAndIncrement() < count);
                    nbRemoved = c.get();
                }
            }
        }
        return nbRemoved;
    }

    public synchronized boolean ltrim(String key, int start, int end) {
        boolean success = false;
        if (cache.containsKey(key)) {
            Object l = cache.get(key);
            if (l instanceof ArrayList) {
                end = end + 1;
                int len = ((ArrayList) l).size();
                if (end > len) {
                    end = len;
                }
                ArrayList<Object> trimmed = new ArrayList<>();
                if (!(start < 0 || start > len - 1 || start > end)) {
                    trimmed.addAll(((ArrayList) l).subList(start, end));
                }
                cache.replace(key, trimmed);
                success = true;
            }
        }
        return success;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                      SETS                                                      */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    public synchronized int sadd(String key, Object member) {
        int res = -1;
        if (cache.containsKey(key)) {
            Object s = cache.get(key);
            if (s instanceof HashSet) {
                /* unchecked cast, we can't use instanceof HashSet<Object> because of type erasure.
                 * there might be some kind of work around, but we know for sure that we'll only have
                 * HashSet of objects. Best thing would most likely to change the design a bit, but
                 * lack of time and we'll just assume that nobody will never ever change the code of
                 * storage in a way that it adds HashSet that are not containing objects. */
                res = ((HashSet) s).add(member) ? 1 : 0;
            }
        } else {
            HashSet<Object> s = new HashSet<>();
            s.add(member);
            cache.put(key, s);
            res = 1;
        }
        return res;
    }

    public synchronized int scard(String key) {
        int res = 0;
        if (cache.containsKey(key)) {
            Object s = cache.get(key);
            res = (s instanceof HashSet) ? ((HashSet) s).size() : -1;
        }
        return res;
    }

    public synchronized int srem(String key, Object member) {
        int res = 0;
        if (cache.containsKey(key)) {
            Object s = cache.get(key);
            if (s instanceof HashSet) {
                res = ((HashSet) s).remove(member) ? 1 : 0;
            } else {
                res = -1;
            }
        }
        return res;
    }

    public synchronized int sismember(String key, Object member) {
        int res = 0;
        if (cache.containsKey(key)) {
            Object s = cache.get(key);
            if (s instanceof HashSet) {
                res = ((HashSet) s).contains(member) ? 1 : 0;
            } else {
                res = -1;
            }
        }
        return res;
    }

    public synchronized List<Object> smembers(String key) {
        List<Object> res = null;
        if (cache.containsKey(key)) {
            Object o = cache.get(key);
            if (o instanceof HashSet) {
                res = new ArrayList<>();
                /* unchecked cast, we can't use instanceof HashSet<Object> because of type erasure.
                 * there might be some kind of work around, but we know for sure that we'll only have
                 * HashSet of objects. Best thing would most likely to change the design a bit, but
                 * lack of time and we'll just assume that nobody will never ever change the code of
                 * storage in a way that it adds HashSet that are not containing objects. */
                ((HashSet) o).forEach(res::add);
            }
        } else {
            res = new ArrayList<>();
        }
        return res;
    }

    public synchronized List<Object> sinter(String[] keys) {
        // if no keys were provided, we return an empty list
        if (keys.length == 0) {
            return new ArrayList<>();
        }

        // Create a list of all the sets
        List<HashSet> sets = new ArrayList<>();
        for (String k : keys) {
            if (cache.containsKey(k)) {
                Object o = cache.get(k);
                if (o instanceof HashSet) {
                    sets.add((HashSet) o);
                } else {
                    // Early exit
                    // One of the provided keys is not a HashSet, we can't do sinter, we return an error
                    return null;
                }
            } else {
                // Early exit
                // Non existing keys are considered empty sets, if one of the keys is missing an empty set is returned
                return new ArrayList<>();
            }
        }

        /* unchecked cast, we can't use instanceof HashSet<Object> because of type erasure.
         * there might be some kind of work around, but we know for sure that we'll only have
         * HashSet of objects. Best thing would most likely to change the design a bit, but
         * lack of time and we'll just assume that nobody will never ever change the code of
         * storage in a way that it adds HashSet that are not containing objects. */
        HashSet<Object> setInter = sets.get(0);

        // Do the inter
        for (int i = 0; i < keys.length; i++) {
            setInter.retainAll(sets.get(i));
        }

        // Create the list of common elements
        ArrayList<Object> commonElements = new ArrayList<>();
        for (Object o : setInter) {
            commonElements.add(o);
        }
        return commonElements;
    }

    public synchronized int sinterstore(String[] keys) {
        // we don't want to do inter on the first key
        String[] subKeys = new String[keys.length - 1];
        for (int i = 0; i < keys.length - 1; i++) {
            subKeys[i] = keys[i + 1];
        }

        List<Object> inter = sinter(subKeys);
        // at least one key was not a set, we return an error
        if (inter == null) {
            return -1;
        }

        HashSet<Object> interSet = new HashSet<>();
        interSet.addAll(inter);
        if (cache.containsKey(keys[0])) {
            cache.replace(keys[0], interSet);
        } else {
            cache.put(keys[0], interSet);
        }
        return 1;
    }

    public synchronized Object spop(String key) {
        Object res = null;
        if (cache.containsKey(key)) {
            Object o = cache.get(key);
            if (o instanceof HashSet) {
                res = randomlyPickAndRemove((HashSet) o);
            }
        }
        return res;
    }

    private synchronized Object randomlyPickAndRemove(HashSet set) {
        Object res = null;
        int size = set.size();
        if (size > 0) {
            int randomItem = new Random().nextInt(size);
            int i = 0;
            for (Object item : set) {
                if (i == randomItem) {
                    res = item;
                    break;
                } else {
                    i++;
                }
            }
            set.remove(res);
        }
        return res;
    }

}