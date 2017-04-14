package client.requests.client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;

import java.util.*;
import java.util.stream.Collectors;

public class RequestHelp extends Request {
    ArrayList<String> requests;

    private final String SEPARATOR = "\n--------------------\n";

    public RequestHelp(ArrayList<String> tokens) throws NoTokensException {
        super(tokens);
        requests = new ArrayList<>();
        parse();
    }

    @Override
    public String toString() {
        return getMessage();
    }

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

    public String getSeparator() {
        return SEPARATOR;
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

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
            } else {
                res.add(cmd.toUpperCase() + " : (error) I'm sorry, I don't recognize the command \"" +
                                            cmd.toUpperCase() + "\". " + "Did you mean \"" +
                                            RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?");
            }
        }

        // return the reconstituted string
        return String.join(SEPARATOR, res);
    }

    public String getHelp() {
        // create the help
        ArrayList<String> l = new ArrayList<>();
        l.add(getHelpHelp());
        l.add(getHelpQuit());
        l.add(getHelpExit());
        l.add(getHelpAddServer());
        l.add(getHelpGet());
        l.add(getHelpSet());
        l.add(getHelpDecr());
        l.add(getHelpDecrBy());
        l.add(getHelpIncr());
        l.add(getHelpIncrBy());
        l.add(getHelpType());
        l.add(getHelpDel());

        // sort the help by alphabetical order
        l.sort(String::compareTo);

        // return the reconstituted string
        return String.join(SEPARATOR, l);
    }

    public String getHelpHelp() {
        String res = "";
        res += "HELP [cmd1, cmd2, ... cmdN]" + "\n\n"

            +  "DESCRIPTION: TODO";

        return res;
    }

    public String getHelpExit() {
        String res = "";
        res += "EXIT" + "\n\n"

            +  "DESCRIPTION: TODO";

        return res;
    }

    public String getHelpQuit() {
        String res = "";
        res += "QUIT" + "\n\n"

            +  "DESCRIPTION: TODO";

        return res;
    }

    public String getHelpAddServer() {
        String res = "";
        res += "ADD_SERVER server_ip server_name" + "\n\n"

            +  "DESCRIPTION: TODO";

        return res;
    }

    public String getHelpGet() {
        String res = "";
        res += "GET key" + "\n\n"

            +  "DESCRIPTION: Get the value of the specified key. If the key does not exist " +
               "the special value 'null' is returned.";

        return res;
    }

    public String getHelpSet() {
        String res = "";
        res += "SET key value" + "\n\n"

            +  "DESCRIPTION: Set key to hold the value. If key already holds a value, it is overwritten, " +
               "regardless of its type.";

        return res;
    }

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

    public String getHelpType() {
        String res = "";
        res += "TYPE key" + "\n\n"

            + "Return the type of the value stored at key in form of a string. The type can be one of \"none\", " +
              "\"string\", \"list\", \"set\". \"none\" is returned if the key does not exist.";

        return res;
    }

    public String getHelpDel() {
        String res = "";
        res += "DEL key" + "\n\n"

            +  "DESCRIPTION: Remove the specified keys. If a given key does not exist no operation is performed " +
               "for this key. The command returns the number of keys removed.";

        return res;
    }
}