package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Mathieu on 09/04/2017.
 */
public class RequestSetTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSet r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSet(tokens);
    }

    @Test
    public void reqSetNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSetLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSetTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqSetKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqSetObject() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getObject());
    }

    @Test
    public void reqSetToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("set(\"token1\", token2)", r.toString());
    }
}