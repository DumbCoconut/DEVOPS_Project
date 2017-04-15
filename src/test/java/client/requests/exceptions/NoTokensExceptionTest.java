package client.requests.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NoTokensExceptionTest {
    private NoTokensException e;

    @Before
    public void init() {
        e = new NoTokensException();
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("(error) no tokens were provided to the request.", e.getMessage());
    }

}