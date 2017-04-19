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
        QUIT, EXIT, HELP, ADD_SERVER,
        GET, SET, TYPE, DECR, DECRBY, INCR, INCRBY, DEL,
        LINDEX, LLEN, LPOP, LPUSH, LRANGE, LREM, LSET, LTRIM, RPOP, RPUSH,
        SADD, SCARD, SREM, SISMEMBER, SMEMBERS, SINTER
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
        addClientRequests();
        addDataTypesRequests();
        addListRequests();
        addSetRequests();

        // All commands are upper case
        cmds.replaceAll((k,v) -> v.toUpperCase());
    }

    /**
     * Add requests related to the client.
     */
    private void addClientRequests() {
        cmds.put(Cmd.QUIT, "quit");
        cmds.put(Cmd.EXIT, "exit");
        cmds.put(Cmd.HELP, "help");
        cmds.put(Cmd.ADD_SERVER, "add_server");
    }

    /**
     * Add requests related to data types.
     */
    private void addDataTypesRequests() {
        cmds.put(Cmd.GET, "get");
        cmds.put(Cmd.SET, "set");
        cmds.put(Cmd.TYPE, "type");
        cmds.put(Cmd.DECR, "decr");
        cmds.put(Cmd.DECRBY, "decrby");
        cmds.put(Cmd.INCR, "incr");
        cmds.put(Cmd.INCRBY, "incrby");
        cmds.put(Cmd.DEL, "del");
    }

    /**
     * Add requests related to lists.
     */
    private void addListRequests() {
        cmds.put(Cmd.LINDEX, "lindex");
        cmds.put(Cmd.LLEN, "llen");
        cmds.put(Cmd.LPOP, "lpop");
        cmds.put(Cmd.LPUSH, "lpush");
        cmds.put(Cmd.LRANGE, "lrange");
        cmds.put(Cmd.LREM, "lrem");
        cmds.put(Cmd.LSET, "lset");
        cmds.put(Cmd.LTRIM, "ltrim");
        cmds.put(Cmd.RPOP, "rpop");
        cmds.put(Cmd.RPUSH, "rpush");
    }

    /**
     * Add requests related to sets.
     */
    private void addSetRequests() {
        cmds.put(Cmd.SADD, "sadd");
        cmds.put(Cmd.SCARD, "scard");
        cmds.put(Cmd.SREM, "srem");
        cmds.put(Cmd.SISMEMBER, "sismember");
        cmds.put(Cmd.SMEMBERS, "smembers");
        cmds.put(Cmd.SINTER, "sinter");
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
     * Get the natural name of the LINDEX command.
     * @return The natural name of the LINDEX command.
     */
    public String getLIndexCmd() {
        return cmds.get(Cmd.LINDEX);
    }

    /**
     * Get the natural name of the LLEN command.
     * @return The natural name of the LLEN command.
     */
    public String getLLenCmd() {
        return cmds.get(Cmd.LLEN);
    }

    /**
     * Get the natural name of the LPOP command.
     * @return The natural name of the LPOP command.
     */
    public String getLPopCmd() {
        return cmds.get(Cmd.LPOP);
    }

    /**
     * Get the natural name of the LPUSH command.
     * @return The natural name of the LPUSH command.
     */
    public String getLPushCmd() {
        return cmds.get(Cmd.LPUSH);
    }

    /**
     * Get the natural name of the LRANGE command.
     * @return The natural name of the LRANGE command.
     */
    public String getLRangeCmd() {
        return cmds.get(Cmd.LRANGE);
    }

    /**
     * Get the natural name of the LREM command.
     * @return The natural name of the LREM command.
     */
    public String getLRemCmd() {
        return cmds.get(Cmd.LREM);
    }

    /**
     * Get the natural name of the LSET command.
     * @return The natural name of the LSET command.
     */
    public String getLSetCmd() {
        return cmds.get(Cmd.LSET);
    }

    /**
     * Get the natural name of the LTRIM command.
     * @return The natural name of the LTRIM command.
     */
    public String getLTrimCmd() {
        return cmds.get(Cmd.LTRIM);
    }

    /**
     * Get the natural name of the RPOP command.
     * @return The natural name of the RPOP command.
     */
    public String getRPopCmd() {
        return cmds.get(Cmd.RPOP);
    }

    /**
     * Get the natural name of the RPUSH command.
     * @return The natural name of the RPUSH command.
     */
    public String getRPushCmd() {
        return cmds.get(Cmd.RPUSH);
    }

    /**
     * Get the natural name of the SADD command.
     * @return The natural name of the SADD command.
     */
    public String getSAddCmd() {
        return cmds.get(Cmd.SADD);
    }

    /**
     * Get the natural name of the SCARD command.
     * @return The natural name of the SCARD command.
     */
    public String getSCardCmd() {
        return cmds.get(Cmd.SCARD);
    }

    /**
     * Get the natural name of the SREM command.
     * @return The natural name of the SREM command.
     */
    public String getSRemCmd() {
        return cmds.get(Cmd.SREM);
    }

    /**
     * Get the natural name of the SISMEMBER command.
     * @return The natural name of the SISMEMBER command.
     */
    public String getSIsMemberCmd() {
        return cmds.get(Cmd.SISMEMBER);
    }

    /**
     * Get the natural name of the SMEMBERS command.
     * @return The natural name of the SMEMBERS command.
     */
    public String getSMembersCmd() {
        return cmds.get(Cmd.SMEMBERS);
    }

    /**
     * Get the natural name of the SINTER command.
     * @return The natural name of the SINTER command.
     */
    public String getSInterCmd() {
        return cmds.get(Cmd.SINTER);
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