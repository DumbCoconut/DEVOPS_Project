package client;

import client.requests.RequestName;
import client.requests.client.RequestADDSERVER;
import client.requests.client.RequestHELP;
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
    private String serverIp;
    private String serverName;

    private boolean exitRequested;
    private ArrayList<String> tokens;

    private final String OK = "OK";
    private final String NOT_OK = "NOT OK";
    private final String EMPTY_STRING = "(empty string)";
    private final String EMPTY_LIST = "(empty list)";
    private final String ERROR_PARSE_INT = "(error) value is not an integer";
    private final String ERROR_WRONG_TYPE = "(error) Operation dagainst a key holding the wrong kind of value";
    private final String ERROR_NOT_LIST = "(error) not a list";
    private final String NIL = "(nil)";

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
            RequestHELP r = new RequestHELP(tokens);
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
            RequestADDSERVER r = new RequestADDSERVER(tokens);
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
                RequestGET r = new RequestGET(tokens);
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
                RequestSET r = new RequestSET(tokens);
                System.out.println(set(r.getKey(), r.getObject()));
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
                RequestTYPE r = new RequestTYPE(tokens);
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
                RequestDECR r = new RequestDECR(tokens);
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
                RequestDECRBY r = new RequestDECRBY(tokens);
                System.out.println(decrBy(r.getKey(), r.getInteger()));
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
                RequestINCR r = new RequestINCR(tokens);
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
                RequestINCRBY r = new RequestINCRBY(tokens);
                System.out.println(incrBy(r.getKey(), r.getInteger()));
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
                RequestDEL r = new RequestDEL(tokens);
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
                RequestLINDEX r = new RequestLINDEX(tokens);
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
                RequestLLEN r = new RequestLLEN(tokens);
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
                RequestLPOP r = new RequestLPOP(tokens);
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
                RequestLPUSH r = new RequestLPUSH(tokens);
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
                RequestLRANGE r = new RequestLRANGE(tokens);
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
                RequestLREM r = new RequestLREM(tokens);
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
                RequestLSET r = new RequestLSET(tokens);
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
                RequestLTRIM r = new RequestLTRIM(tokens);
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
                RequestRPOP r = new RequestRPOP(tokens);
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
                RequestRPUSH r = new RequestRPUSH(tokens);
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
        Registry registry = LocateRegistry.getRegistry(ip);
        server = (RedisLikeServer) registry.lookup(name);
    }

    private void printServerNotSet() {
        System.out.println("Server is not set. Please add a server. Type \"help add_server\" if you need help.");
    }

    private String get(String key) {
        try {
            Object o = server.get(key);
            return o != null ? o.toString() : NIL;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String set(String key, Object value) {
        try {
            server.set(key, value);
            return OK;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String type(String key) {
        try {
            return server.type(key);
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String decr(String key) {
        try {
            return String.valueOf(server.decr(key));
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String decrBy(String key, String integer) {
        try {
            int realInteger = Integer.parseInt(integer);
            return String.valueOf(server.decrBy(key, realInteger));
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String incr(String key) {
        try {
            return String.valueOf(server.incr(key));
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String incrBy(String key, String integer) {
        try {
            int realInteger = Integer.parseInt(integer);
            return String.valueOf(server.incrBy(key, realInteger));
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String del(String key) {
        try {
            return server.del(key) ? OK : NOT_OK;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lindex(String key, String index) {
        try {
            int realIndex = Integer.parseInt(index);
            Object o = server.lindex(key, realIndex);
            if (o == null) {
                return NIL;
            } else if (o.equals("")) {
                return EMPTY_STRING;
            } else {
                return o.toString();
            }
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String llen(String key) {
        try {
            int len = server.llen(key);
            return len >= 0 ? String.valueOf(len) : ERROR_NOT_LIST;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lpop(String key) {
        try {
            Object o = server.lpop(key);
            if (o == null) {
                return NIL;
            } else {
                return o.toString();
            }
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lpush(String key, Object value) {
        try {
            return server.lpush(key, value) ? OK : ERROR_WRONG_TYPE;
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
                return ERROR_WRONG_TYPE;
            } else if (objects.isEmpty()) {
                return EMPTY_LIST;
            } else {
                int len = objects.size();
                String res = "";
                for (int i = 0; i < len; i++) {
                    res += (i + 1) + ") " + objects.get(i).toString() + "\n";
                }
                return res;
            }
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lrem(String key, String count, String value) {
        try {
            int realCount = Integer.parseInt(count);
            return String.valueOf(server.lrem(key, realCount, value));
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String lset(String key, String index, String value) {
        try {
            int realIndex = Integer.parseInt(index);
            return server.lset(key, realIndex, value) ? OK : NOT_OK;
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String ltrim(String key, String start, String end) {
        try {
            int realStart = Integer.parseInt(start);
            int realEnd = Integer.parseInt(end);
            return server.ltrim(key, realStart, realEnd) ? OK : NOT_OK;
        } catch (NumberFormatException e) {
            return ERROR_PARSE_INT;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String rpush(String key, Object value) {
        try {
            return server.rpush(key, value) ? OK : ERROR_WRONG_TYPE;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }

    private String rpop(String key) {
        try {
            Object o = server.rpop(key);
            return o != null ? o.toString(): NIL;
        } catch (RemoteException e) {
            return e.getMessage();
        }
    }
}