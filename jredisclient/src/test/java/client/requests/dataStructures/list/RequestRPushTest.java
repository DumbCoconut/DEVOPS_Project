package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestRPushTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestRPush r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestRPush(tokens);
    }

    @Test
    public void reqRPushNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqRPushLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqRPushTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqRPushKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqRPushString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getString());
    }

    @Test
    public void reqRPushToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("rpush(\"token1\", token2)", r.toString());
    }
}