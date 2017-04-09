package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestIncrByTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestIncrBy r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestIncrBy(tokens);
    }

    @Test
    public void reqIncrByNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqIncrByLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqIncrByTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqIncrByKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqIncrByInteger() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getInteger());
    }

    @Test
    public void reqIncrByToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("incrby(\"token1\", token2)", r.toString());
    }
}