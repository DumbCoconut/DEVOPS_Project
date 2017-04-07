package server;

import org.apache.commons.cli.*;
import storage.DuplicatedKeyException;
import storage.NonExistentKeyException;
import storage.Storage;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;

public class Server implements RedisLikeServer {
    public static final int DEFAULT_PORT = 9000;
    public static int NUM_SERV = 0;

    private Options opt;
    private CommandLine commandLine;
    private String name;
    private int port;

    private Storage storage;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.parse(args);
            Registry registry = LocateRegistry.getRegistry();
            Remote remote = UnicastRemoteObject.exportObject(server, server.getPort());
            registry.bind(server.getName(), remote);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Parsing failed.  Reason: " + e.getMessage());
            server.help();
        }
    }

    /**
     * Default constructor.
     */
    public Server() {
        initOptions();
        port = DEFAULT_PORT;
        name = "server_" + NUM_SERV++;
        storage = new Storage(10L);
    }

    /**
     * Get the name of this server.
     * @return the name of this server.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the port of this server.
     * @return the port of this server.
     */
    public int getPort() {
        return port;
    }

    /**
     * Initialize the options available in the CLI.
     */
    private void initOptions() {
        opt = new Options();

        Option help = Option.builder("h")
                            .longOpt("help")
                            .hasArg(false)
                            .desc("Display this information")
                            .build();

        Option name = Option.builder("n")
                            .longOpt("name")
                            .hasArg()
                            .desc("Give a name to the server.")
                            .build();

        Option port = Option.builder("p")
                            .longOpt("port")
                            .hasArg()
                            .desc("Port of the server.")
                            .build();

        opt.addOption(help);
        opt.addOption(name);
        opt.addOption(port);
    }

    /**
     * Parse the arguments of the command line and retrieve the various pieces of information.
     * <p>
     *     Note that if the parser does not find any information, no attribute will change,
     *     i.e the name and the port will keep their default values.
     * </p>
     * @param args The arguments of the command line.
     * @throws ParseException If there are any problems encountered while parsing the command line tokens.
     */
    public void parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(opt, args);
        if (commandLine.hasOption("name")) name = getNameFromCommandLine();
        if (commandLine.hasOption("port")) port = getPortFromCommandLine();
    }

    /**
     * Extract the server name from the command line.
     * @return The server name.
     */
    private String getNameFromCommandLine() {
        return commandLine.getOptionValue("name");
    }

    /**
     * Extract the port number from the command line.
     * @return The port number from the command line if valid, DEFAULT_PORT otherwise.
     */
    private int getPortFromCommandLine() {
        try {
            return Integer.parseInt(commandLine.getOptionValue("port"));
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }

    /**
     * Display the help message.
     */
    public void help() {
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
     * Get the value of the specified key.
     * @param key The key we want the value of.
     * @return The value of the key if it exists, null otherwise.
     */
    public Object get(String key) {
        try {
            return storage.get(key);
        } catch (NonExistentKeyException e) {
            return null;
        }
    }

    /**
     * Set key to hold the string value. If key already holds a value, it is overwritten, regardless of its type.
     * @param key The key holding the value.
     * @param value The value to set.
     */
    public void set(String key, Object value) {
        try {
            storage.store(value, key);
        } catch (DuplicatedKeyException e) {
            storage.replace(key, value);
        }
    }

    /**
     * Return the type of the value stored at key in form of a string.
     * @param key The key holding the value.
     * @return one of "none", "string", "int", "list". "none" is returned if the key does not exist.
     */
    public String type(String key) {
        try {
            return storage.get(key).getClass().getSimpleName();
        } catch (NonExistentKeyException e) {
            return "none";
        }
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
    public int decr(String key) {
        return incrBy(key, -1);
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
    public int decrBy(String key, int integer) {
        return incrBy(key, -integer);
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
    public int incr(String key) {
        return incrBy(key, 1);
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
    public int incrBy(String key, int integer) {
        int newValue = integer;
        try {
            Object o = storage.get(key);
            if (o instanceof Integer) {
                newValue += (Integer) o;
                set(key, newValue);
            } else {
                set(key, integer);
            }
        } catch (NonExistentKeyException e) {
            set(key, integer);
        }
        return newValue;
    }

    /**
     * Remove the specified key. If a given key does not exist no operation is performed for this key.
     * @param key The key to remove.
     * @return True if the key existed and has been removed, false otherwise.
     */
    public boolean del(String key) {
        try {
            storage.remove(key);
            return true;
        } catch (NonExistentKeyException e) {
            return false;
        }
    }

    /**
     * Return the specified element of the list stored at the specified key.
     * @param key The key holding the list.
     * @param index The index of the value in the list.
     * @return The requested element.
     */
    public Object lIndex(String key, int index) {
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
    public boolean lSet(String key, int index, Object value) {
        // TODO
        return false;
    }

    /**
     * Add the value to the tail of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the tail of the list.
     * @return true if it has been added, false otherwise.
     */
    public boolean lPush(String key, Object value) {
        // TODO
        return false;
    }

    /**
     * Add the value to the head of the list stored at key.
     * @param key The key holding the list.
     * @param value The value to add to the head of the list.
     * @return true if it has been added, false otherwise.
     */
    public boolean rPush(String key, Object value) {
        // TODO
        return false;
    }

    /**
     * Atomically return and remove the first element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    public Object lPop(String key) {
        // TODO
        return false;
    }

    /**
     * Atomically return and remove the last element of the list.
     * @param key The key holding the list.
     * @return The element that has been removed if it existed, null otherwise.
     */
    public Object rPop(String key) {
        // TODO
        return false;
    }

    /**
     * Return the length of the list stored at the specified key.
     * @param key The key holding the list.
     * @return the length of the list stored at the specified key.
     */
    public int lLen(String key) {
        // TODO
        return 0;
    }
}