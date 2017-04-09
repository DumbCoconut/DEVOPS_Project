package client.requests.dataTypes;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestTypeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestType r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestType(tokens);
    }

    @Test
    public void reqTypeNotEnoughToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqTypeLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqTypeTooManyToken() throws Exception {
        thrown.expect(Exception.class);
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqTypeKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqTypeToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("type(\"token1\")", r.toString());
    }
}