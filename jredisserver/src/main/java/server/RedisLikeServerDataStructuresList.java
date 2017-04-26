package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface RedisLikeServerDataStructuresList extends Remote {
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
    Object lindex(String key, int index) throws RemoteException;

    /**
     *  Return the length of the list stored at the specified key.
     *  <p>
     *      If the key does not exist zero is returned (the same behaviour as for empty lists).
     *      If the value stored at key is not a list an error is returned.
     *  </p>
     * @param key The key holding the list.
     * @return null if the value stored at key is not a list, the length of the list otherwise.
     */
    int llen(String key) throws RemoteException;

    /**
     * Atomically return and remove the first element of the list.
     * <p>
     *     For example if the list contains the elements "a","b","c" LPOP will return "a" and the list will
     *     become "b","c".
     * </p>
     * @param key The key holding the list.
     * @return null if the key does not exist or the list is already empty, the removed object otherwise.
     */
    Object lpop(String key) throws RemoteException;

    /**
     * Add the value to the tail of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the tail of the list.
     * @return True if we added the value to the list, false if key was not holding a list.
     */
    boolean lpush(String key, Object value) throws RemoteException;

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
    ArrayList<Object> lrange(String key, int start, int end) throws RemoteException;

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
    int lrem(String key, int count, Object value) throws RemoteException;

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
    boolean lset(String key, int index, Object value) throws RemoteException;

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
    boolean ltrim(String key, int start, int end) throws RemoteException;

    /**
     * Add the value to the head of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the head of the list.
     * @return True if we added the value to the list, false if key was not holding a list.
     */
    boolean rpush(String key, Object value) throws RemoteException;

    /**
     * Atomically return and remove the last element of the list.
     * <p>
     *      For example if the list contains the elements "a","b","c" RPOP will return "c" and the list will
     *      become "a","b".
     * </p>
     * @param key The key holding the list.
     * @return null if the key does not exist or the list is already empty, the removed object otherwise.
     */
    Object rpop(String key) throws RemoteException;
}
