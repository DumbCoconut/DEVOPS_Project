package client.requests.dataTypes;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestDecrByTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestDECRBY r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestDECRBY(tokens);
    }

    @Test
    public void reqDecrByNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqDecrByLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqDecrByTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqDecrByKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqDecrByInteger() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getInteger());
    }

    @Test
    public void reqDecrByToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("decrby(\"token1\", token2)", r.toString());
    }
}