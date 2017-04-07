package server;

import org.apache.commons.cli.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ServerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String defaultKey = "key";
    private String nonExistentKey = "non_existent_key";
    private String defaultString = "The sweatpants years.";
    private int defaultInt = 10;
    private int defaultIncDec = 3;

    /* Create a server and store nbObjects in it. */
    private Server createAndPopulateServer(int nbObjects) {
        Server s = new Server();
        for(int i = 0; i < nbObjects; i++) {
            s.set("key" + (i + 1), "object" + (i + 1));
        }
        return s;
    }

    /* Create a server and store a string in it. */
    private Server createAndStoreDefaultString() {
        Server s = new Server();
        s.set(defaultKey, defaultString);
        return s;
    }

    /* Create a server and store an int in it. */
    private Server createAndStoreDefaultInt() {
        Server s = new Server();
        s.set(defaultKey, defaultInt);
        return s;
    }

    @Test
    public void serverWithoutParsing() {
        Server s = new Server();
        assertEquals(s.getPort(), Server.DEFAULT_PORT);
        assertEquals(s.getName(), "server_" + (Server.NUM_SERV - 1));
    }

    @Test
    public void twoServerNamesWithoutParsing() {
        Server s = new Server();
        Server s2 = new Server();
        assertNotEquals(s.getName(), s2.getName());
    }

    @Test
    public void serverNameParserShortOpt() throws ParseException {
        Server s = new Server();
        String name = "Ladies and gentlemen, allow me to present, Man Not Caring.";
        s.parse(new String[]{"-n", name});
        assertEquals(s.getName(), name);
    }

    @Test
    public void serverNameParserLongOpt() throws ParseException {
        Server s = new Server();
        String name = "Ladies and gentlemen, allow me to present, Man Not Caring.";
        s.parse(new String[]{"--name", name});
        assertEquals(s.getName(), name);
    }

    @Test
    public void serverPortParserShortOpt() throws ParseException {
        Server s = new Server();
        String port = "118218";
        s.parse(new String[]{"-p", port});
        assertEquals(s.getPort(), Integer.parseInt(port));
    }

    @Test
    public void serverPortParserLongOpt() throws ParseException {
        Server s = new Server();
        String port = "118218";
        s.parse(new String[]{"--port", port});
        assertEquals(s.getPort(), Integer.parseInt(port));
    }

    @Test
    public void serverWithParserInvalidPort() throws ParseException {
        Server s = new Server();
        String port = "What is it with steel wool?";
        s.parse(new String[]{"-p", port});
        assertEquals(s.getPort(), Server.DEFAULT_PORT);
    }

    @Test
    public void failParse() throws ParseException {
        thrown.expect(ParseException.class);
        Server s = new Server();
        s.parse(new String[]{"-A drunk clown hurt me once."});
    }

    @Test
    public void getWithExistentKey() {
        Server s = createAndPopulateServer(2);
        assertEquals(s.get("key1"), "object1");
    }

    @Test
    public void getWithNonExistentKey() {
        Server s = createAndPopulateServer(2);
        assertEquals(s.get(nonExistentKey), null);
    }

    @Test
    public void setWithExistentKey() {
        Server s = createAndPopulateServer(2);
        String newValue = "Oh, my God! I look like Alice Cooper!";
        s.set("key2", newValue);
        assertEquals(s.get("key2"), newValue);
    }

    @Test
    public void setWithNonExistentKey() {
        Server s = createAndPopulateServer(2);
        s.set("key3", "object3");
        assertEquals(s.get("key3"), "object3");
    }

    @Test
    public void typeWithExistentKey() {
        Server s = new Server();
        s.set("key", 3);
        assertEquals(s.type("key"), "Integer");
    }

    @Test
    public void typeWithNonExistentKey() {
        Server s = new Server();
        assertEquals(s.type(nonExistentKey), "none");
    }

    @Test
    public void decrWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.decr(defaultKey);
        assertEquals(s.get(defaultKey), defaultInt - 1);
    }

    @Test
    public void decrWithInvalidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.decr(defaultKey);
        assertEquals(s.get(defaultKey), -1);
    }

    @Test
    public void decrWithNonExistentKey() {
        Server s = new Server();
        s.decr(defaultKey);
        assertEquals(s.get(defaultKey), -1);
    }

    @Test
    public void decrByWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), defaultInt - defaultIncDec);
    }

    @Test
    public void decrByWithInValidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), -defaultIncDec);
    }

    @Test
    public void decrByWithNonExistentKey() {
        Server s = new Server();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), -defaultIncDec);
    }

    @Test
    public void incrWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.incr(defaultKey);
        assertEquals(s.get(defaultKey), defaultInt + 1);
    }

    @Test
    public void incrWithInvalidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.incr(defaultKey);
        assertEquals(s.get(defaultKey), 1);
    }

    @Test
    public void incrWithNonExistentKey() {
        Server s = new Server();
        s.incr(defaultKey);
        assertEquals(s.get(defaultKey), 1);
    }

    @Test
    public void incrByWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), defaultInt + defaultIncDec);
    }

    @Test
    public void incrByWithInValidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), defaultIncDec);
    }

    @Test
    public void incrByWithNonExistentKey() {
        Server s = new Server();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(s.get(defaultKey), defaultIncDec);
    }

    @Test
    public void delExistentKey() {
        Server s = createAndPopulateServer(2);
        boolean hasBeenDeleted = s.del("key1");
        assertEquals(hasBeenDeleted, true);
        assertEquals(s.get("key1"), null);
    }

    @Test
    public void delNonExistentKey() {
        Server s = createAndPopulateServer(2);
        boolean hasBeenDeleted = s.del(nonExistentKey);
        assertEquals(hasBeenDeleted, false);
    }
}