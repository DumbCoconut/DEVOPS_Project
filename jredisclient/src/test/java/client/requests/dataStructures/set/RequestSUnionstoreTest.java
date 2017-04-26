package client.requests.dataStructures.set;

import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RequestSUnionstoreTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestSUnionstore r;
    private int legitNbTokens = 3;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestSUnionstore(tokens);
    }

    @Test
    public void  testParseZeroToken() throws Exception {
        thrown.expect(NoTokensException.class);
        createRequest(0);
    }

    @Test
    public void reqSUnionstoreNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqSUnionstoreLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqSUnionstoreKeys() throws Exception {
        createRequest(4);
        assertArrayEquals(new String[]{"token1", "token2", "token3"}, r.getKeys());
    }

    @Test
    public void reqSUnionstoreToString() throws Exception {
        createRequest(4);
        assertEquals("sunionstore(\"token1\", \"token2\", \"token3\")", r.toString());
    }
}