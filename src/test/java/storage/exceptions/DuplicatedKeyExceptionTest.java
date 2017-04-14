package storage.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicatedKeyExceptionTest {
    DuplicatedKeyException e;

    @Before
    public void init() {
        e = new DuplicatedKeyException("poupi");
    }

    @Test
    public void getMessage() throws Exception {
        assertEquals("The key \"poupi\" is already used in the cache.", e.getMessage());
    }

}