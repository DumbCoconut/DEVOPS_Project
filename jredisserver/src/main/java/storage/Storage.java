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

    /**
     * Add the value to the tail of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the tail of the list.
     * @return True if we added the value to the list, false if key was not holding a list.
     */
    public synchronized boolean lPush(String key, Object value) {
        return insertHelper(key, value, -1);
    }

    /**
     * Add the value to the head of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the head of the list.
     * @return True if we added the value to the list, false if key was not holding a list.
     */
    public synchronized boolean rPush(String key, Object value) {
        return insertHelper(key, value, 0);
    }

    /**
     * Add the value at the given index of the list stored at key.
     * <p>
     *     If the index is negative, we add the value at the end of the list.
     * </p>
     * @param key The key holding the list.
     * @param value The value to add to the list.
     * @param index The index where to add the value.
     * @return True if we added the value to the list, false if key was not holding a list.
     */
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

    /**
     * Atomically return and remove the first element of the list.
     * <p>
     *     For example if the list contains the elements "a","b","c" LPOP will return "a" and the list will
     *     become "b","c".
     * </p>
     * @param key The key holding the list.
     * @return null if the key does not exist or the list is already empty, the removed object otherwise.
     */
    public synchronized Object lPop(String key) {
        return removeHelper(key, false);
    }

    /**
     * Atomically return and remove the last element of the list.
     * <p>
     *      For example if the list contains the elements "a","b","c" RPOP will return "c" and the list will
     *      become "a","b".
     * </p>
     * @param key The key holding the list.
     * @return null if the key does not exist or the list is already empty, the removed object otherwise.
     */
    public synchronized Object rPop(String key) {
        return removeHelper(key, true);
    }

    /**
     * Remove either the first element or the last element.
     * @param key The key holding the list.
     * @param first True if we want to remove the first element, false if we want to remove the last element.
     * @return null if the key does not exist or the list is already empty, the removed object otherwise.
     */
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

    /**
     *  Return the specified element of the list stored at the specified key.
     *  <p>
     *      0 is the first element, 1 the second and so on.
     *      If the value stored at key is not of list type an error is returned.
     *      If the index is out of range an empty string is returned.
     *  </p>
     * @param key The key holding the list.
     * @param index The index of the element.
     * @return null if key does not hold a list, empty string if the index  is out of range, the element otherwise.
     */
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

    /**
     *  Return the length of the list stored at the specified key.
     *  <p>
     *      If the key does not exist zero is returned (the same behaviour as for empty lists).
     *      If the value stored at key is not a list an error is returned.
     *  </p>
     * @param key The key holding the list.
     * @return null if the value stored at key is not a list, the length of the list otherwise.
     */
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

    /**
     * Set the list element at index with the new value.
     * <p>
     *      Out of range indexes will generate an error.
     * </p>
     * @param key The key holding the list.
     * @param index The index where to store the new value.
     * @param value The new value.
     * @return True if the new value was set, false if key does not exist or is not holding a key or index is out of range.
     */
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

    /**
     *  Return the specified elements of the list stored at the specified key. Start and end are zero-based indexes.
     *  <p>
     *      For example LRANGE foobar 0 2 will return the first three elements of the list.
     *      Indexes out of range will not produce an error: if start is over the end of the list, or start > end,
     *      an empty list is returned. If end is over the end of the list, we will threat it just like the last element
     *      of the list.
     *  </p>
     * @param key The key holding the list.
     * @param start The start of the range.
     * @param end The end of the range.
     * @return A list of the objects that are in the given range, null if the key does not hold a list.
     */
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

    /**
     * Remove the first count occurrences of the value element from the list.
     * <p>
     *      If count is zero all the elements are removed.
     *      The number of removed elements is returned as an integer.
     *      Note that non existing keys are considered like empty lists by LREM, so LREM against non existing keys
     *      will always return 0.
     * </p>
     * @param key The key holding the list.
     * @param count The number of elements to remove.
     * @param value The value to remove.
     * @return An integer reply containing the number of removed elements if the operation succeeded.
     */
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

    /**
     *  Trim an existing list so that it will contain only the specified range of elements specified.
     *  <p>
     *      Start and end are zero-based indexes. 0 is the first element of the list (the list head), 1 the next element
     *      and so on. For example LTRIM foobar 0 2 will modify the list stored at foobar key so that only the first
     *      three elements of the list will remain. Indexes out of range will not produce an error: if start is over the
     *      end of the list, or start > end, an empty list is left as value. If end over the end of the list we will
     *      threat it just like the last element of the list.
     *  </p>
     * @param key The key holding the list.
     * @param start The start of the range.
     * @param end The end of the range.
     * @return true if the key exists and holds a list, false otherwise.
     */
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

    /**
     *  Add the specified member to the set value stored at key.
     *  <p>
     *      f member is already a member of the set no operation is performed. If key does not exist a new set with the
     *      specified member as sole member is created. If the key exists but does not hold a set value an error is
     *      returned.
     *  </p>
     * @param key The key holding the set.
     * @param member The member to add.
     * @return 1 if the new element was added, 0 if the element was already a member of the set, -1 if an error happened.
     */
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

    /**
     * Return the set cardinality (number of elements). If the key does not exist 0 is returned, like for empty sets.
     * @param key The key holding the set.
     * @return Cardinality of the set, -1 if key holds anything but a set.
     */
    public synchronized int scard(String key) {
        int res = 0;
        if (cache.containsKey(key)) {
            Object s = cache.get(key);
            res = (s instanceof HashSet) ? ((HashSet) s).size() : -1;
        }
        return res;
    }

    /**
     *  Remove the specified member from the set value stored at key.
     *  <p>
     *      If member was not a member of the set no operation is performed.
     *      If key does not hold a set value an error is returned.
     *  </p>
     * @param key The key holding the set.
     * @param member The member to remove.
     * @return 1 if the element was removed, 0 if the element was not a member of the set, -1 if an error happened.
     */
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

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is returned.
     * @param key The key holding the set.
     * @param member The member to test.
     * @return 1 if the element is a member of the set, 0 if it is not a member, -1 if an error happened.
     */
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


    /**
     *  Return all the members (elements) of the set value stored at key. This is just syntax glue for SINTERSECT.
     * @param key The key holding the set.
     * @return All the members (elements) of the set value stored at key, null if key does not hold a set.
     */
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

    /**
     * Return the members of a set resulting from the intersection of all the sets hold at the specified keys.
     * <p>
     *     If just a single key is specified, then this command produces the same result as SMEMBERS.
     *     Actually SMEMBERS is just syntax sugar for SINTERSECT.
     *     Non existing keys are considered like empty sets, so if one of the keys is missing an empty set is returned
     *     (since the intersection with an empty set always is an empty set).
     * </p>
     * @param keys The keys holding the sets.
     * @return the members resulting from the intersection of all the sets, null if at least one key does not hold a set.
     */
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

    /**
     * This command works exactly like SINTER but instead of being returned the resulting set is stored as dstkey.
     * @param keys The keys holding the sets.
     * @return 1 if sinter succeed, -1 if at least one key does not hold a set.
     */
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

    /**
     * Remove a random element from a Set returning it as return value.
     * <p>
     *     If the Set is empty or the key does not exist, a null object is returned.
     *     The SRANDMEMBER command does a similar work but the returned element is not removed from the Set.
     * </p>
     * @param key The key holding the set.
     * @return The removed object, null if key does not exist or is not holding a set.
     */
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
        Object res = randomlyPick(set);
        set.remove(res);
        return res;
    }

    private synchronized Object randomlyPick(HashSet set) {
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
        }
        return res;
    }

    /**
     * Return a random element from a Set, without removing the element.
     * <p>
     *     If the Set is empty or the key does not exist, a null object is returned.
     *     The SPOP command does a similar work but the returned element is popped (removed) from the Set.
     * </p>
     * @param key The key holding the set.
     * @return The random object, null if key does not exist or is not holding a set.
     */
    public synchronized Object srandmember(String key) {
        Object res = null;
        if (cache.containsKey(key)) {
            Object o = cache.get(key);
            if (o instanceof HashSet) {
                res = randomlyPick((HashSet) o);
            }
        }
        return res;
    }

    /**
     *  Move the specifided member from the set at srckey to the set at dstkey.
     *  <p>
     *      This operation is atomic, in every given moment the element will appear to be in the source or destination
     *      set for accessing clients.
     *
     *      If the source set does not exist or does not contain the specified element no operation is performed and
     *      zero is returned, otherwise the element is removed from the source set and added to the destination set.
     *      On success one is returned, even if the element was already present in the destination set.
     *
     *      An error is raised if the source or destination keys contain a non Set value.
     * </p>
     * @param srckey The key of the source set.
     * @param dstkey The key of the destination set.
     * @param member The member to move.
     * @return 1 if the element was moved, 0 if the element was not found on and no operation was performed, -1 if error
     */
    public synchronized int smove(String srckey, String dstkey, Object member) {
        int res = 0;

        // does srckey exists? yes ->continue no->0
        if (cache.containsKey(srckey)) {
            Object src = cache.get(srckey);
            // is srckey a set? yes->continue no->error
            if (src instanceof HashSet) {
                HashSet srcSet = (HashSet) src;
                // does srckey contain member? yes->remove & continue no->0
                if (srcSet.contains(member)) {
                    srcSet.remove(member);
                    // does cache contain dstkey? yes->continue no->insert src
                    if (cache.containsKey(dstkey)) {
                        Object dst = cache.get(dstkey);
                        // is dstkey a set? yes-> add it & done no-> error
                        if (dst instanceof HashSet) {
                            HashSet dstSet = (HashSet) dst;
                            if (! dstSet.contains(member)) {
                                sadd(dstkey, member);
                                res = 1;
                            }
                        } else {
                            res = -1;
                        }
                    } else {
                        sadd(dstkey, member);
                        res = 1;
                    }
                }
            } else {
                res = -1;
            }
        }
        return res;
    }

    /**
     * Return the members of a set resulting from the union of all the sets hold at the specified keys.
     * <p>
     *      If just a single key is specified, then this command produces the same result as SMEMBERS.
     *      Non existing keys are considered like empty sets.
     * </p>
     * @param keys The keys holding the sets.
     * @return the members resulting from the union of all the sets, null if at least one key does not hold a set.
     */
    public synchronized List<Object> sunion(String[] keys) {
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
                    // One of the provided keys is not a HashSet, we can't do sunion, we return an error
                    return null;
                }
            }
        }

        // Do the union
        ArrayList<Object> res = new ArrayList<>();
        for (HashSet set : sets) {
            for (Object o : set) {
                if (!res.contains(o)) {
                    res.add(o);
                }
            }
        }

        return res;
    }

    /**
     * This command works exactly like SUNION but instead of being returned the resulting set is stored as dstkey.
     * <p>
     *     Any existing value in dstkey will be over-written.
     * </p>
     * @param keys The keys holding the sets.
     * @return 1 if sunion succeed, -1 if at least one key does not hold a set.
     */
    public synchronized int sunionstore(String[] keys) {
        // we don't want to do union on the first key
        String[] subKeys = new String[keys.length - 1];
        System.arraycopy(keys, 1, subKeys, 0, keys.length - 1);

        List<Object> union = sunion(subKeys);
        // at least one key was not a set, we return an error
        if (union == null) {
            return -1;
        }

        HashSet<Object> unionSet = new HashSet<>();
        unionSet.addAll(union);
        if (cache.containsKey(keys[0])) {
            cache.replace(keys[0], unionSet);
        } else {
            cache.put(keys[0], unionSet);
        }
        return 1;
    }

    /**
     * Return the members of a set resulting from the difference between the first set provided and all the successive sets.
     * <p>
     *      Example:
     *          key1 = x,a,b,c
     *          key2 = c
     *          key3 = a,d
     *          SDIFF key1,key2,key3 => x,b
     *      Non existing keys are considered like empty sets.
     * </p>
     * @param keys The keys holding the sets.
     * @return The members resulting from the diff, null if at least one of the key does not hold a set.
     */
    public synchronized List<Object> sdiff(String[] keys) {
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
                    // One of the provided keys is not a HashSet, we can't do sdiff, we return an error
                    return null;
                }
            } else {
                sets.add(new HashSet<>());
            }
        }

        /* unchecked cast, we can't use instanceof HashSet<Object> because of type erasure.
         * there might be some kind of work around, but we know for sure that we'll only have
         * HashSet of objects. Best thing would most likely to change the design a bit, but
         * lack of time and we'll just assume that nobody will never ever change the code of
         * storage in a way that it adds HashSet that are not containing objects. */
        HashSet<Object> setDiff = (HashSet<Object>) sets.get(0).clone();

        // Do the diff. Starts at one, otherwise we'll do set diff with itself..
        for (int i = 1; i < keys.length; i++) {
            setDiff.removeAll(sets.get(i));
        }

        // Create the list of diff elements
        ArrayList<Object> diffElements = new ArrayList<>();
        for (Object o : setDiff) {
            diffElements.add(o);
        }

        return diffElements;
    }

    /**
     * This command works exactly like SDIFF but instead of being returned the resulting set is stored in dstkey.
     * @param keys The keys holding the sets.
     * @return 1 if sdiff succeed, -1 if at least one key does not hold a set.
     */
    public synchronized int sdiffstore(String[] keys) {
        // we don't want to do diff on the first key
        String[] subKeys = new String[keys.length - 1];
        System.arraycopy(keys, 1, subKeys, 0, keys.length - 1);

        List<Object> diff = sdiff(subKeys);
        // at least one key was not a set, we return an error
        if (diff == null) {
            return -1;
        }

        HashSet<Object> diffSet = new HashSet<>();
        diffSet.addAll(diff);
        if (cache.containsKey(keys[0])) {
            cache.replace(keys[0], diffSet);
        } else {
            cache.put(keys[0], diffSet);
        }
        return 1;
    }
}