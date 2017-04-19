package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestSInterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSInter r;
    private int legitNbTokens = 2;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSInter(tokens);
    }

    @Test
    public void  testParseZeroToken() throws Exception {
        thrown.expect(NoTokensException.class);
        createRequest(0);
    }

    @Test
    public void reqSInterLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSInterKeys() throws Exception {
        createRequest(4);
        assertArrayEquals(new String[]{"token1", "token2", "token3"}, r.getKeys());
    }

    @Test
    public void reqSInterToString() throws Exception {
        createRequest(4);
        assertEquals("sinter(\"token1\", \"token2\", \"token3\")", r.toString());
    }
}