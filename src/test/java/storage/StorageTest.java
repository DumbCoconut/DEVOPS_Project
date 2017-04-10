package storage;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class StorageTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /* Creates a storage and store nbObjects objects in it. We only store <String, String> where the object will be
     * called "object{number}" and the key "key{number}" with {number} starting at 1.
     * Example: <"object1", "key1">, <"object2", "key2">, etc */
    private Storage createAndStoreHelper(Long nbObjects) throws DuplicatedKeyException {
        Storage s = new Storage(nbObjects);
        for (Long i = 0L; i < nbObjects; i++) {
            s.store("key" + (i + 1), "object" + (i + 1));
        }
        return s;
    }

    /* Asserts that the given key are (or are not) in the storage or not.
     * Example: to assert that "key1" is in, "key2" is not and "key3" is in the storage s :
     * containsHelper(s, new Boolean[]{true, false, true}) */
    private void containsHelper(Storage s, boolean[] keys) {
        int nbKeys = keys.length;
        for (int i = 0; i < nbKeys; i++) {
            String id = "key" + (i + 1);
            if (keys[i]) assertTrue(s.getCache().containsKey(id));
            else assertFalse(s.getCache().containsKey(id));
        }
    }

    /* Matcher for the duplicate key exception. */
    private org.hamcrest.Matcher<String> matcherDuplicateKeyHelper() {
        return CoreMatchers.allOf(
                containsString("key"),
                containsString("already used")
        );
    }

    /* Matcher for the non existent key exception. */
    private org.hamcrest.Matcher<String> matcherNonExistentHelper() {
        return CoreMatchers.allOf(
                containsString("key"),
                containsString("does not exist")
        );
    }

    @Test
    public void storageWithNegativeSize() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

        new Storage(-1);
    }

    @Test
    public void storageWithNulSize() throws IllegalArgumentException {
        thrown.expect(IllegalArgumentException.class);

        new Storage(0);
    }

    @Test
    public void storageWithValidSize() throws IllegalArgumentException {
        new Storage(1);
    }

    @Test
    public void storeKeyDoesNotAlreadyExists() throws DuplicatedKeyException {
        createAndStoreHelper(5L);
    }

    @Test
    public void storeInteger() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "42");
        assertEquals(Integer.class, s.get("key").getClass());
    }

    @Test
    public void storeIntegerWithLeadingZeros() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "000042");
        assertEquals(Integer.class, s.get("key").getClass());
    }

    @Test
    public void storeKeyAlreadyExists() throws DuplicatedKeyException {
        thrown.expect(DuplicatedKeyException.class);
        thrown.expectMessage(matcherDuplicateKeyHelper());

        Storage s = createAndStoreHelper(3L);
        s.store("key3", "object4");
    }

    @Test
    public void storeOverTheSizeLimit() throws KeyException {
        Storage s = createAndStoreHelper(3L);
        s.store("key4", "object4");
        containsHelper(s, new boolean[]{false, true, true, true});
    }

    @Test
    public void getExistingObject() throws KeyException {
        Storage s = createAndStoreHelper(3L);
        assertEquals("object2", s.get("key2"));
        containsHelper(s, new boolean[]{true, true, true});
    }

    @Test
    public void getNonExistingObject() throws KeyException {
        thrown.expect(NonExistentKeyException.class);
        thrown.expectMessage(matcherNonExistentHelper());

        Storage s = createAndStoreHelper(3L);
        s.get("not_used_key");
    }

    @Test
    public void removeObjectNotInCache() throws KeyException {
        thrown.expect(NonExistentKeyException.class);
        thrown.expectMessage(matcherNonExistentHelper());

        Storage s = createAndStoreHelper(1L);
        s.remove("not_used_key");
    }

    @Test
    public void removeObjectInCache() throws KeyException {
        Storage s = createAndStoreHelper(4L);
        s.remove("key2");
        containsHelper(s, new boolean[]{true, false, true, true});
    }

    /* --------------------------------------------------------------------------------------------------
     * The following tests are most likely useless. They test things that do not need to be tested,
     * E.G overloaded methods that do nothing but call the "real" method using different parameters.
     * There only purpose is to get 100% coverage.
     * --------------------------------------------------------------------------------------------------- */

    @Test
    public void constructorWithoutSize() {
        Storage s = new Storage();
        assertEquals(Storage.MAX_SIZE, s.getMaxSize());
    }

    @Test
    public void replaceValueInCache() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreHelper(2L);
        String newValue = "Same job, different outfit.";
        s.replace("key2", newValue);
        assertEquals(newValue, s.get("key2"));
    }

    @Test
    public void basicKeyException() throws KeyException {
        thrown.expect(KeyException.class);
        thrown.expectMessage("key");

        throw new KeyException("bip");
    }
}