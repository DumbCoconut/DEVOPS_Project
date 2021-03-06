package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestSRemTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSRem r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSRem(tokens);
    }

    @Test
    public void reqSRemNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSRemLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSRemTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSRemKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSRemGetMember() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getMember());
    }

    @Test
    public void reqSRemToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("srem(\"token1\", token2)", r.toString());
    }
}