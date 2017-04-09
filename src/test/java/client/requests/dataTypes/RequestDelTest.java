package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestDelTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestDel r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestDel(tokens);
    }

    @Test
    public void reqDelNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqDelLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqDelTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqDelKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqDelToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("del(\"token1\")", r.toString());
    }
}