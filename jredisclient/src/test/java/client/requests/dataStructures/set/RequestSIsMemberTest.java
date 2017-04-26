package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestSIsMemberTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSIsMember r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSIsMember(tokens);
    }

    @Test
    public void reqSIsMemberNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSIsMemberLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSIsMemberTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSIsMemberKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSIsMemberGetMember() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getMember());
    }

    @Test
    public void reqSIsMemberToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("sismember(\"token1\", token2)", r.toString());
    }
}