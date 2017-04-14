package client.requests.client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;

import java.util.*;
import java.util.Map.Entry;

import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

public class RequestHelp extends Request {
    ArrayList<String> requests;

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
        requests.addAll(tokens.subList(1, tokens.size()));
        /* sort requests alphabetically */
        requests.sort(String::compareToIgnoreCase);
    }

    public ArrayList<String> getRequests() {
        return requests;
    }

    public String getMessage() {
        if (tokens.size() == 1) {
            return help();
        } else {
            String res = "";
            for (String cmd : requests) {
                if (cmd.equals(RequestName.getInstance().getHelpCmd())) {
                    res += helpHelp() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getQuitCmd()) || cmd.equals(RequestName.getInstance().getExitCmd())) {
                    res += helpQuit() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getAddServerCmd())) {
                    res += helpAddServer() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getGetCmd())) {
                    res += helpGet() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getSetCmd())) {
                    res += helpSet() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getDecrCmd())) {
                    res += helpDecr() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getDecrByCmd())) {
                    res += helpDecrBy() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getIncrCmd())) {
                    res += helpIncr() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getIncrByCmd())) {
                    res += helpIncrBy() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getTypeCmd())) {
                    res += helpType() + "\n";

                } else if (cmd.equals(RequestName.getInstance().getDelCmd())) {
                    res += helpDel() + "\n";

                } else {
                    res += "(error) I'm sorry, I don't recognize the command \"" + cmd + "\". "
                            + "Did you mean \"" + RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?";

                }
            }
            return res;
        }
    }

    private String help() {
        String res =    helpHelp()      + "\n"
                    +   helpQuit()      + "\n"
                    +   helpAddServer() + "\n"
                    +   helpGet()       + "\n"
                    +   helpSet()       + "\n"
                    +   helpDecr()      + "\n"
                    +   helpDecrBy()    + "\n"
                    +   helpIncr()      + "\n"
                    +   helpIncrBy()    + "\n"
                    +   helpType()      + "\n"
                    +   helpDel()       + "\n";

        return res;
    }

    private String helpHelp() {
        return "HELP [cmd1, cmd2, ... cmdN]";
    }

    private String helpQuit() {
        return "QUIT | EXIT";
    }

    private String helpAddServer() {
        return "ADD_SERVER server_ip server_name";
    }

    private String helpGet() {
        return "GET key";
    }

    private String helpSet() {
        return "SET key value";
    }

    private String helpDecr() {
        return "DECR key";
    }

    private String helpDecrBy() {
        return "DECRBY key integer";
    }

    private String helpIncr() {
        return "INCR key";
    }

    private String helpIncrBy() {
        return "INCRBY key integer";
    }

    private String helpType() {
        return "TYPE key";
    }

    private String helpDel() {
        return "DEL key";
    }
}