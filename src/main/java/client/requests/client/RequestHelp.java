package client.requests.client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;

import java.util.*;
import java.util.stream.Collectors;

public class RequestHelp extends Request {
    /**
     * Separate the different blocks (one per request).
     */
    private final String SEPARATOR = "\n--------------------\n";

    /**
     * List containing all the commands where help has been requested.
     */
    ArrayList<String> requests;

    /**
     * Constructor
     * @param tokens The different words of the request, e.g {"help", "get", "set"}.
     * @throws NoTokensException When no tokens are provided to the request.
     */
    public RequestHelp(ArrayList<String> tokens) throws NoTokensException {
        super(tokens);
        requests = new ArrayList<>();
        parse();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return getMessage();
    }

    /**
     * Parse the tokens and retrieve the commands where help has been requested.
     */
    public void parse() {
        /* sublist because we skip "help" */
        /* remove the duplicates */
        /* put everything to lower case */
        List<String> upperCase = tokens.subList(1, tokens.size()).stream()
                                                                 .map(String::toUpperCase)
                                                                 .distinct()
                                                                 .collect(Collectors.toList());

        /* add the sublist as our requests */
        requests.addAll(upperCase);

        /* sort requests alphabetically */
        requests.sort(String::compareToIgnoreCase);
    }

    /**
     * Get the separator.
     * @return The separator.
     */
    public String getSeparator() {
        return SEPARATOR;
    }

    /**
     * Get all the commands where help has been requested.
     * @return all the commands where help has been requested.
     */
    public ArrayList<String> getRequests() {
        return requests;
    }

