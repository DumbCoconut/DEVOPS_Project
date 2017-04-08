package client;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Collection;

public class Client {
    Options opt;

    public Client() {
        
    }

    /**
     * Display the help message.
     */
    private void help() {
        // TODO : fix display to be more constant.
        System.out.println("Help");
        Collection<Option> options = opt.getOptions();
        for (Option o : options) {
            if (o.hasLongOpt()) {
                System.out.println("-" + o.getOpt() + "\t\t--" + o.getLongOpt() + "\t\t" + o.getDescription());
            } else {
                System.out.println("-" + o.getOpt() + "\t\t\t\t" + o.getDescription());
            }
        }
    }

    /**
     * Test if the specified key exists.
     * @param key The key to test.
     * @return True if the key exists, false otherwise.
     */
    private boolean exists(String key) {
        // TODO
        return false;
    }

    /**
     * Get the value of the specified key.
     * @param key The key we want the value of.
     * @return The value of the key if it exists, null otherwise.
     */
    private Object get(String key) {
        // TODO
        return null;
    }

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * @param key The key holding the value.
     * @param value The value to set.
     * @return True if we set the value, false otherwise.
     */
    private boolean set(String key, Object value) {
        // TODO
        return false;
    }

    /**
     * Return the type of the value stored at key in form of a string.
     * @param key The key holding the value.
     * @return one of "none", "string", "int", "list". "none" is returned if the key does not exist.
     */
    private String type(String key) {
        // TODO
        return null;
    }

    /**
     * Decrement the number stored at key by one.
     * @param key The key holding the value.
     * @return the new value of key after the decrement.
     */
    private int decr(String key) {
        // TODO
        return 0;
    }

    /**
     * Decrement the number stored at key by one.
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the decrement.
     */
    private int decrBy(String key, int integer) {
        // TODO
        return 0;
    }

    /**
     * Increment the number stored at key by one.
     * @param key The key holding the value.
     * @return the new value of key after the Increment.
     */
    private int incr(String key) {
        // TODO
        return 0;
    }

    /**
     * Increment the number stored at key by one.
     * @param key The key holding the value.
     * @param integer The increment value.
     * @return the new value of key after the Increment.
     */
    private int incrBy(String key, int integer) {
        // TODO
        return 0;
    }

    /**
     * Remove the specified key. If a given key does not exist no operation is performed for this key.
     * @param key The key to remove.
     * @return True if the key existed and has been removed, false otherwise.
     */
    private boolean del(String key) {
        // TODO
        return false;
    }

    /**
     * Return the specified element of the list stored at the specified key.
     * @param key The key holding the list.
     * @param index The index of the value in the list.
     * @return The requested element.
     */
    private Object lIndex(String key, int index) {
        // TODO
        return null;
    }

    /**
     * Set the list element at index (see LINDEX for information about the index argument) with the new value.
     * Out of range indexes will generate an error.
     * @param key The key holding the list.
     * @param index The index of the value in the list.
     * @param value The value to set.
     * @return True if we set the value, false otherwise.
     */
    private boolean lSet(String key, int index, Object value) {
        // TODO
        return false;
    }

    /**
     * Add the value to the tail of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the tail of the list.
     * @return true if it has been added, false otherwise.
     */
    private boolean lPush(String key, Object value) {
        // TODO
        return false;
    }

    /**
     * Add the value to the head of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the head of the list.
     * @return true if it has been added, false otherwise.
     */
    private boolean rPush(String key, Object value) {
        // TODO
        return false;
    }

    /**
     * Atomically return and remove the first element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    private Object lPop(String key) {
        // TODO
        return false;
    }

    /**
     * Atomically return and remove the last element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    private Object rPop(String key) {
        // TODO
        return false;
    }

    /**
     * Return the length of the list stored at the specified key.
     * @param key The key holding the list.
     * @return the length of the list stored at the specified key.
     */
    private int lLen(String key) {
        // TODO
        return 0;
    }
}