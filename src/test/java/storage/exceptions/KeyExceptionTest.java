package storage.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeyExceptionTest {
    KeyException e;

    @Before
    public void init() {
        e = new KeyException("poupi");
    }

    @Test
    public void getKey() throws Exception {
        assertEquals("poupi", e.getKey());
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("An operation using the key \"poupi\" has encountered an exception.", e.getMessage());
    }
}