    /**
     * Get the message containing information about all the commands where help has been requested.
     * @return Help containing information about all the commands where help has been requested.
     */
    public String getMessage() {
        if (tokens.size() == 1) {
            return getHelp();
        }

        // create the help
        // we do not need to sort because requests is already sorted
        ArrayList<String> res = new ArrayList<>();
        for (String cmd : requests) {
            if (cmd.equals(RequestName.getInstance().getHelpCmd())) {
                res.add(getHelpHelp());
            } else if (cmd.equals(RequestName.getInstance().getQuitCmd())) {
                res.add(getHelpQuit());
            } else if (cmd.equals(RequestName.getInstance().getExitCmd())) {
                res.add(getHelpExit());
            } else if (cmd.equals(RequestName.getInstance().getAddServerCmd())) {
                res.add(getHelpAddServer());
            } else if (cmd.equals(RequestName.getInstance().getGetCmd())) {
                res.add(getHelpGet());
            } else if (cmd.equals(RequestName.getInstance().getSetCmd())) {
                res.add(getHelpSet());
            } else if (cmd.equals(RequestName.getInstance().getDecrCmd())) {
                res.add(getHelpDecr());
            } else if (cmd.equals(RequestName.getInstance().getDecrByCmd())) {
                res.add(getHelpDecrBy());
            } else if (cmd.equals(RequestName.getInstance().getIncrCmd())) {
                res.add(getHelpIncr());
            } else if (cmd.equals(RequestName.getInstance().getIncrByCmd())) {
                res.add(getHelpIncrBy());
            } else if (cmd.equals(RequestName.getInstance().getTypeCmd())) {
                res.add(getHelpType());
            } else if (cmd.equals(RequestName.getInstance().getDelCmd())) {
                res.add(getHelpDel());
            } else if (cmd.equals(RequestName.getInstance().getLIndexCmd())) {
                res.add(getHelpLIndex());
            } else if (cmd.equals(RequestName.getInstance().getLLenCmd())) {
                res.add(getHelpLLen());
            } else if (cmd.equals(RequestName.getInstance().getLPopCmd())) {
                res.add(getHelpLPop());
            } else if (cmd.equals(RequestName.getInstance().getLPushCmd())) {
                res.add(getHelpLPush());
            } else if (cmd.equals(RequestName.getInstance().getLRangeCmd())) {
                res.add(getHelpLRange());
            } else if (cmd.equals(RequestName.getInstance().getLRemCmd())) {
                res.add(getHelpLRem());
            } else if (cmd.equals(RequestName.getInstance().getLSetCmd())) {
                res.add(getHelpLSet());
            } else if (cmd.equals(RequestName.getInstance().getLTrimCmd())) {
                res.add(getHelpLTrim());
            } else if (cmd.equals(RequestName.getInstance().getRPopCmd())) {
                res.add(getHelpRPop());
            } else if (cmd.equals(RequestName.getInstance().getRPushCmd())) {
                res.add(getHelpRPush());
            } else if (cmd.equals(RequestName.getInstance().getSAddCmd())) {
                res.add(getHelpSAdd());
            } else if (cmd.equals(RequestName.getInstance().getSCardCmd())) {
                res.add(getHelpSCard());
            } else if (cmd.equals(RequestName.getInstance().getSRemCmd())) {
                res.add(getHelpSRem());
            } else {
                res.add(cmd.toUpperCase() + " : (error) I'm sorry, I don't recognize the command \"" +
                                            cmd.toUpperCase() + "\". " + "Did you mean \"" +
                                            RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?");
            }
        }
        // return the reconstituted string
        return String.join(SEPARATOR, res);
    }

    /**
     * Get the default help message.
     * @return The default help message.
     */
    public String getHelp() {
        // create the help
        ArrayList<String> l = new ArrayList<>();

        // add all the commands
        RequestName.getInstance().getAllCmds().forEach((k,v) -> l.add(v));

        // sort the help by alphabetical order
        l.sort(String::compareTo);

        // return the reconstituted string
        return "Please type HELP for at least one of these commands: " + String.join(", ", l);
    }

    /**
     * Get the help message of HELP.
     * @return The help message of HELP.
     */
    public String getHelpHelp() {
        String res = "";
        res += "HELP [cmd1, cmd2, ... cmdN]" + "\n\n"

            +  "DESCRIPTION: Display the help for the given commands. If no command is provided, display " +
               "the help for all the commands.";

        return res;
    }

    /**
     * Get the help message of EXIT.
     * @return The help message of EXIT.
     */
    public String getHelpExit() {
        String res = "";
        res += "EXIT" + "\n\n"

            +  "DESCRIPTION: Terminate the client. This command is the same as QUIT.";

        return res;
    }

    /**
     * Get the help message of QUIT.
     * @return The help message of QUIT.
     */
    public String getHelpQuit() {
        String res = "";
        res += "QUIT" + "\n\n"

            +  "DESCRIPTION: Terminate the client. This command is the same as EXIT.";

        return res;
    }

    /**
     * Get the help message of ADD_SERVER.
     * @return The help message of ADD_SERVER.
     */
    public String getHelpAddServer() {
        String res = "";
        res += "ADD_SERVER server_ip server_name" + "\n\n"

            +  "DESCRIPTION: Connect to the given server. server_ip is the IP of the server (if local, 127.0.0.1). " +
               "server_name is the name of the server on the host (example: server_0)";

        return res;
    }

    /**
     * Get the help message of GET.
     * @return The help message of GET.
     */
    public String getHelpGet() {
        String res = "";
        res += "GET key" + "\n\n"

            +  "DESCRIPTION: Get the value of the specified key. If the key does not exist " +
               "the special value 'null' is returned.";

        return res;
    }

    /**
     * Get the help message of SET.
     * @return The help message of SET.
     */
    public String getHelpSet() {
        String res = "";
        res += "SET key value" + "\n\n"

            +  "DESCRIPTION: Set key to hold the value. If key already holds a value, it is overwritten, " +
               "regardless of its type.";

        return res;
    }

    /**
     * Get the help message of DECR.
     * @return The help message of DECR.
     */
    public String getHelpDecr() {
        String res = "";
        res += "DECR key" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of DECRBY.
     * @return The help message of DECRBY.
     */
    public String getHelpDecrBy() {
        String res = "";
        res += "DECRBY key integer" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of INCR.
     * @return The help message of INCR.
     */
    public String getHelpIncr() {
        String res = "";
        res += "INCR key" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of INCRBY.
     * @return The help message of INCRBY.
     */
    public String getHelpIncrBy() {
        String res = "";
        res += "INCRBY key integer" + "\n\n"

            +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
               "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
               "before to perform the increment or decrement operation." + "\n\n"

            +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
               "the increment/decrement is integer.";

        return res;
    }

    /**
     * Get the help message of TYPE.
     * @return The help message of TYPE.
     */
    public String getHelpType() {
        String res = "";
        res += "TYPE key" + "\n\n"

            + "Return the type of the value stored at key in form of a string. The type can be one of \"none\", " +
              "\"string\", \"list\", \"set\". \"none\" is returned if the key does not exist.";

        return res;
    }

    /**
     * Get the help message of DEL.
     * @return The help message of DEL.
     */
    public String getHelpDel() {
        String res = "";
        res += "DEL key" + "\n\n"

            +  "DESCRIPTION: Remove the specified keys. If a given key does not exist no operation is performed " +
               "for this key. The command returns the number of keys removed.";

        return res;
    }

    /**
     * Get the help message of LINDEX.
     * @return The help message of LINDEX.
     */
    public String getHelpLIndex() {
        String res = "";
        res += "LINDEX key index" + "\n\n"

             +  "DESCRIPTION: Return the specified element of the list stored at the specified key. 0 is the " +
                "first element, 1 the second and so on." + "\n\n"

             +  "If the value stored at key is not of list type an error is returned. " +
                "If the index is out of range an empty string is returned.";

        return res;
    }

    /**
     * Get the help message of LLEN.
     * @return The help message of LLEN.
     */
    public String getHelpLLen() {
        String res = "";
        res += "LLEN key" + "\n\n"

            +  "DESCRIPTION: Return the length of the list stored at the specified key." +
               " If the key does not exist zero is returned (the same behaviour as for empty lists). " +
               "If the value stored at key is not a list an error is returned.";

        return res;
    }

    /**
     * Get the help message of LPOP.
     * @return The help message of LPOP.
     */
    public String getHelpLPop() {
        String res = "";
        res += "LPOP key" + "\n\n"

            +  "DESCRIPTION: Atomically return and remove the first (LPOP) or last (RPOP) element of the list. " +
               "For example if the list contains the elements \"a\",\"b\",\"c\" LPOP will return \"a\" and the list " +
                "will become \"b\",\"c\".\n" + "\n\n"

            +  "If the key does not exist or the list is already empty the special value 'nil' is returned.";

        return res;
    }

    /**
     * Get the help message of LPUSH.
     * @return The help message of LPUSH.
     */
    public String getHelpLPush() {
        String res = "";
        res += "LPUSH key string" + "\n\n"

            +  "DESCRIPTION: Add the string value to the head (RPUSH) or tail (LPUSH) of the list stored at key. " +
               "If the key does not exist an empty list is created just before the append operation. " +
               "If the key exists but is not a List an error is returned.";

        return res;
    }

    /**
     * Get the help message of LRANGE.
     * @return The help message of LRANGE.
     */
    public String getHelpLRange() {
        String res = "";
        res += "LRANGE key start end" + "\n\n"

            +  "DESCRIPTION: Return the specified elements of the list stored at the specified key. " +
               "Start and end are zero-based indexes. 0 is the first element of the list (the list head), " +
               "1 the next element and so on." + "\n\n"

            +  "For example LRANGE foobar 0 2 will return the first three elements of the list." + "\n\n"

            +  "Indexes out of range will not produce an error: if start is over the end of the list, or start > end," +
               " an empty list is returned. If end is over the end of the list RedisLike will threat it just like the " +
               "last element of the list.";

        return res;
    }

    /**
     * Get the help message of LREM.
     * @return The help message of LREM.
     */
    public String getHelpLRem() {
        String res = "";
        res += "LREM key count value" + "\n\n"

            +  "DESCRIPTION: Remove the first count occurrences of the value element from the list. If count is zero " +
               "all the elements are removed. " + "\n\n"

            + "Note that non existing keys are considered like empty lists by LREM, so LREM against non existing " +
              "keys will always return 0.";

        return res;
    }

    /**
     * Get the help message of LSET.
     * @return The help message of LSET.
     */
    public String getHelpLSet() {
        String res = "";
        res += "LSET key index value" + "\n\n"

            +  "DESCRIPTION: Set the list element at index with the new value.";

        return res;
    }

    /**
     * Get the help message of LTRIM.
     * @return The help message of LTRIM.
     */
    public String getHelpLTrim() {
        String res = "";
        res += "LTRIM key start end" + "\n\n"

            +  "Trim an existing list so that it will contain only the specified range of elements specified. Start" +
               " and end are zero-based indexes. 0 is the first element of the list (the list head), " +
               "1 the next element and so on." + "\n\n"

            +  "For example LTRIM foobar 0 2 will modify the list stored at foobar key so that only the first three " +
               "elements of the list will remain." + "\n\n"

            +  "Indexes out of range will not produce an error: if start is over the end of the list, or start > end," +
               " an empty list is left as value. If end over the end of the list Redis will threat it just like the " +
               "last element of the list.";

        return res;
    }

    /**
     * Get the help message of RPOP.
     * @return The help message of RPOP.
     */
    public String getHelpRPop() {
        String res = "";
        res += "RPOP key" + "\n\n"

                +  "DESCRIPTION: Atomically return and remove the first (LPOP) or last (RPOP) element of the list. " +
                "For example if the list contains the elements \"a\",\"b\",\"c\" LPOP will return \"a\" and the list " +
                "will become \"b\",\"c\".\n" + "\n\n"

                +  "If the key does not exist or the list is already empty the special value 'nil' is returned.";

        return res;
    }

    /**
     * Get the help message of RPUSH.
     * @return The help message of RPUSH.
     */
    public String getHelpRPush() {
        String res = "";
        res += "RPUSH key string" + "\n\n"

            +  "DESCRIPTION: Add the string value to the head (RPUSH) or tail (LPUSH) of the list stored at key. " +
               "If the key does not exist an empty list is created just before the append operation. " +
               "If the key exists but is not a List an error is returned.";

        return res;
    }

    /**
     * Get the help message of SADD.
     * @return The help message of SADD.
     */
    public String getHelpSAdd() {
        String res = "";
        res += "SADD key member" + "\n\n"

            +  "DESCRIPTION: Add the specified member to the set value stored at key. If member is already a member " +
               "of the set no operation is performed. If key does not exist a new set with the specified member as " +
               "sole member is created. If the key exists but does not hold a set value an error is returned.";

        return res;
    }

    /**
     * Get the help message of SADD.
     * @return The help message of SADD.
     */
    public String getHelpSCard() {
        String res = "";
        res += "SCARD key" + "\n\n"

            +  "DESCRIPTION: Return the set cardinality (number of elements). If the key does not exist 0 is returned" +
               ", like for empty sets.";

        return res;
    }

    /**
     * Get the help message of SREM.
     * @return The help message of SREM.
     */
    public String getHelpSRem() {
        String res = "";
        res += "SREM key member" + "\n\n"

            +  "DESCRIPTION: Remove the specified member from the set value stored at key. If member was not " +
               "a member of the set no operation is performed. If key does not hold a set value an error is returned.";

        return res;
    }
}