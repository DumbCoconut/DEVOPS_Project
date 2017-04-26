package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RequestLRangeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLRange r;
    private int legitNbTokens = 4;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLRange(tokens);
    }

    @Test
    public void reqLRangeNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLRangeLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLRangeTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLRangeKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLRangeStart() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getStart());
    }

    @Test
    public void reqLRangeEnd() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token3", r.getEnd());
    }

    @Test
    public void reqLRangeToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("lrange(\"token1\", token2, token3)", r.toString());
    }
}