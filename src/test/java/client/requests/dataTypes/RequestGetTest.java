package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestGetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestGet r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestGet(tokens);
    }

    @Test
    public void reqGetNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqGetLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqGetTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqGetKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqGetToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("get(\"token1\")", r.toString());
    }
}