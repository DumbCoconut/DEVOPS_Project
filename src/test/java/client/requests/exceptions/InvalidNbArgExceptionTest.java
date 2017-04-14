package client.requests.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvalidNbArgExceptionTest {
    private InvalidNbArgException e;
    private int given;
    private int expected;

    @Before
    public void init() {
        given = 0;
        expected = 2;
        e = new InvalidNbArgException(given, expected);
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("(error) wrong number of arguments (given " + given + ", expected " + expected + ")", e.getMessage());
    }

}