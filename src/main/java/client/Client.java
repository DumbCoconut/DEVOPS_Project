package client;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Collection;

public class Client {
    Options opt;

    public Client() {
        initOptions();
    }

    /**
     * Display the help message.
     */
    private void help() {
        // TODO : fix display
        System.out.println("Help");
        Collection<Option> options = opt.getOptions();
        for (Option o : options) {
                System.out.println(o.getOpt() + "\t" + "\t" + o.getDescription());
        }
    }

    /**
     * Terminates the client.
     */
    private void quit() {
        System.exit(0);
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

    /**
     * Initialize the options available in the CLI.
     */
    private void initOptions() {
        opt = new Options();
        addBasicOptions();
        addSDataTypesOptions();
        addListOptions();
    }

    /**
     * Add options related to the program, e.g help.
     */
    private void addBasicOptions() {
        Option help = Option.builder("h")
                            .longOpt("help")
                            .hasArg(false)
                            .desc("Display this information")
                            .build();

        Option quit = Option.builder("q")
                            .longOpt("quit")
                            .hasArg(false)
                            .desc("Quit this client")
                            .build();

        opt.addOption(help);
        opt.addOption(quit);
    }

    /**
     * Add options related to data types.
     */
    private void addSDataTypesOptions() {
        Option exists   = Option.builder("a")
                .hasArg()
                .desc("Test if the specified key exists.")
                .build();

        Option get      = Option.builder("GET")
                .hasArg()
                .desc("Get the value of the specified key.")
                .build();

        Option set      = Option.builder("GET")
                .hasArgs()
                .numberOfArgs(2)
                .desc("Set key to hold the string value.")
                .build();

        Option type     = Option.builder("TYPE")
                .hasArg()
                .desc("Return the type of the value stored at key in form of a string.")
                .build();

        Option decr     = Option.builder("DECR")
                .hasArg()
                .desc("Decrement the number stored at key by one.")
                .build();

        Option decrBy   = Option.builder("DECRBY")
                .hasArgs()
                .numberOfArgs(2)
                .desc("Decrement the number stored at key by an integer.")
                .build();

        Option incr     = Option.builder("INCR")
                .hasArg()
                .desc("Increment the number stored at key by one.")
                .build();

        Option incrBy   = Option.builder("INCRBY")
                .hasArgs()
                .numberOfArgs(2)
                .desc("Increment the number stored at key by an integer.")
                .build();

        Option del      = Option.builder("DEL")
                .hasArg()
                .desc("Remove the specified key.")
                .build();

        opt.addOption(exists);
        opt.addOption(get);
        opt.addOption(set);
        opt.addOption(type);
        opt.addOption(decr);
        opt.addOption(decrBy);
        opt.addOption(incr);
        opt.addOption(incrBy);
        opt.addOption(del);
    }

    /**
     * Add options related to list data structure.
     */
    private void addListOptions()  {
        Option lIndex   = Option.builder("LINDEX")
                                .hasArgs()
                                .numberOfArgs(2)
                                .desc("Return the specified element of the list stored at the specified key.")
                                .build();

        Option lSet     = Option.builder("LSET")
                                .hasArgs()
                                .numberOfArgs(3)
                                .desc("Set the list element at index with the new value.")
                                .build();

        Option lPush    = Option.builder("LPUSH")
                                .hasArgs()
                                .numberOfArgs(2)
                                .desc("Add the string value to the tail of the list stored at key.")
                                .build();

        Option rPush    = Option.builder("RPUSH")
                                .hasArgs()
                                .numberOfArgs(2)
                                .desc("Add the string value to the head of the list stored at key.")
                                .build();

        Option lPop     = Option.builder("LPOP")
                                .hasArg()
                                .desc("Atomically return and remove the first element of the list.")
                                .build();

        Option rPop     = Option.builder("RPOP")
                                .hasArg()
                                .desc("Atomically return and remove the last element of the list.")
                                .build();

        Option lLen     = Option.builder("LLEN")
                                .hasArg()
                                .desc("Return the length of the list stored at the specified key.")
                                .build();

        opt.addOption(lIndex);
        opt.addOption(lSet);
        opt.addOption(lPush);
        opt.addOption(rPush);
        opt.addOption(lPop);
        opt.addOption(rPop);
        opt.addOption(lLen);
    }
}