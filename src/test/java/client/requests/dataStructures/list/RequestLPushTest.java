package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestLPushTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLPUSH r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLPUSH(tokens);
    }

    @Test
    public void reqLPushNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLPushLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLPushTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLPushKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLPushString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getString());
    }

    @Test
    public void reqLPushToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("lpush(\"token1\", token2)", r.toString());
    }
}