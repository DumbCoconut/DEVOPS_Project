package client;

import client.requests.RequestName;
import client.requests.dataTypes.*;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public static final String QUIT     = "quit";
    public static final String EXIT     = "exit";


    private boolean exitRequested;
    private ArrayList<String> tokens;

    public Client() {
        enterLoop();
    }

    public void enterLoop() {
        Scanner terminal = new Scanner(System.in);
        exitRequested = false;
        while (!exitRequested) {
            System.out.print("> ");
            String cmd = terminal.nextLine();
            tokens = splitIntoTokens(cmd);
            parseAndexecuteCmd();
        }
    }

    public void parseAndexecuteCmd() {
        /* early exit */
        if (tokens.isEmpty()) {
            return;
        }

        String cmd = tokens.get(0).toLowerCase();
        switch (cmd) {
            case RequestName.HELP:
                break;
            case QUIT:
            case EXIT:
                exitRequested = true;
                System.out.println("Bye!");
                break;
            case RequestName.GET:
                try {
                    RequestGet r = new RequestGet(tokens);
                    get(r.getKey());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.SET:
                try {
                    RequestSet r = new RequestSet(tokens);
                    set(r.getKey(), r.getObject());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.TYPE:
                try {
                    RequestType r = new RequestType(tokens);
                    type(r.getKey());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                 break;
            case RequestName.DECR:
                try {
                    RequestDecr r = new RequestDecr(tokens);
                    decr(r.getKey());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.DECRBY:
                try {
                    RequestDecrBy r = new RequestDecrBy(tokens);
                    decrBy(r.getKey(), Integer.valueOf(r.getInteger()));
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.INCR:
                try {
                    RequestIncr r = new RequestIncr(tokens);
                    incr(r.getKey());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.INCRBY:
                try {
                    RequestIncrBy r = new RequestIncrBy(tokens);
                    incrBy(r.getKey(), Integer.valueOf(r.getInteger()));
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case RequestName.DEL:
                try {
                    RequestDel r = new RequestDel(tokens);
                    del(r.getKey());
                } catch (InvalidNbArgException | NoTokensException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                System.out.println("(error) I'm sorry, I don't recognize that command.");
                break;
        }
    }

    /**
     * Split a string into tokens. Each word is a token, except if in quotes.
     * <p>
     *     Example: "Hello my name is" will be split into [hello, my, name, is] but
     *     "Hello \"my name is\"" will be split into [hello, my name is].
     *     Note that delimiters like "," are considered part of the word if they are
     *     not separated from it by a space.
     * </p>
     * @param s The string to parse.
     * @return ArrayList containing the tokens.
     */
    public ArrayList<String> splitIntoTokens(String s) {
        ArrayList<String> list = new ArrayList<>();
        /*
         * [^"]     - token starting with something other than "
         * \S*      - followed by zero or more non-space characters
         * ...or...
         * ".+?"    - a "-symbol followed by whatever, until another "
         */
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(s);
        while (m.find()) {
            list.add(m.group(1).replace("\"", ""));
        }
        return list;
    }

    /**
     * Display the help message.
     */
    private void help() {

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