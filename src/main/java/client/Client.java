package client;

import client.requests.RequestName;
import client.requests.client.RequestAddServer;
import client.requests.client.RequestHelp;
import client.requests.dataStructures.list.*;
import client.requests.dataTypes.*;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;
import server.RedisLikeServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    private RedisLikeServer server;
    private Registry registry;
    private String serverIp;
    private String serverName;

    private boolean exitRequested;
    private ArrayList<String> tokens;

    public static void main(String[] args) {
        Client c = new Client();
        c.enterLoop();
    }

    public Client() {}

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

        String cmd = tokens.get(0).toUpperCase();

        if (cmd.equals(RequestName.getInstance().getHelpCmd())) {
            doHelp();
        } else if (cmd.equals(RequestName.getInstance().getQuitCmd()) || cmd.equals(RequestName.getInstance().getExitCmd())) {
            doExit();
        } else if (cmd.equals(RequestName.getInstance().getAddServerCmd())) {
            doAddServer();
        } else if (cmd.equals(RequestName.getInstance().getGetCmd())) {
            doGet();
        } else if (cmd.equals(RequestName.getInstance().getSetCmd())) {
            doSet();
        } else if (cmd.equals(RequestName.getInstance().getTypeCmd())) {
            doType();
        } else if (cmd.equals(RequestName.getInstance().getDecrCmd())) {
            doDecr();
        } else if (cmd.equals(RequestName.getInstance().getDecrByCmd())) {
            doDecrBy();
        } else if (cmd.equals(RequestName.getInstance().getIncrCmd())) {
            doIncr();
        } else if (cmd.equals(RequestName.getInstance().getIncrByCmd())) {
            doIncrBy();
        } else if (cmd.equals(RequestName.getInstance().getDelCmd())) {
            doDel();
        } else if (cmd.equals(RequestName.getInstance().getLIndexCmd())) {
            doLindex();
        } else if (cmd.equals(RequestName.getInstance().getLLenCmd())) {
            doLLen();
        } else if (cmd.equals(RequestName.getInstance().getLPopCmd())) {
            doLPop();
        } else if (cmd.equals(RequestName.getInstance().getLPushCmd())) {
            doLPush();
        } else if (cmd.equals(RequestName.getInstance().getLRangeCmd())) {
            doLRange();
        } else if (cmd.equals(RequestName.getInstance().getLRemCmd())) {
            doLRem();
        } else if (cmd.equals(RequestName.getInstance().getLSetCmd())) {
            doLSet();
        } else if (cmd.equals(RequestName.getInstance().getLTrimCmd())) {
            doLTrim();
        } else if (cmd.equals(RequestName.getInstance().getRPopCmd())) {
            doRPop();
        } else if (cmd.equals(RequestName.getInstance().getRPushCmd())) {
            doRPush();
        } else {
            doUndefinedCmd(cmd);
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

    private void doHelp() {
        try {
            RequestHelp r = new RequestHelp(tokens);
            System.out.println(r.getMessage());
        } catch (NoTokensException e) {
            System.out.println(e.getMessage());
        }
    }

    private void doExit() {
        exitRequested = true;
        System.out.println("Bye!");
    }

    private void doAddServer() {
        if (! shouldWeDisconnectFromCurrentServer()) {
            return;
        }

        try {
            RequestAddServer r = new RequestAddServer(tokens);
            serverIp = r.getIp();
            serverName = r.getName();
            addServer(serverIp, serverName);
        } catch (InvalidNbArgException | NoTokensException e) {
            System.out.println(e.getMessage());
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private boolean shouldWeDisconnectFromCurrentServer() {
        if (server != null) {
            System.out.println(
                    "You are already connected to a server (" + serverName + "@" + serverIp + "). " +
                    "This client can only handle one server at a time.\n" +
                    "Are you sure you want to exit the current server and connect to the new one? (yes/no)");
            Scanner terminal = new Scanner(System.in);
            boolean answered = false;
            boolean shouldConnectAnw = true;
            while (!answered) {
                String answer = terminal.nextLine().toLowerCase();
                switch (answer) {
                    case "no":
                        shouldConnectAnw = false;
                        answered = true;
                        break;
                    case "yes":
                        shouldConnectAnw = true;
                        answered = true;
                        break;
                    default:
                        System.out.println("Please answer \"yes\" or \"no\".");
                        break;
                }
            }
            return shouldConnectAnw;
        } else {
            return true;
        }
    }

    private void doGet() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestGet r = new RequestGet(tokens);
                System.out.println(get(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doSet() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestSet r = new RequestSet(tokens);
                set(r.getKey(), r.getObject());
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doType() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestType r = new RequestType(tokens);
                System.out.println(type(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDecr() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDecr r = new RequestDecr(tokens);
                System.out.println(decr(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDecrBy() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDecrBy r = new RequestDecrBy(tokens);
                System.out.println(decrBy(r.getKey(), Integer.valueOf(r.getInteger())));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doIncr() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestIncr r = new RequestIncr(tokens);
                System.out.println(incr(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doIncrBy() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestIncrBy r = new RequestIncrBy(tokens);
                System.out.println(incrBy(r.getKey(), Integer.valueOf(r.getInteger())));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doDel() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestDel r = new RequestDel(tokens);
                System.out.println(del(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLindex() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLIndex r = new RequestLIndex(tokens);
                System.out.println(lindex(r.getKey(), r.getIndex()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLLen() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLLen r = new RequestLLen(tokens);
                System.out.println(llen(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLPop() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLPop r = new RequestLPop(tokens);
                System.out.println(lpop(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLPush() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLPush r = new RequestLPush(tokens);
                System.out.println(lpush(r.getKey(), r.getString()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLRange() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLRange r = new RequestLRange(tokens);
                System.out.println(lrange(r.getKey(), r.getStart(), r.getEnd()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLRem() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLRem r = new RequestLRem(tokens);
                System.out.println(lrem(r.getKey(), r.getCount(), r.getValue()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLSet() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLSet r = new RequestLSet(tokens);
                System.out.println(lset(r.getKey(), r.getIndex(), r.getValue()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doLTrim() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestLTrim r = new RequestLTrim(tokens);
                System.out.println(ltrim(r.getKey(), r.getStart(), r.getEnd()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doRPop() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestRPop r = new RequestRPop(tokens);
                System.out.println(rpop(r.getKey()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doRPush() {
        if (!isServerSet()) {
            printServerNotSet();
        } else {
            try {
                RequestRPush r = new RequestRPush(tokens);
                System.out.println(rpush(r.getKey(), r.getString()));
            } catch (InvalidNbArgException | NoTokensException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void doUndefinedCmd(String cmd) {
        System.out.println("(error) I'm sorry, I don't recognize that command. "
                + "Did you mean \"" + RequestName.getInstance().findClosestCmdMatch(cmd) + "\"?");
    }

    private boolean isServerSet() {
        return !(server == null);
    }

    private void addServer(String ip, String name) throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry(ip);
        server = (RedisLikeServer) registry.lookup(name);
    }

    private void printServerNotSet() {
        System.out.println("Server is not set. Please add a server. Type \"help add_server\" if you need help.");
    }

    private Object get(String key) {
        try {
            return server.get(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void set(String key, Object value) {
        try {
            server.set(key, value);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private String type(String key) {
        try {
            return server.type(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return "";
        }
    }

    private int decr(String key) {
        try {
            return server.decr(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int decrBy(String key, int integer) {
        try {
            return server.decrBy(key, integer);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int incr(String key) {
        try {
            return server.incr(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int incrBy(String key, int integer) {
        try {
            return server.incrBy(key, integer);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private boolean del(String key) {
        try {
            return server.del(key);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String lindex(String key, String index) {
        try {
            int realIndex = Integer.parseInt(index);
            Object o = server.lindex(key, realIndex);
            if (o == null) {
                return "(nil)";
            } else if (o.equals("")) {
                return "(empty string)";
            } else {
                return o.toString();
            }
        } catch (NumberFormatException e) {
            return "(error) value is not an integer";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String llen(String key) {
        try {
            int len = server.llen(key);
            return len >= 0 ? String.valueOf(len) : "(error) not a list";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lpop(String key) {
        try {
            Object o = server.lpop(key);
            if (o == null) {
                return "(nil)";
            } else {
                return o.toString();
            }
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lpush(String key, Object value) {
        try {
            return server.lpush(key, value) ? "OK" : "(error) Operation against a key holding the wrong kind of value";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lrange(String key, String start, String end) {
        try {
            int realStart = Integer.parseInt(start);
            int realEnd = Integer.parseInt(end);
            ArrayList<Object> objects = server.lrange(key, realStart, realEnd);
            if (objects == null) {
                return "(error) Operation against a key holding the wrong kind of value";
            } else if (objects.isEmpty()) {
                return ("(empty list)");
            } else {
                int len = objects.size();
                String res = "";
                for (int i = 0; i < len; i++) {
                    res += (i + 1) + ") " + objects.get(i).toString() + "\n";
                }
                return res;
            }
        } catch (NumberFormatException e) {
            return "(error) value is not an integer";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lrem(String key, String count, String value) {
        try {
            int realCount = Integer.parseInt(count);
            return String.valueOf(server.lrem(key, realCount, value));
        } catch (NumberFormatException e) {
            return "(error) value is not an integer";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lset(String key, String index, String value) {
        try {
            int realIndex = Integer.parseInt(index);
            return server.lset(key, realIndex, value) ? "OK" : "NOT OK";
        } catch (NumberFormatException e) {
            return "(error) value is not an integer";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String ltrim(String key, String start, String end) {
        try {
            int realStart = Integer.parseInt(start);
            int realEnd = Integer.parseInt(end);
            return server.ltrim(key, realStart, realEnd) ? "OK" : "NOT OK";
        } catch (NumberFormatException e) {
            return "(error) value is not an integer";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String rpush(String key, Object value) {
        try {
            return server.rpush(key, value) ? "OK" : "(error) Operation against a key holding the wrong kind of value";
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String rpop(String key) {
        try {
            Object o = server.rpop(key).toString();
            if (o == null) {
                return "(nil)";
            } else {
                return o.toString();
            }
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }
}