package client.requests.client;

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
    public void testHelpToString() throws Exception {
        String[] tokens = new String[] {"help", "help", "get", "set"};
        createRequest(tokens);
        assertEquals(r.getMessage(), r.toString());
    }

    @Test
    public void testHelpHelp() throws Exception {
        String[] tokens = new String[] {"help", "help"};
        createRequest(tokens);
        assertEquals("", r.getMessage());
    }
}