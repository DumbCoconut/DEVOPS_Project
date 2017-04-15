package client.requests;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

public class RequestName {
    /**
     * Enumeration of the available commands.
     */
    public enum Cmd {
        QUIT, EXIT, HELP, ADD_SERVER, GET, SET, TYPE, DECR, DECRBY, INCR, INCRBY, DEL;
    }

    /**
     * Map commands to their natural names.
     */
    private HashMap<Cmd, String> cmds;

    /**
     * Constructor.
     */
    private RequestName() {
        cmds = new HashMap<>();
        cmds.put(Cmd.QUIT, "quit");
        cmds.put(Cmd.EXIT, "exit");
        cmds.put(Cmd.HELP, "help");
        cmds.put(Cmd.ADD_SERVER, "add_server");
        cmds.put(Cmd.GET, "get");
        cmds.put(Cmd.SET, "set");
        cmds.put(Cmd.TYPE, "type");
        cmds.put(Cmd.DECR, "decr");
        cmds.put(Cmd.DECRBY, "decrby");
        cmds.put(Cmd.INCR, "incr");
        cmds.put(Cmd.INCRBY, "incrby");
        cmds.put(Cmd.DEL, "del");

        // All commands are upper case
        cmds.replaceAll((k,v) -> v.toUpperCase());
    }

    /**
     * The instance representing the singleton.
     */
    private static RequestName ourInstance = null;

    /**
     * get the singleton's instance.
     * @return The singleton's instance.
     */
    public static RequestName getInstance() {
        if (ourInstance == null) {
            ourInstance = new RequestName();
        }
        return ourInstance;
    }

    /**
     * Get all the commands available.
     * @return A map containing the commands mapped to their natural names.
     */
    public HashMap<Cmd, String> getAllCmds() {
        return cmds;
    }

    /**
     * Get the natural name of the QUIT command.
     * @return The natural name of the QUIT command.
     */
    public String getQuitCmd() {
        return cmds.get(Cmd.QUIT);
    }

    /**
     * Get the natural name of the EXIT command.
     * @return The natural name of the EXIT command.
     */
    public String getExitCmd() {
        return cmds.get(Cmd.EXIT);
    }

    /**
     * Get the natural name of the HELP command.
     * @return The natural name of the HELP command.
     */
    public String getHelpCmd() {
        return cmds.get(Cmd.HELP);
    }

    /**
     * Get the natural name of the ADD_SERVER command.
     * @return The natural name of the ADD_SERVER command.
     */
    public String getAddServerCmd() {
        return cmds.get(Cmd.ADD_SERVER);
    }

    /**
     * Get the natural name of the GET command.
     * @return The natural name of the GET command.
     */
    public String getGetCmd() {
        return cmds.get(Cmd.GET);
    }

    /**
     * Get the natural name of the SET command.
     * @return The natural name of the SET command.
     */
    public String getSetCmd() {
        return cmds.get(Cmd.SET);
    }

    /**
     * Get the natural name of the TYPE command.
     * @return The natural name of the TYPE command.
     */
    public String getTypeCmd() {
        return cmds.get(Cmd.TYPE);
    }

    /**
     * Get the natural name of the DECR command.
     * @return The natural name of the DECR command.
     */
    public String getDecrCmd() {
        return cmds.get(Cmd.DECR);
    }

    /**
     * Get the natural name of the DECRBY command.
     * @return The natural name of the DECRBY command.
     */
    public String getDecrByCmd() {
        return cmds.get(Cmd.DECRBY);
    }

    /**
     * Get the natural name of the INCR command.
     * @return The natural name of the INCR command.
     */
    public String getIncrCmd() {
        return cmds.get(Cmd.INCR);
    }

    /**
     * Get the natural name of the INCRBY command.
     * @return The natural name of the INCRBY command.
     */
    public String getIncrByCmd() {
        return cmds.get(Cmd.INCRBY);
    }

    /**
     * Get the natural name of the DEL command.
     * @return The natural name of the DEL command.
     */
    public String getDelCmd() {
        return cmds.get(Cmd.DEL);
    }

    /**
     * Find the closest match of the given command using Levenshtein distance (e.g "DEL" for "dal".
     * @param cmd The command we want to find a match of.
     * @return the closest match of the given command.
     */
    public String findClosestCmdMatch(String cmd) {
        String cmdToUpper = cmd.toUpperCase();

        HashMap<String, Integer> distances = new HashMap<>();
        for (Map.Entry<Cmd, String> e : cmds.entrySet()) {
            distances.put(e.getValue(), getLevenshteinDistance(cmdToUpper, e.getValue()));
        }
        Object[] entrySet = distances.entrySet().toArray();
        Arrays.sort(entrySet, (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue()
                .compareTo(((Map.Entry<String, Integer>) o1).getValue()));
        return ((Map.Entry<String, Integer>) entrySet[entrySet.length - 1]).getKey().toUpperCase();
    }
}