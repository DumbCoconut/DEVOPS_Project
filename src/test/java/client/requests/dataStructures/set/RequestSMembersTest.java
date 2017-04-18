package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestSMembersTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSMembers r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSMembers(tokens);
    }

    @Test
    public void reqSMembersNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSMembersLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSMembersTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSMembersKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSMembersToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("smembers(\"token1\")", r.toString());
    }
}