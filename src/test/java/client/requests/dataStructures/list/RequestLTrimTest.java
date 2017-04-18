package client.requests.dataStructures.list;

import client.requests.exceptions.InvalidNbArgException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestLTrimTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private RequestLTRIM r;
    private int legitNbTokens = 4;

    private void createRequest(int nbTokens) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbTokens; i++) {
            tokens.add("token" + i);
        }
        r = new RequestLTRIM(tokens);
    }

    @Test
    public void reqLTrimNotEnoughToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + (legitNbTokens - 2)
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens - 1);
    }

    @Test
    public void reqLTrimLegitNbToken() throws Exception {
        createRequest(legitNbTokens);
    }

    @Test
    public void reqLTrimTooManyToken() throws Exception {
        thrown.expect(InvalidNbArgException.class);
        thrown.expectMessage("(error) wrong number of arguments (given " + legitNbTokens
                + ", expected " + (legitNbTokens - 1) + ")");
        createRequest(legitNbTokens + 1);
    }

    @Test
    public void reqLTrimKey() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token1", r.getKey());
    }

    @Test
    public void reqLTrimStart() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token2", r.getStart());
    }

    @Test
    public void reqLTrimEnd() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("token3", r.getEnd());
    }

    @Test
    public void reqLTrimToString() throws Exception {
        createRequest(legitNbTokens);
        assertEquals("ltrim(\"token1\", token2, token3)", r.toString());
    }
}