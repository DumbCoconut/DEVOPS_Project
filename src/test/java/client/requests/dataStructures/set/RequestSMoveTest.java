package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestSMoveTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSMove r;
    private int legitNbTokens = 4;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSMove(tokens);
    }

    @Test
    public void reqSMoveNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSMoveLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSMoveTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSMoveKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSMoveDstKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getDstkey());
    }

    @Test
    public void reqSMoveMember() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token3", r.getMember());
    }

    @Test
    public void reqSMoveToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("smove(\"token1\", token2, token3)", r.toString());
    }
}