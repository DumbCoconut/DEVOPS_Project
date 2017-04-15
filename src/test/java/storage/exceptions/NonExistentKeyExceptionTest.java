package storage.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NonExistentKeyExceptionTest {
    private NonExistentKeyException e;

    @Before
    public void init() {
        e = new NonExistentKeyException("poupi");
    }
    @Test
    public void getMessage() throws Exception {
        assertEquals("The key \"poupi\" does not exist in the cache.", e.getMessage());
    }
}