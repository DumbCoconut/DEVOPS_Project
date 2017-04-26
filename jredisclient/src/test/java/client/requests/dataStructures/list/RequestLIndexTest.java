package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestLIndexTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLIndex r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLIndex(tokens);
    }

    @Test
    public void reqLIndexNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLIndexLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLIndexTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLIndexKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLIndexIndex() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getIndex());
    }

    @Test
    public void reqLIndexToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("lindex(\"token1\", token2)", r.toString());
    }
}