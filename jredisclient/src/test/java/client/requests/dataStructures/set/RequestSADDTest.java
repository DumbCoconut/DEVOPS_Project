package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestSADDTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSAdd r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSAdd(tokens);
    }

    @Test
    public void reqSAddNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSAddLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSAddTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSAddKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSAddGetMember() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getMember());
    }

    @Test
    public void reqSAddToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("sadd(\"token1\", token2)", r.toString());
    }
}