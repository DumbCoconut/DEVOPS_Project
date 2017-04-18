package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestLRemTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLREM r;
    private int legitNbTokens = 4;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLREM(tokens);
    }

    @Test
    public void reqLRemNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLRemLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLRemTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLRemKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLRemCount() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getCount());
    }

    @Test
    public void reqLRemValue() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token3", r.getValue());
    }

    @Test
    public void reqLRemToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("lrem(\"token1\", token2, token3)", r.toString());
    }
}