package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RedisLikeServerDataStructuresSet extends Remote {
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
    int sadd(String key, Object member) throws RemoteException;

    /**
     * Return the set cardinality (number of elements). If the key does not exist 0 is returned, like for empty sets.
     * @param key The key holding the set.
     * @return Cardinality of the set, -1 if key holds anything but a set.
     */
    int scard(String key) throws RemoteException;

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
    int srem(String key, Object member) throws RemoteException;

    /**
     * Return 1 if member is a member of the set stored at key, otherwise 0 is returned.
     * @param key The key holding the set.
     * @param member The member to test.
     * @return 1 if the element is a member of the set, 0 if it is not a member, -1 if an error happened.
     */
    int sismember(String key, Object member) throws RemoteException;

    /**
     *  Return all the members (elements) of the set value stored at key. This is just syntax glue for SINTERSECT.
     * @param key The key holding the set.
     * @return All the members (elements) of the set value stored at key, null if key does not hold a set.
     */
    List<Object> smembers(String key) throws RemoteException;

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
    List<Object> sinter(String[] keys) throws RemoteException;

    /**
     * This command works exactly like SINTER but instead of being returned the resulting set is stored as dstkey.
     * @param keys The keys holding the sets.
     * @return 1 if sinter succeed, -1 if at least one key does not hold a set.
     */
    int sinterstore(String[] keys) throws RemoteException;

    /**
     * Remove a random element from a Set returning it as return value.
     * <p>
     *     If the Set is empty or the key does not exist, a null object is returned.
     *     The SRANDMEMBER command does a similar work but the returned element is not removed from the Set.
     * </p>
     * @param key The key holding the set.
     * @return The removed object, null if key does not exist or is not holding a set.
     */
    Object spop(String key) throws RemoteException;

    /**
     * Return a random element from a Set, without removing the element.
     * <p>
     *     If the Set is empty or the key does not exist, a null object is returned.
     *     The SPOP command does a similar work but the returned element is popped (removed) from the Set.
     * </p>
     * @param key The key holding the set.
     * @return The random object, null if key does not exist or is not holding a set.
     */
    Object srandmember(String key) throws RemoteException;

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
    int smove(String srckey, String dstkey, Object member) throws RemoteException;

    /**
     * Return the members of a set resulting from the union of all the sets hold at the specified keys.
     * <p>
     *      If just a single key is specified, then this command produces the same result as SMEMBERS.
     *      Non existing keys are considered like empty sets.
     * </p>
     * @param keys The keys holding the sets.
     * @return the members resulting from the union of all the sets, null if at least one key does not hold a set.
     */
    List<Object> sunion(String[] keys) throws RemoteException;

    /**
     * This command works exactly like SUNION but instead of being returned the resulting set is stored as dstkey.
     * <p>
     *     Any existing value in dstkey will be over-written.
     * </p>
     * @param keys The keys holding the sets.
     * @return 1 if sunion succeed, -1 if at least one key does not hold a set.
     */
    int sunionstore(String[] keys) throws RemoteException;

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
    List<Object> sdiff(String[] keys) throws RemoteException;

    /**
     * This command works exactly like SDIFF but instead of being returned the resulting set is stored in dstkey.
     * @param keys The keys holding the sets.
     * @return 1 if sdiff succeed, -1 if at least one key does not hold a set.
     */
    int sdiffstore(String[] keys) throws RemoteException;
}