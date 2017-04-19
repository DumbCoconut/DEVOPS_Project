package server;

import org.apache.commons.cli.*;
import storage.exceptions.DuplicatedKeyException;
import storage.exceptions.NonExistentKeyException;
import storage.Storage;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements RedisLikeServer {
    public static final int DEFAULT_PORT = 42933;
    public static final long DEFAULT_CACHE_SIZE = 10L;
    public static final String DEFAULT_NAME = "redis-like";

    private Options opt;
    private CommandLine commandLine;
    private String name;
    private int port;

    private Storage storage;

    public static void main(String[] args) {
        Server server = new Server();
        try {
            boolean help = server.parse(args);
            if (help) {
                server.help();
                System.exit(0);
            }
            Registry registry = LocateRegistry.getRegistry();
            Remote remote = UnicastRemoteObject.exportObject(server, server.getPort());
            registry.bind(server.getName(), remote);
            System.out.println("Hello. I am server \"" + server.getName() + "\" and I'm running on port " + server.getPort());
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Parsing failed. Reason: " + e.getMessage());
            server.help();
        }
    }

    /**
     * Default constructor.
     */
    public Server() {
        initOptions();
        port = DEFAULT_PORT;
        name = DEFAULT_NAME;
        storage = new Storage(DEFAULT_CACHE_SIZE);
    }

    /**
     * Get the name of this server.
     * @return the name of this server.
     */
    public String getName() throws RemoteException {
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
                            .desc("Display this information.")
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
     * @return True if the server should display the help after parsing, false otherwise.
     * @throws ParseException If there are any problems encountered while parsing the command line tokens.
     */
    public boolean parse(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(opt, args);
        if (commandLine.hasOption("help")) return true;
        if (commandLine.hasOption("name")) name = getNameFromCommandLine();
        if (commandLine.hasOption("port")) port = getPortFromCommandLine();
        return false;
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
        System.out.println("Options:");
        System.out.println("\t-h\t--help\tDisplay this information.");
        System.out.println("\t-n\t--name\tSet the name of this server.");
        System.out.println("\t-p\t--port\tSet the port of this server.");
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
            storage.store(key, value);
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

    public Object lindex(String key, int index) {
        return storage.lindex(key, index);
    }

    public int llen(String key) {
        return storage.llen(key);
    }

    public Object lpop(String key) {
        return storage.lPop(key);
    }

    public boolean lpush(String key, Object value) {
        return storage.lPush(key, value);
    }

    public ArrayList<Object> lrange(String key, int start, int end) {
        return storage.lrange(key, start, end);
    }

    public int lrem(String key, int count, Object value) {
        return storage.lrem(key, count, value);
    }

    public boolean lset(String key, int index, Object value) {
        return storage.lset(key, index, value);
    }

    public boolean ltrim(String key, int start, int end) {
        return storage.ltrim(key, start, end);
    }

    public Object rpop(String key) {
        return storage.rPop(key);
    }

    public boolean rpush(String key, Object value) {
        return storage.rPush(key, value);
    }

    public int sadd(String key, Object member) {
        return storage.sadd(key, member);
    }

    public int scard(String key) {
        return storage.scard(key);
    }

    public int srem(String key, Object member) {
        return storage.srem(key, member);
    }

    public int sismember(String key, Object member) {
        return storage.sismember(key, member);
    }

    public List<Object> smembers(String key) {
        return storage.smembers(key);
    }

    public List<Object> sinter(String[] keys) {
        return storage.sinter(keys);
    }

    public int sinterstore(String[] keys) {
        return storage.sinterstore(keys);
    }

    public Object spop(String key) {
        return storage.spop(key);
    }
}