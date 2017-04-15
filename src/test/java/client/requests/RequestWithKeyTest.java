package client.requests;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestWithKeyTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestWithKey mockRequest;
    private int minNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        mockRequest = new RequestWithKey(tokens) {};
    }

    @Test
    public void reqWithKeyNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        createRequest(minNbTokens - 1);
    }

    @Test
    public void reqWithKeyExactNbToken() throws Exception {
        createRequest(minNbTokens);
    }

    @Test
    public void reqWithKeyLegitNbToken() throws Exception {
        createRequest(minNbTokens + 5);
    }

    @Test
    public void reqWithKeyKey() throws Exception {
        createRequest(minNbTokens);
        assertEquals("token1", mockRequest.getKey());
    }

    @Test
    public void reqWithKeyToString() throws Exception {
        createRequest(minNbTokens);
        assertEquals("request(\"token1\")", mockRequest.toString());
    }
}