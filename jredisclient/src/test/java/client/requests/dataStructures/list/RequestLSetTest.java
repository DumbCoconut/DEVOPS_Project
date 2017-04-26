package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestLSetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLSet r;
    private int legitNbTokens = 4;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLSet(tokens);
    }

    @Test
    public void reqLSetNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLSetLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLSetTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLSetKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLSetIndex() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getIndex());
    }

    @Test
    public void reqLSetValue() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token3", r.getValue());
    }

    @Test
    public void reqLSetToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("lset(\"token1\", token2, token3)", r.toString());
    }
}