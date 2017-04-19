package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestSRandMemberTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSRandMember r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSRandMember(tokens);
    }

    @Test
    public void reqSRandMemberNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSRandMemberLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSRandMemberTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSRandMemberKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSRandMemberToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("srandmember(\"token1\")", r.toString());
    }
}