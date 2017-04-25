package client.requests.client;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestSetServerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSetServer r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSetServer(tokens);
    }

    @Test
    public void reqAddServerNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqAddServerLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqAddServerTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqAddServerIp() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getIp());
    }

    @Test
    public void reqAddServerName() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getName());
    }

    @Test
    public void reqAddServerToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1:token2", r.toString());
    }
}