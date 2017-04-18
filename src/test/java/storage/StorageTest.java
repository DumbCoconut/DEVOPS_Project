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
}