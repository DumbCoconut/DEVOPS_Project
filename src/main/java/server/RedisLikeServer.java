package server;

import java.rmi.Remote;

interface RedisLikeServer extends Remote {
    /**
     * Get the value of the specified key.
     * @param key The key we want the value of.
     * @return The value of the key if it exists, null otherwise.
     */
    Object get(String key);

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * @param key The key holding the value.
     * @param value The value to set.
     */
    void set(String key, Object value);

    /**
     * Return the type of the value stored at key in form of a string.
     * @param key The key holding the value.
     * @return one of "none", "string", "int", "list". "none" is returned if the key does not exist.
     */
    String type(String key);

    /**
     * Decrement the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the decrement.
     */
    int decr(String key);

    /**
     * Decrement the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the decrement.
     */
    int decrBy(String key, int integer);

    /**
     * Increment the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the Increment.
     */
    int incr(String key);

    /**
     * Increment the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the Increment.
     */
    int incrBy(String key, int integer);

    /**
     * Remove the specified key. If a given key does not exist no operation is performed for this key.
     * @param key The key to remove.
     * @return True if the key existed and has been removed, false otherwise.
     */
    boolean del(String key);

    /**
     * Return the specified element of the list stored at the specified key.
     * @param key The key holding the list.
     * @param index The index of the value in the list.
     * @return The requested element.
     */
    Object lIndex(String key, int index);

    /**
     * Set the list element at index (see LINDEX for information about the index argument) with the new value.
     * Out of range indexes will generate an error.
     * @param key The key holding the list.
     * @param index The index of the value in the list.
     * @param value The value to set.
     * @return True if we set the value, false otherwise.
     */
    boolean lSet(String key, int index, Object value);

    /**
     * Add the value to the tail of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the tail of the list.
     * @return true if it has been added, false otherwise.
     */
    boolean lPush(String key, Object value);

    /**
     * Add the value to the head of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the head of the list.
     * @return true if it has been added, false otherwise.
     */
    boolean rPush(String key, Object value);

    /**
     * Atomically return and remove the first element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    Object lPop(String key);

    /**
     * Atomically return and remove the last element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    Object rPop(String key);

    /**
     * Return the length of the list stored at the specified key.
     * @param key The key holding the list.
     * @return the length of the list stored at the specified key.
     */
    int lLen(String key);
}
