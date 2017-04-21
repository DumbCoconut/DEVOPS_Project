package storage;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import storage.exceptions.DuplicatedKeyException;
import storage.exceptions.KeyException;
import storage.exceptions.NonExistentKeyException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class StorageTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                      HELPERS                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

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

    private Storage createAndStoreEmptyListHelper() throws DuplicatedKeyException {
        Storage s = new Storage(10L);
        s.store("key", new ArrayList<>());
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

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                               TESTS CONSTRUCTOR                                                */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

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
    public void constructorWithoutSize() {
        Storage s = new Storage();
        assertEquals(Storage.MAX_SIZE, s.getMaxSize());
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS STORE                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void storeKeyDoesNotAlreadyExists() throws DuplicatedKeyException {
        createAndStoreHelper(5L);
    }

    @Test
    public void storeIntegerCheckValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", 42);
        assertEquals(42, s.get("key"));
    }

    @Test
    public void storeIntegerCheckClass() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", 42);
        assertEquals(Integer.class, s.get("key").getClass());
    }

    @Test
    public void storeIntegerAsString() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "42");
        assertEquals(Integer.class, s.get("key").getClass());
    }

    @Test
    public void storeIntegerAsStringWithLeadingZeros() throws DuplicatedKeyException, NonExistentKeyException {
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

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS GET                                                     */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

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

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS REMOVE                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

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

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS REPLACE                                                 */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void replaceValueInCacheStringToInteger() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.replace("key", "3");
        assertEquals(s.get("key").getClass(), Integer.class);
    }

    @Test
    public void replaceValueInCacheIntegerToString() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "3");
        s.replace("key", "value");
        assertEquals(s.get("key").getClass(), String.class);
    }

    @Test
    public void replaceWithIntegerCheckClass() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "hi");
        s.replace("key", 3);
        assertEquals(Integer.class, s.get("key").getClass());
    }

    @Test
    public void replaceWithIntegerCheckValue() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "hi");
        s.replace("key", 3);
        assertEquals(3, s.get("key"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS RPUSH                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void rPushCreateArrayList() throws NonExistentKeyException {
        Storage s = new Storage();
        s.rPush("key", "value");
        assertEquals(s.get("key").getClass(), ArrayList.class);
    }

    @Test
    public void rPushOnEmptyList() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.rPush("key", "value");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value", l.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void rPushOnNonEmptyList() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.rPush("key", "value");
        s.rPush("key", "value2");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value2", l.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void rPushOnWrongTypeReturnValue() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "hey");
        assertEquals(false, s.rPush("key", "value"));
    }

    @Test
    public void rPushOnEmptyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s =  createAndStoreEmptyListHelper();
        assertEquals(true, s.rPush("key", "value"));
    }

    @Test
    public void rPushOnNonEmptyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s =  createAndStoreEmptyListHelper();
        s.rPush("key", "value");
        assertEquals(true, s.rPush("key", "value2"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LPUSH                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lPushCreateArrayList() throws NonExistentKeyException {
        Storage s = new Storage();
        s.lPush("key", "value");
        assertEquals(s.get("key").getClass(), ArrayList.class);
    }

    @Test
    public void lPushOnEmptyList() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value", l.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void lPushOnNonEmptyList() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value2", l.get(l.size() - 1));
        } else {
            fail();
        }
    }

    @Test
    public void lPushOnWrongTypeReturnValue() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "hey");
        assertEquals(false, s.lPush("key", "value"));
    }

    @Test
    public void lPushOnEmptyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s =  createAndStoreEmptyListHelper();
        assertEquals(true, s.lPush("key", "value"));
    }

    @Test
    public void lPushOnNonEmptyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s =  createAndStoreEmptyListHelper();
        s.rPush("key", "value");
        assertEquals(true, s.lPush("key", "value2"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS RPOP                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void rpopOnEmptyList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(null, s.rPop("key"));
    }

    @Test
    public void rpopOnNotList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(null, s.rPop("key"));
    }

    @Test
    public void rpopOnNotListDoesNotRemove() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.store("key2", "hello");
        s.rPop("key2");
        assertEquals("hello", s.get("key2"));
    }

    @Test
    public void rpopListObjectIsDeleted() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        s.rPop("key");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value2", l.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void rpopListObjectIsReturned() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        Object returned = s.rPop("key");
        assertEquals("value", returned);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LPOP                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lpopOnEmptyList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(null, s.lPop("key"));
    }

    @Test
    public void lpopOnNotList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(null, s.lPop("key"));
    }

    @Test
    public void lpopOnNotListDoesNotRemove() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.store("key2", "hello");
        s.lPop("key2");
        assertEquals("hello", s.get("key2"));
    }

    @Test
    public void lpopListObjectIsDeleted() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        s.lPop("key");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals("value", l.get(0));
        } else {
            fail();
        }
    }

    @Test
    public void lpopListObjectIsReturned() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        Object returned = s.lPop("key");
        assertEquals("value2", returned);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LLEN                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void llenOnNonExistentKey() {
        Storage s = new Storage();
        assertEquals(0, s.llen("key"));
    }

    @Test
    public void llenOnNotList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(-1, s.llen("key"));
    }

    @Test
    public void llenOnList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int expected = 5;
        for (int i = 0; i < expected; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(expected, s.llen("key"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LINDEX                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lindexOnNotList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(null, s.lindex("key", 0));
    }

    @Test
    public void lindexOutOfRange() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals("", s.lindex("key", len));
    }

    @Test
    public void lindexOnListValidIndex() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(String.valueOf(len - 2), s.lindex("key", len - 2));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LSET                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lsetOnNotListReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(false, s.lset("key", 0, "value"));
    }

    @Test
    public void lsetNotAList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(false, s.lset("key", 0, "value"));
    }

    @Test
    public void lsetOnNotListDoesNotModifyOld() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.lset("key", 0, "value");
        assertEquals("value", s.get("key"));
    }

    @Test
    public void lsetOutOfRangeUpperReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(false, s.lset("key", len, "value"));
    }

    @Test
    public void lsetOutOfRangeLowerReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(false, s.lset("key", -1, "value"));
    }

    @Test
    public void lsetOutOfRangeUpperDoesNotModifyAnything() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        ArrayList<Object> expected = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
            expected.add("" + i);
        }

        s.lset("key", len, "zzzz");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertArrayEquals(expected.toArray(), l.toArray());
        } else {
            fail();
        }
    }

    @Test
    public void lsetOutOfRangeLowerDoesNotModifyAnything() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        ArrayList<Object> expected = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
            expected.add("" + i);
        }
        s.lset("key", -1, "zzzz");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertArrayEquals(expected.toArray(), l.toArray());
        } else {
            fail();
        }
    }

    @Test
    public void lsetOnListReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        assertEquals(true, s.lset("key", 0, "value4"));
    }

    @Test
    public void lsetOnListModifyOldValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lset("key", 0, "new");
        assertEquals("new", s.lindex("key", 0));
    }

    @Test
    public void lsetOnListDoesNotModifyAnythingElse() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        ArrayList<Object> expected = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
            expected.add("" + i);
        }
        s.lset("key", 0, "new");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            Object[] actual = ((ArrayList<Object>) o).subList(1, ((ArrayList<Object>) o).size() - 1).toArray();
            assertArrayEquals(expected.subList(1, expected.size() - 1).toArray(), actual);
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LREM                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lremOnNonExistingKey() {
        Storage s = new Storage();
        assertEquals(0, s.lrem("key", 50, "value"));
    }

    @Test
    public void lremEmptyList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(0, s.lrem("key", 50, "value"));
    }

    @Test
    public void lremNotAList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(0, s.lrem("key", 0, 0));
    }

    @Test
    public void lremAllElementsOfReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int toRemove = 5;
        for (int i = 0; i < toRemove; i++) {
            s.lPush("key", "" + "remove_me");
        }
        s.lPush("key", "do_not_remove_me");
        assertEquals(toRemove, s.lrem("key", toRemove, "remove_me"));
    }

    @Test
    public void lremAllElementsOfDoesRemove() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int toRemove = 5;
        for (int i = 0; i < toRemove; i++) {
            s.lPush("key", "" + "remove_me");
        }
        s.lPush("key", "do_not_remove_me");
        s.lrem("key", 0, "remove_me");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals(false, l.contains("remove_me"));
        } else {
            fail();
        }
    }

    @Test
    public void lremAllElementsOfDoesNotRemoveOthers() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int toRemove = 5;
        for (int i = 0; i < toRemove; i++) {
            s.lPush("key", "" + "remove_me");
        }
        s.lPush("key", "do_not_remove_me");
        s.lrem("key", 0, "remove_me");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals(true, l.contains("do_not_remove_me"));
        } else {
            fail();
        }
    }

    @Test
    public void lremPartOfEnoughElementsReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        int toRemove = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + "remove_me");
        }
        assertEquals(toRemove, s.lrem("key", toRemove, "remove_me"));
    }

    @Test
    public void lremPartOfEnoughElementsDoesNotRemoveMore() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        int toRemove = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + "remove_me");
        }
        s.lrem("key", toRemove, "remove_me");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals(len - toRemove, l.size());
        } else {
            fail();
        }
    }

    @Test
    public void lremPartOfNotEnoughElementsReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 2;
        int toRemove = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + "remove_me");
        }
        assertEquals(len, s.lrem("key", toRemove, "remove_me"));
    }

    @Test
    public void lremPartOfNotEnoughElementsDoesNotRemoveMore() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 2;
        int toRemove = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + "remove_me");
        }
        s.lPush("key", "do_not_remove_me");
        s.lrem("key", toRemove, "remove_me");
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            ArrayList<Object> l = (ArrayList<Object>) o;
            assertEquals(true, l.contains("do_not_remove_me"));
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LRANGE                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void lrangeOnNonExistingKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.lrange("key", 0, 5).toArray());
    }

    @Test
    public void lRangeNotAList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(null, s.lrange("key", 0, 0));
    }

    @Test
    public void lrangeOnEmptyList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertArrayEquals(new Object[]{}, s.lrange("key", 0, 0).toArray());
    }

    @Test
    public void lrangeOnListBelowLowerBoundReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        assertArrayEquals(new Object[]{}, s.lrange("key", -1, 10).toArray());
    }

    @Test
    public void lrangeOnListBelowLowerBoundDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.lrange("key", -1, 3);
        String[] expected = new String[]{"0", "1", "2", "3", "4"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    @Test
    public void lrangeOnListStartOverEndReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        String[] expected = new String[] {};
        assertArrayEquals(expected, s.lrange("key", len + 1, len - 1).toArray());
    }

    @Test
    public void lrangeOnListStartOverEndDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.lrange("key", len + 1, len - 1);
        String[] expected = new String[]{"0", "1", "2", "3", "4"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    @Test
    public void lrangeOnListReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        String[] expected = new String[]{"1", "2", "3"};
        ArrayList<Object> range = s.lrange("key", 1, 3);
        assertArrayEquals(expected, range.toArray());
    }

    @Test
    public void lrangeOnListDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.lrange("key", 1, 3);
        String[] expected = new String[]{"0", "1", "2", "3", "4"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    @Test
    public void lrangeOverUpperBoundReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        String[] expected = new String[] {"1", "2"};
        ArrayList<Object> range = s.lrange("key", 1, len + 1);
        assertArrayEquals(expected, range.toArray());
    }

    @Test
    public void lrangeOverUpperBoundDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.lrange("key", 0, len + 1);
        String[] expected = new String[] {"0", "1", "2"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS LTRIM                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void ltrimOnNonExistingKey() {
        Storage s = new Storage();
        assertEquals(false, s.ltrim("key", 0, 5));
    }

    @Test
    public void ltrimOnEmptyList() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        assertEquals(true, s.ltrim("key", 0, 5));
    }

    @Test
    public void ltrimNotAList() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(false, s.ltrim("key", 0, 0));
    }

    @Test
    public void ltrimOnListBelowLowerBoundReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        assertEquals(true, s.ltrim("key", -1, 10));
    }

    @Test
    public void ltrimOnListBelowLowerBoundDoesModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        s.lPush("key", "value");
        s.lPush("key", "value2");
        s.ltrim("key", -1, 10);
        Object[] expected = new Object[] {};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        }
    }

    @Test
    public void ltrimOnListStartOverEndReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        String[] expected = new String[] {};
        assertEquals(true, s.ltrim("key", len + 1, len - 1));
    }

    @Test
    public void ltrimOnListStartOverEndDoesModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.ltrim("key", len + 1, len - 1);
        Object[] expected = new Object[]{};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    @Test
    public void ltrimOnListReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(true, s.ltrim("key", 1, 3));
    }

    @Test
    public void ltrimOnListDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.ltrim("key", 1, 3);
        String[] expected = new String[]{"1", "2", "3"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    @Test
    public void ltrimOverUpperBoundReturnValue() throws DuplicatedKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 3;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        assertEquals(true, s.ltrim("key", 0, len + 1));
    }

    @Test
    public void ltrimOverUpperBoundDoesModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = createAndStoreEmptyListHelper();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.lPush("key", "" + i);
        }
        s.ltrim("key", 2, len + 1);
        String[] expected = new String[] {"2", "3", "4"};
        Object o = s.get("key");
        if (o instanceof ArrayList) {
            assertArrayEquals(expected, ((ArrayList) o).toArray());
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SADD                                                      */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void testSaddExistentKeyNotSetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        int res = s.sadd("key", "value2");
        assertEquals(true, res < 0);
    }

    @Test
    public void testSaddExistentKeyNotSetDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.sadd("key", "value2");
        assertEquals("value", s.get("key"));
    }

    @Test
    public void testSAddNonExistentKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(1, s.sadd("key", "value"));
    }

    @Test
    public void testSAddNonExistentKeyDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            HashSet set = (HashSet) o;
            assertEquals(true, set.contains("value"));
        } else {
            fail();
        }
    }

    @Test
    public void testSAddExistentKeyNonExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "member");
        assertEquals(1, s.sadd("key", "member2"));
    }

    @Test
    public void testSAddExistentKeyNonExistentObjectDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        s.sadd("key", "value2");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            HashSet set = (HashSet) o;
            assertEquals(true, set.contains("value") && set.contains("value2"));
        } else {
            fail();
        }
    }

    @Test
    public void testSAddExistentKeyExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "member");
        assertEquals(0, s.sadd("key", "member"));
    }

    @Test
    public void testSAddExistentKeyExistentObjectDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            HashSet set = (HashSet) o;
            assertEquals(1, set.size());
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SCARD                                                     */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    // Here we do not test if scard modifies the set because it obviously don't and we do not have the time..

    @Test
    public void testSCardNonExistentKey() {
        Storage s = new Storage();
        assertEquals(0, s.scard("key"));
    }

    @Test
    public void testSCardExistentKeySet() {
        Storage s = new Storage();
        int size = 3;
        for (int i = 0; i < size; i++) {
            s.sadd("key", "" + i);
        }
        assertEquals(size, s.scard("key"));
    }

    @Test
    public void testSCardExistentKeyNotSet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        int res = s.scard("key");
        assertEquals(true, res < 0);
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SREM                                                      */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void testSRemNonExistentKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(0, s.srem("key", "value"));
    }

    @Test
    public void testSRemExistentKeyExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "value");
        assertEquals(1, s.srem("key", "value"));
    }

    @Test
    public void testSRemExistentKeyExistentObjectDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            int oldSize = ((HashSet) o).size();
            s.srem("key", "value");
            int newSize = ((HashSet) o).size();
            assertEquals(oldSize - 1, newSize);
        } else {
            fail();
        }
    }

    @Test
    public void testSRemExistentKeyNonExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "value");
        assertEquals(0, s.srem("key", "value2"));
    }

    @Test
    public void testSRemExistentKeyExistentNonObjectDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            int oldSize = ((HashSet) o).size();
            s.srem("key", "value2");
            int newSize = ((HashSet) o).size();
            assertEquals(oldSize, newSize);
        } else {
            fail();
        }
    }

    @Test
    public void testSRemExistentKeyNotSetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(-1, s.srem("key", "value"));
    }

    @Test
    public void testSRemExistentKeyNotSetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.srem("key", "value");
        assertEquals("value", s.get("key"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SISMEMBER                                                 */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void testSIsMemberNonExistentKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(0, s.sismember("key", "value"));
    }

    @Test
    public void testSIsMemberExistentKeyExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "value");
        assertEquals(1, s.sismember("key", "value"));
    }

    @Test
    public void testSIsMemberExistentKeyExistentObjectDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            HashSet oldSet = ((HashSet) o);
            s.sismember("key", "value");
            HashSet newSet = (HashSet) s.get("key");
            assertEquals(oldSet, newSet);
        } else {
            fail();
        }
    }

    @Test
    public void testSIsMemberExistentKeyNonExistentObjectReturnValue() {
        Storage s = new Storage();
        s.sadd("key", "value");
        assertEquals(0, s.sismember("key", "value2"));
    }

    @Test
    public void testSIsMemberExistentKeyNonExistentObjectDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        Object o = s.get("key");
        if (o instanceof HashSet) {
            HashSet oldSet = ((HashSet) o);
            s.sismember("key", "value2");
            HashSet newSet = (HashSet) s.get("key");
            assertEquals(oldSet, newSet);
        } else {
            fail();
        }
    }

    @Test
    public void testSIsMemberExistentKeyNotSetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(-1, s.sismember("key", "value"));
    }

    @Test
    public void testSIsMemberExistentKeyNotSetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.sismember("key", "value");
        assertEquals("value", s.get("key"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SMEMBERS                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sMembersOnNonExistingKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.smembers("key").toArray());
    }

    @Test
    public void sMembersNotASet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(null, s.smembers("key"));
    }

    @Test
    public void sMembersOnEmptySet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", new HashSet<>());
        assertArrayEquals(new Object[]{}, s.smembers("key").toArray());
    }

    @Test
    public void sMembersOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        String[] expected = new String[]{"0","1", "2", "3","4"};
        List<Object> range = s.smembers("key");
        assertArrayEquals(expected, range.toArray());
    }

    @Test
    public void sMembersOnSetDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        s.smembers("key");
        String[] expected = new String[]{"0", "1", "2", "3", "4"};
        Object o = s.get("key");
        if (o instanceof HashSet) {
            assertArrayEquals(expected, ((HashSet) o).toArray());
        } else {
            fail();
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SINTER                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sInterOnNonExistingKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sinter(new String[]{"key"}).toArray());
    }

    @Test
    public void sInterOnNoKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sinter(new String[]{}).toArray());
    }

    @Test
    public void sInterNotASet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("keyset", "value");
        s.store("key", "value");
        assertEquals(null, s.sinter(new String[]{"key", "keyset"}));
    }

    @Test
    public void sInterOnEmptySet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        s.store("key2", new HashSet<>());

        assertArrayEquals(new Object[]{}, s.sinter(new String[]{"key", "key2"}).toArray());
    }

    @Test
    public void sInterOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        String[] expected = new String[]{"0","1", "2", "3"};
        List<Object> inter = s.sinter(new String[] {"key", "key2"});
        assertArrayEquals(expected, inter.toArray());
    }

    @Test
    public void sInterOnSetDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        Object oldSet1 = s.get("key");
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        Object oldSet2 = s.get("key2");
        s.sinter(new String[] {"key", "key2"});
        assertEquals(oldSet1, s.get("key"));
        assertEquals(oldSet2, s.get("key2"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                               TESTS SINTERSTORE                                                */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sInterStoreOnNonExistingKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(1, s.sinterstore(new String[]{"key", "key2"}));
    }

    @Test
    public void sInterStoreOnNonExistingKeyDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sinterstore(new String[]{"key", "key2"});
        assertEquals(new HashSet<>(), s.get("key"));
    }

    @Test
    public void sInterStoreNotASetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        assertEquals(-1, s.sinterstore(new String[]{"key", "key2", "keyset"}));
    }

    @Test
    public void sInterStoreNotASetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        s.sinterstore(new String[]{"key", "key2", "keyset"});
        thrown.expect(NonExistentKeyException.class);
        s.get("key");
    }

    @Test
    public void sInterStoreOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        assertEquals(1, s.sinterstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void sInterStoreOnSetDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }

        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sinter(new String[] {"key", "key2"}));
        s.sinterstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }

    @Test
    public void sInterStoreOnSetExistentDestKeyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        s.store("newkey", "value");
        assertEquals(1, s.sinterstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void sInterStoreOnSetExistentDestKeyDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sinter(new String[] {"key", "key2"}));
        s.store("newkey", "value");
        s.sinterstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SPOP                                                    */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sPopNonExistentKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(null, s.spop("key"));
    }

    @Test
    public void sPopExistentKeyNotSetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(null, s.spop("key"));
    }

    @Test
    public void sPopExistentKeyNotSetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.spop("key");
        assertEquals("value", s.get("key"));
    }

    @Test
    public void sPopExistentKeyEmptySetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", new HashSet<>());
        assertEquals(null, s.spop("key"));
    }

    @Test
    public void sPopExistentKeyEmptySetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", new HashSet<>());
        s.spop("key");
        assertEquals(new HashSet<>(), s.get("key"));
    }

    @Test
    public void sPopExistentKeyNonEmptySetReturnValue() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        HashSet<Object> oldSet = new HashSet<>();
        oldSet.addAll((HashSet) s.get("key"));
        assertEquals(true, oldSet.contains(s.spop("key")));
    }

    @Test
    public void sPopExistentKeyNonEmptySetDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        for (int i = 0; i < 1000; i++) {
            s.sadd("key", i);
        }
        Object o = s.spop("key");
        assertEquals(false, ((HashSet) s.get("key")).contains(o));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SRANDMEMBER                                             */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sRandMemberNonExistentKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(null, s.srandmember("key"));
    }

    @Test
    public void sRandMemberExistentKeyNotSetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        assertEquals(null, s.srandmember("key"));
    }

    @Test
    public void sRandMemberExistentKeyNotSetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", "value");
        s.srandmember("key");
        assertEquals("value", s.get("key"));
    }

    @Test
    public void sRandMemberExistentKeyEmptySetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key", new HashSet<>());
        assertEquals(null, s.srandmember("key"));
    }

    @Test
    public void sRandMemberExistentKeyEmptySetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key", new HashSet<>());
        s.srandmember("key");
        assertEquals(new HashSet<>(), s.get("key"));
    }

    @Test
    public void sRandMemberExistentKeyNonEmptySetReturnValue() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        assertEquals(true, ((HashSet) s.get("key")).contains(s.srandmember("key")));
    }

    @Test
    public void sRandMemberExistentKeyNonEmptySetDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        for (int i = 0; i < 1000; i++) {
            s.sadd("key", i);
        }
        Object o = s.srandmember("key");
        assertEquals(true, ((HashSet) s.get("key")).contains(o));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SMOVE                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sMoveFirstKeyNonExistentReturnValue() {
        Storage s = new Storage();
        s.sadd("dstkey", "value");
        assertEquals(0, s.smove("srckey", "dstkey", "member"));
    }

    @Test
    public void sMoveFirstKeyNonExistentDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("dstkey", "value");
        s.smove("srckey", "dstkey", "member");
        HashSet set = (HashSet) s.get("dstkey");
        assertEquals(true, set.contains("value") && !set.contains("member"));
    }

    @Test
    public void sMoveFirstKeyNotASetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("srckey", "value");
        s.sadd("dstkey", "a");
        assertEquals(-1, s.smove("srckey", "dstkey", "value"));
    }

    @Test
    public void sMoveFirstKeyNotASetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("srckey", "value");
        s.sadd("dstkey", "a");
        s.smove("srckey", "dstkey", "value");
        Object o1 = s.get("srckey");
        HashSet o2 = (HashSet) s.get("dstkey");
        assertEquals(true, o1.equals("value") && !o2.contains("value"));
    }

    @Test
    public void sMoveSecondKeyNonExistentReturnValue() {
        Storage s = new Storage();
        s.sadd("srckey", "value");
        assertEquals(1, s.smove("srckey", "dstkey", "value"));
    }

    @Test
    public void sMoveSecondKeyNonExistentDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("srckey", "value");
        s.smove("srckey", "dstkey", "value");
        HashSet s1 = (HashSet) s.get("srckey");
        HashSet s2 = (HashSet) s.get("dstkey");
        assertEquals(true, !s1.contains("value") && s2.contains("value"));
    }

    @Test
    public void sMoveSecondKeyNotASetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("dstkey", "a");
        s.sadd("srckey", "value");
        assertEquals(-1, s.smove("srckey", "dstkey", "value"));
    }

    @Test
    public void sMoveSecondKeyNotASetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("srckey", "a");
        s.store("dstkey", "value");
        s.smove("srckey", "dstkey", "value");
        HashSet o1 = (HashSet) s.get("srckey");
        Object o2 = s.get("dstkey");
        assertEquals(true, !o1.contains("value") && o2.equals("value"));
    }

    @Test
    public void sMoveBothKeyOKContainsMemberReturnValue() {
        Storage s = new Storage();
        s.sadd("srckey", "member");
        s.sadd("dstkey", "not_member");
        assertEquals(1, s.smove("srckey", "dstkey", "member"));
    }

    @Test
    public void sMoveBothKeyOKContainsMemberDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("srckey", "member");
        s.sadd("dstkey", "not_member");
        s.smove("srckey", "dstkey", "member");
        HashSet s1 = (HashSet) s.get("srckey");
        HashSet s2 = (HashSet) s.get("dstkey");
        assertEquals(true, !s1.contains("member") && s2.contains("member"));
    }

    @Test
    public void sMoveBothKeyOKDoesNotContainsMemberReturnValue() {
        Storage s = new Storage();
        s.sadd("srckey", "not_member");
        s.sadd("dstkey", "not_member");
        assertEquals(0, s.smove("srckey", "dstkey", "member"));
    }

    @Test
    public void sMoveBothKeyOKDoesNotContainsMemberDoesNotModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sadd("srckey", "not_member");
        s.sadd("dstkey", "not_member");
        s.smove("srckey", "dstkey", "member");
        HashSet s1 = (HashSet) s.get("srckey");
        HashSet s2 = (HashSet) s.get("dstkey");
        assertEquals(true, !s1.contains("member") && !s2.contains("member"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SUNION                                                  */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sUnionOnNonExistingKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sunion(new String[]{"key"}).toArray());
    }

    @Test
    public void sUnionOnNoKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sunion(new String[]{}).toArray());
    }

    @Test
    public void sUnionNotASet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("keyset", "value");
        s.store("key", "value");
        assertEquals(null, s.sunion(new String[]{"key", "keyset"}));
    }

    @Test
    public void sUnionOnEmptySet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        s.store("key2", new HashSet<>());

        assertArrayEquals(new Object[]{"value"}, s.sunion(new String[]{"key", "key2"}).toArray());
    }

    @Test
    public void sUnionOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len + 1; i++) {
            s.sadd("key2", "" + i);
        }
        String[] expected = new String[]{"0","1", "2", "3", "4", "5"};
        List<Object> inter = s.sunion(new String[] {"key", "key2"});
        assertArrayEquals(expected, inter.toArray());
    }

    @Test
    public void sUnionOnSetDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        Object oldSet1 = s.get("key");
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        Object oldSet2 = s.get("key2");
        s.sunion(new String[] {"key", "key2"});
        assertEquals(oldSet1, s.get("key"));
        assertEquals(oldSet2, s.get("key2"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SUNIONSTORE                                               */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sUnionStoreOnNonExistingKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(1, s.sunionstore(new String[]{"key", "key2"}));
    }

    @Test
    public void sUnionStoreOnNonExistingKeyDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sunionstore(new String[]{"key", "key2"});
        assertEquals(new HashSet<>(), s.get("key"));
    }

    @Test
    public void sUnionStoreNotASetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        assertEquals(-1, s.sunionstore(new String[]{"key", "key2", "keyset"}));
    }

    @Test
    public void sUnionStoreNotASetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        s.sunionstore(new String[]{"key", "key2", "keyset"});
        thrown.expect(NonExistentKeyException.class);
        s.get("key");
    }

    @Test
    public void sUnionStoreOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        assertEquals(1, s.sunionstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void sUnionStoreOnSetDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }

        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sinter(new String[] {"key", "key2"}));
        s.sinterstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }

    @Test
    public void sUnionStoreOnSetExistentDestKeyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        s.store("newkey", "value");
        assertEquals(1, s.sunionstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void SUnionStoreOnSetExistentDestKeyDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sunion(new String[] {"key", "key2"}));
        s.store("newkey", "value");
        s.sunionstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                  TESTS SDIFF                                                   */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void SDiffOnNonExistingKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sdiff(new String[]{"key"}).toArray());
    }

    @Test
    public void SDiffOnNoKey() {
        Storage s = new Storage();
        assertArrayEquals(new Object[]{}, s.sdiff(new String[]{}).toArray());
    }

    @Test
    public void SDiffNotASet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("keyset", "value");
        s.store("key", "value");
        assertEquals(null, s.sdiff(new String[]{"key", "keyset"}));
    }

    @Test
    public void SDiffOnEmptySet() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.sadd("key", "value");
        s.store("key2", new HashSet<>());

        assertArrayEquals(new Object[]{"value"}, s.sdiff(new String[]{"key", "key2"}).toArray());
    }

    @Test
    public void SDiffOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len + 1; i++) {
            s.sadd("key2", "" + i);
        }
        String[] expected = new String[]{};
        List<Object> inter = s.sdiff(new String[] {"key", "key2"});
        assertArrayEquals(expected, inter.toArray());
    }

    @Test
    public void SDiffOnSetDoesNotModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        Object oldSet1 = s.get("key");
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        Object oldSet2 = s.get("key2");
        s.sdiff(new String[] {"key", "key2"});
        assertEquals(oldSet1, s.get("key"));
        assertEquals(oldSet2, s.get("key2"));
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*                                                                                                                */
    /*                                                TESTS SDIFFSTORE                                                */
    /*                                                                                                                */
    /*----------------------------------------------------------------------------------------------------------------*/

    @Test
    public void sDiffStoreOnNonExistingKeyReturnValue() {
        Storage s = new Storage();
        assertEquals(1, s.sdiffstore(new String[]{"key", "key2"}));
    }

    @Test
    public void sDiffStoreOnNonExistingKeyDoesModify() throws NonExistentKeyException {
        Storage s = new Storage();
        s.sdiffstore(new String[]{"key", "key2"});
        assertEquals(new HashSet<>(), s.get("key"));
    }

    @Test
    public void sDiffStoreNotASetReturnValue() throws DuplicatedKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        assertEquals(-1, s.sdiffstore(new String[]{"key", "key2", "keyset"}));
    }

    @Test
    public void sDiffStoreNotASetDoesNotModify() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        s.store("key2", "value");
        s.sadd("keyset", "value");
        s.sdiffstore(new String[]{"key", "key2", "keyset"});
        thrown.expect(NonExistentKeyException.class);
        s.get("key");
    }

    @Test
    public void sDiffStoreOnSetReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        assertEquals(1, s.sdiffstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void sDiffStoreOnSetDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }

        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sdiff(new String[] {"key", "key2"}));
        s.sdiffstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }

    @Test
    public void sDiffStoreOnSetExistentDestKeyReturnValue() throws DuplicatedKeyException, NonExistentKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        s.store("newkey", "value");
        assertEquals(1, s.sdiffstore(new String[] {"newkey", "key", "key2"}));
    }

    @Test
    public void sDiffStoreOnSetExistentDestKeyDoesModify() throws NonExistentKeyException, DuplicatedKeyException {
        Storage s = new Storage();
        int len = 5;
        for (int i = 0; i < len; i++) {
            s.sadd("key", "" + i);
        }
        for (int i = 0; i < len - 1; i++) {
            s.sadd("key2", "" + i);
        }
        HashSet<Object> expected = new HashSet<>();
        expected.addAll(s.sdiff(new String[] {"key", "key2"}));
        s.store("newkey", "value");
        s.sdiffstore(new String[]{"newkey", "key", "key2"});
        assertEquals(expected, s.get("newkey"));
    }
}