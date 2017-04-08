package client.requests;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Request createMockObject(int nbArgs) throws Exception {
        ArrayList<String> tokens = new ArrayList<>();
        for (int i = 0; i < nbArgs + 1; i++) {
            tokens.add("arg" + i);
        }
        return new Request(tokens) {
            @Override
            public String toString() {
                return "";
            }
        };
    }

    @Test
    public void constructorWith0Tokens() throws Exception {
        thrown.expect(Exception.class);

        createMockObject(-1);
    }

    @Test
    public void getNbArgs() throws Exception {
        int nbArgs = 3;
        assertEquals(nbArgs, createMockObject(nbArgs).getNbArgs());
    }

    @Test
    public void nbExpectedTokensConstructor() throws Exception {
        int nbArgs = 5;
        assertEquals(nbArgs + 1, createMockObject(nbArgs).nbExpectedTokens());
    }

    @Test
    public void nbExpectedTokensAfterSet() throws Exception {
        Request r = createMockObject(2);
        int nbArgs = 5;
        r.setNbArgs(nbArgs);
        assertEquals(nbArgs + 1, r.nbExpectedTokens());
    }

    @Test
    public void nbExpectedTokensAfterInvalidSet() throws Exception {
        Request r = createMockObject(2);
        int oldNbTokens = r.nbExpectedTokens();
        int nbArgs = -1;
        try {
            r.setNbArgs(nbArgs);
        } catch (IllegalArgumentException e) {
            assertEquals(oldNbTokens, r.nbExpectedTokens());
            return;
        }
        fail();
    }

    @Test
    public void setNbArgsValid() throws Exception {
        Request r = createMockObject(5);
        int nbArgs = 2;
        r.setNbArgs(nbArgs);
        assertEquals(nbArgs, r.getNbArgs());
    }

    @Test
    public void setNulNbArgs() throws Exception {
        Request r = createMockObject(10);
        r.setNbArgs(0);
        assertEquals(0, r.getNbArgs());
    }

    @Test
    public void setNegativeNbArgs() throws Exception {
        Request r = createMockObject(10);
        int oldNbArgs = r.getNbArgs();
        try {
            r.setNbArgs(-1);
        } catch (IllegalArgumentException e){
            assertEquals(oldNbArgs, r.getNbArgs());
            return;
        }
        fail();
    }
}