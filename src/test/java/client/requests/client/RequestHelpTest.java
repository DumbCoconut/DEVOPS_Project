package client.requests.client;

import client.requests.Request;
import client.requests.RequestName;
import client.requests.exceptions.NoTokensException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;

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
}