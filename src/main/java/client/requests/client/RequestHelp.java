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
    }

    public String getMessage() {
        if (tokens.size() == 1) {
            return help();
        } else {
            String res = "";
            for (String cmd : requests) {
                switch (cmd) {
                    case RequestName.HELP:
                        res += helpHelp() + "\n";
                        break;
                    case RequestName.QUIT:
                    case RequestName.EXIT:
                        res += helpQuit() + "\n";
                        break;
                    case RequestName.ADD_SERVER:
                        res += helpAddServer() + "\n";
                        break;
                    case RequestName.GET:
                        res += helpGet() + "\n";
                        break;
                    case RequestName.SET:
                        res += helpSet() + "\n";
                        break;
                    case RequestName.DECR:
                        res += helpDecr() + "\n";
                        break;
                    case RequestName.DECRBY:
                        res += helpDecrBy() + "\n";
                        break;
                    case RequestName.INCR:
                        res += helpIncr() + "\n";
                        break;
                    case RequestName.INCRBY:
                        res += helpIncrBy() + "\n";
                        break;
                    case RequestName.TYPE:
                        res += helpType() + "\n";
                        break;
                    case RequestName.DEL:
                        res += helpDel() + "\n";
                        break;
                    default:
                        res += "(error) I'm sorry, I don't recognize the command \"" + cmd + "\". "
                                + "Did you mean \"" + findClosestCmdMatch(cmd) + "\"?";
                        break;
                }
            }
            return res;
        }
    }

    private String findClosestCmdMatch(String cmd) {
        HashMap<String, Integer> distances = new HashMap<>();
        distances.put(RequestName.HELP, getLevenshteinDistance(cmd, RequestName.HELP));
        distances.put(RequestName.QUIT, getLevenshteinDistance(cmd, RequestName.QUIT));
        distances.put(RequestName.ADD_SERVER, getLevenshteinDistance(cmd, RequestName.ADD_SERVER));
        distances.put(RequestName.GET, getLevenshteinDistance(cmd, RequestName.GET));
        distances.put(RequestName.SET, getLevenshteinDistance(cmd, RequestName.SET));
        distances.put(RequestName.DECR, getLevenshteinDistance(cmd, RequestName.DECR));
        distances.put(RequestName.DECRBY, getLevenshteinDistance(cmd, RequestName.DECRBY));
        distances.put(RequestName.INCR, getLevenshteinDistance(cmd, RequestName.INCR));
        distances.put(RequestName.INCRBY, getLevenshteinDistance(cmd, RequestName.INCRBY));
        distances.put(RequestName.TYPE, getLevenshteinDistance(cmd, RequestName.TYPE));
        distances.put(RequestName.DEL, getLevenshteinDistance(cmd, RequestName.DEL));
        Object[] entrySet = distances.entrySet().toArray();
        Arrays.sort(entrySet, (o1, o2) -> ((Entry<String, Integer>) o2).getValue()
                .compareTo(((Entry<String, Integer>) o1).getValue()));
        return ((Entry<String, Integer>) entrySet[entrySet.length - 1]).getKey();
    }

    private String help() {
        String res =    helpHelp()      + "\n"
                    +   helpQuit() + "\n"
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