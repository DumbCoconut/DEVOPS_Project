package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestDecrTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestDecr r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestDecr(tokens);
    }

    @Test
    public void reqDecrNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqDecrLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqDecrTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqDecrKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqDecrToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("decr(\"token1\")", r.toString());
    }
}