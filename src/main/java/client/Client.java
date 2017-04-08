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
     */
    private void set(String key, Object value) {
        // TODO
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
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the decrement.
     */
    private int decr(String key) {
        // TODO
        return 0;
    }

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
    private int decrBy(String key, int integer) {
        // TODO
        return 0;
    }

    /**
     * Increment the number stored at key by one.
     * <p>
     *     If the key does not exist or contains a value of a wrong type, set the key to the value of "0"
     *     before to perform the increment or decrement operation.
     * </p>
     * @param key The key holding the value.
     * @return the new value of key after the Increment.
     */
    private int incr(String key) {
        // TODO
        return 0;
    }

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
}