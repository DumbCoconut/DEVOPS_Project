package client.requests.client;

import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


public class RequestHelpTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    RequestHelp r;

    private void createRequest(String[] args) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        Collections.addAll(tokens, args);
        r = new RequestHelp(tokens);
    }

    @Before
    public void init() throws Exception {
        createRequest(new String[]{"help"});
    }

    @Test
    public void  testParseZeroToken() throws Exception {
        thrown.expect(NoTokensException.class);
        createRequest(new String[]{});
    }

    @Test
    public void testParseOneToken() throws Exception {
        String[] tokens = new String[] {"help"};
        createRequest(tokens);
        assertEquals(tokens.length - 1, r.getRequests().size());
    }

    @Test
    public void testTwoTokens() throws Exception {
        String[] tokens = new String[] {"help", "add_server"};
        createRequest(tokens);
        assertEquals(tokens.length - 1, r.getRequests().size());
    }

    @Test
    public void testMultipleTokens() throws Exception {
        String[] tokens = new String[] {"help", "add_server", "get", "set"};
        createRequest(tokens);
        assertEquals(tokens.length - 1, r.getRequests().size());
    }

    @Test
    public void testParseToLowerCase() throws Exception {
        String[] tokens = new String[] {"help", "Add_Server", "gEt", "seT"};
        String[] expected = new String[] {
                RequestName.getInstance().getAddServerCmd(),
                RequestName.getInstance().getGetCmd(),
                RequestName.getInstance().getSetCmd()
        };
        createRequest(tokens);
        assertArrayEquals(expected, r.getRequests().toArray());
    }

    @Test
    public void testParseSortedAlphabetically() throws Exception {
        String[] tokens = new String[] {"help", "get", "add_server", "set", "del"};
        String[] expected = new String[] {
                RequestName.getInstance().getAddServerCmd(),
                RequestName.getInstance().getDelCmd(),
                RequestName.getInstance().getGetCmd(),
                RequestName.getInstance().getSetCmd()
        };
        createRequest(tokens);
        assertArrayEquals(expected, r.getRequests().toArray());
    }

    @Test
    public void testParseSortedAlphabeticallyWithUperAndLowerCase() throws Exception {
        String[] tokens = new String[] {"help", "Get", "Add_server", "sET", "Del"};
        String[] expected = new String[] {
                RequestName.getInstance().getAddServerCmd(),
                RequestName.getInstance().getDelCmd(),
                RequestName.getInstance().getGetCmd(),
                RequestName.getInstance().getSetCmd()
        };
        createRequest(tokens);
        assertArrayEquals(expected, r.getRequests().toArray());
    }

    @Test
    public void testRemoveDistinct() throws Exception {
        String[] tokens = new String[] {"help", "get", "get", "get", "get", "add_server", "add_server"};
        String[] expected = new String[] {
                RequestName.getInstance().getAddServerCmd(),
                RequestName.getInstance().getGetCmd()
        };
        createRequest(tokens);
        assertArrayEquals(expected, r.getRequests().toArray());
    }

    @Test
    public void testRemoveDistinctWithUpperAndLowerCase() throws Exception {
        String[] tokens = new String[] {"help", "get", "GeT", "GET", "gEt", "add_server", "Add_Server"};
        String[] expected = new String[] {
                RequestName.getInstance().getAddServerCmd(),
                RequestName.getInstance().getGetCmd()
        };
        createRequest(tokens);
        assertArrayEquals(expected, r.getRequests().toArray());
    }

    @Test
    public void testHelpToString() throws Exception {
        String[] tokens = new String[] {"help", "help", "get", "set"};
        createRequest(tokens);
        assertEquals(r.getMessage(), r.toString());
    }

    @Test
    public void testGetSeparator() throws Exception {
        createRequest(new String[]{"help"});
        assertEquals("\n--------------------\n", r.getSeparator());
    }

    @Test
    public void testGetHelpHelp() throws Exception {
        String expected = "HELP [cmd1, cmd2, ... cmdN]" + "\n\n"
                        + "DESCRIPTION: Display the help for the given commands. If no command is provided, display " +
                          "the help for all the commands.";
        assertEquals(expected, r.getHelpHelp());
    }

    @Test
    public void testGetHelpAddServer() throws Exception {
        String expected = "ADD_SERVER server_ip server_name" + "\n\n"
                        +  "DESCRIPTION: Connect to the given server. server_ip is the IP of the server (if local, 127.0.0.1). " +
                           "server_name is the name of the server on the host (example: server_0)";
        assertEquals(expected, r.getHelpAddServer());
    }

    @Test
    public void getTestGetQuit() throws Exception {
        String expected = "QUIT" + "\n\n"
                        + "DESCRIPTION: Terminate the client. This command is the same as EXIT.";
        assertEquals(expected, r.getHelpQuit());
    }

    @Test
    public void getTestGetExit() throws Exception {
        String expected = "EXIT" + "\n\n"
                        + "DESCRIPTION: Terminate the client. This command is the same as QUIT.";
        assertEquals(expected, r.getHelpExit());
    }

    @Test
    public void testGetHelpGet() throws Exception {
        String expected = "GET key" + "\n\n"
                        + "DESCRIPTION: Get the value of the specified key. If the key does not exist " +
                          "the special value 'null' is returned.";
        assertEquals(expected, r.getHelpGet());
    }

    @Test
    public void testGetHelpSet() throws Exception {
        String expected =  "SET key value" + "\n\n"
                        +  "DESCRIPTION: Set key to hold the value. If key already holds a value, it is overwritten, " +
                           "regardless of its type.";
        assertEquals(expected, r.getHelpSet());
    }

    @Test
    public void testGetHelpDecr() throws Exception {
        String expected =  "DECR key" + "\n\n"
                        +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
                           "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
                           "before to perform the increment or decrement operation." + "\n\n"
                        +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
                           "the increment/decrement is integer.";
        assertEquals(expected, r.getHelpDecr());
    }

    @Test
    public void testGetHelpDecrBy() throws Exception {
        String expected = "DECRBY key integer" + "\n\n"
                        +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
                           "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
                           "before to perform the increment or decrement operation." + "\n\n"
                        +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
                           "the increment/decrement is integer.";
        assertEquals(expected, r.getHelpDecrBy());
    }

    @Test
    public void testGetHelpIncr() throws Exception {
        String expected = "INCR key" + "\n\n"
                        +  "DESCRIPTION: Increment or decrement the number stored at key by one. " +
                           "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
                           "before to perform the increment or decrement operation." + "\n\n"
                        +  "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
                           "the increment/decrement is integer.";
        assertEquals(expected, r.getHelpIncr());
    }

    @Test
    public void testGetHelpIncrBy() throws Exception {
        String expected = "INCRBY key integer" + "\n\n"
                        + "DESCRIPTION: Increment or decrement the number stored at key by one. " +
                          "If the key does not exist or contains a value of a wrong type, set the key to the value of \"0\" " +
                          "before to perform the increment or decrement operation." + "\n\n"
                        + "INCRBY and DECRBY work just like INCR and DECR but instead to increment/decrement by 1 " +
                          "the increment/decrement is integer.";
        assertEquals(expected, r.getHelpIncrBy());
    }

    @Test
    public void testGetHelpType() throws Exception {
        String expected = "TYPE key" + "\n\n"
                        + "Return the type of the value stored at key in form of a string. The type can be one of \"none\", " +
                          "\"string\", \"list\", \"set\". \"none\" is returned if the key does not exist.";
        assertEquals(expected, r.getHelpType());
    }

    @Test
    public void testGetDel() throws Exception {
        String expected = "DEL key" + "\n\n"
                        + "DESCRIPTION: Remove the specified keys. If a given key does not exist no operation is performed " +
                          "for this key. The command returns the number of keys removed.";
        assertEquals(expected, r.getHelpDel());
    }

    @Test
    public void testGetHelp() throws Exception {
        /* ugly code ahead */
        ArrayList<String> tokens = new ArrayList<>();
        tokens.add("help");
        tokens.addAll(RequestName.getInstance().getAllCmds().values());
        int tSize = tokens.size();
        String[] tokensArray = new String[tSize];
        for (int i = 1; i < tSize; i++) {
            tokensArray[i] = tokens.get(i);
        }
        createRequest(tokensArray);
        String helpMessage = r.getHelp();
        for (String s : tokens.subList(1, tokens.size())) {
            assert helpMessage.contains(s) || helpMessage.contains(s.toUpperCase()) || helpMessage.contains(s.toLowerCase());
        }
    }

    @Test
    public void testGetMessageWithHelp() throws Exception {
        createRequest(new String[]{"help", "help"});
        assert r.getMessage().contains("HELP");
    }

    @Test
    public void testGetMessageWithQuit() throws Exception {
        createRequest(new String[]{"help", "QUIT"});
        assert r.getMessage().contains("QUIT");
    }

    @Test
    public void testGetMessageWithExit() throws Exception {
        createRequest(new String[]{"help", "EXIT"});
        assert r.getMessage().contains("EXIT");
    }

    @Test
    public void testGetMessageWithAddServer() throws Exception {
        createRequest(new String[]{"help", "ADD_SERVER"});
        assert r.getMessage().contains("ADD_SERVER");
    }

    @Test
    public void testGetMessageWithGet() throws Exception {
        createRequest(new String[]{"help", "GET"});
        assert r.getMessage().contains("GET");
    }

    @Test
    public void testGetMessageWithSet() throws Exception {
        createRequest(new String[]{"help", "SET"});
        assert r.getMessage().contains("SET");
    }

    @Test
    public void testGetMessageWithDecr() throws Exception {
        createRequest(new String[]{"help", "DECR"});
        assert r.getMessage().contains("DECR");
    }

    @Test
    public void testGetMessageWithDecrBy() throws Exception {
        createRequest(new String[]{"help", "DECRBY"});
        assert r.getMessage().contains("DECRBY");
    }

    @Test
    public void testGetMessageWithIncr() throws Exception {
        createRequest(new String[]{"help", "INCR"});
        assert r.getMessage().contains("INCR");
    }

    @Test
    public void testGetMessageWithIncrBy() throws Exception {
        createRequest(new String[]{"help", "INCRBY"});
        assert r.getMessage().contains("INCRBY");
    }

    @Test
    public void testGetMessageWithType() throws Exception {
        createRequest(new String[]{"help", "TYPE"});
        assert r.getMessage().contains("TYPE");
    }

    @Test
    public void testGetMessageWithDel() throws Exception {
        createRequest(new String[]{"help", "DEL"});
        assert r.getMessage().contains("DEL");
    }

    @Test
    public void testGetMessageWithLIndex() throws Exception {
        createRequest(new String[]{"help", "LINDEX"});
        assert r.getMessage().contains("LINDEX");
    }

    @Test
    public void testGetMessageWithLLen() throws Exception {
        createRequest(new String[]{"help", "LLEN"});
        assert r.getMessage().contains("LLEN");
    }

    @Test
    public void testGetMessageWithLPop() throws Exception {
        createRequest(new String[]{"help", "LPOP"});
        assert r.getMessage().contains("LPOP");
    }

    @Test
    public void testGetMessageWithLPush() throws Exception {
        createRequest(new String[]{"help", "LPUSH"});
        assert r.getMessage().contains("LPUSH");
    }

    @Test
    public void testGetMessageWithLRange() throws Exception {
        createRequest(new String[]{"help", "LRANGE"});
        assert r.getMessage().contains("LRANGE");
    }

    @Test
    public void testGetMessageWithLRem() throws Exception {
        createRequest(new String[]{"help", "LREM"});
        assert r.getMessage().contains("LREM");
    }

    @Test
    public void testGetMessageWithLSet() throws Exception {
        createRequest(new String[]{"help", "LSET"});
        assert r.getMessage().contains("LSET");
    }

    @Test
    public void testGetMessageWithLTrim() throws Exception {
        createRequest(new String[]{"help", "LTRIM"});
        assert r.getMessage().contains("LTRIM");
    }

    @Test
    public void testGetMessageWithRPop() throws Exception {
        createRequest(new String[]{"help", "RPOP"});
        assert r.getMessage().contains("RPOP");
    }

    @Test
    public void testGetMessageWithRPush() throws Exception {
        createRequest(new String[]{"help", "RPUSH"});
        assert r.getMessage().contains("RPUSH");
    }

    @Test
    public void testGetMessageWithSAdd() throws Exception {
        createRequest(new String[]{"help", "SADD"});
        assert r.getMessage().contains("SADD");
    }

    @Test
    public void testGetMessageWithMultipleCmd() throws Exception {
        createRequest(new String[]{"help", "help", "del", "get"});
        assert (
               r.getMessage().contains("HELP") &&
               r.getMessage().contains("DEL") &&
               r.getMessage().contains("GET")
        );
    }

    @Test
    public void testGetMessageWithInvalidCmd() throws Exception {
        createRequest(new String[]{"help", "OWOWOW"});
        assert (
                r.getMessage().contains("OWOWOW") &&
                r.getMessage().contains("error")
        );
    }

    @Test
    public void testGetMessageWithNoCmd() throws Exception {
        createRequest(new String[]{"help"});
        assertEquals(r.getMessage(), r.getHelp());
    }

}