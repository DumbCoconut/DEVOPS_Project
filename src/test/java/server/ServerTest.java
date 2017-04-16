package server;

import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;

import static org.junit.Assert.*;

public class ServerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final String EOL = System.getProperty("line.separator");

    private ByteArrayOutputStream testOutput;

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

    @Before
    public void setUp() throws Exception {
        testOutput = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() throws IOException {
        testOutput.close();
    }

    @Test
    public void serverWithoutParsing() throws RemoteException {
        Server s = new Server();
        assertEquals(Server.DEFAULT_PORT, s.getPort());
        assertEquals(Server.DEFAULT_NAME, s.getName());
    }

    @Test
    public void serverNameParserShortOpt() throws ParseException, RemoteException {
        Server s = new Server();
        String name = "Ladies and gentlemen, allow me to present, Man Not Caring.";
        s.parse(new String[]{"-n", name});
        assertEquals(name, s.getName());
    }

    @Test
    public void serverNameParserLongOpt() throws ParseException, RemoteException {
        Server s = new Server();
        String name = "Ladies and gentlemen, allow me to present, Man Not Caring.";
        s.parse(new String[]{"--name", name});
        assertEquals(name, s.getName());
    }

    @Test
    public void serverNameParserShouldNotExit() throws ParseException {
        assertEquals(false, new Server().parse(new String[]{"--name", "name"}));
    }

    @Test
    public void serverPortParserShortOpt() throws ParseException {
        Server s = new Server();
        String port = "118218";
        s.parse(new String[]{"-p", port});
        assertEquals(Integer.parseInt(port), s.getPort());
    }

    @Test
    public void serverPortParserLongOpt() throws ParseException {
        Server s = new Server();
        String port = "118218";
        s.parse(new String[]{"--port", port});
        assertEquals(Integer.parseInt(port), s.getPort());
    }

    @Test
    public void serverWithParserInvalidPort() throws ParseException {
        Server s = new Server();
        String port = "What is it with steel wool?";
        s.parse(new String[]{"-p", port});
        assertEquals(Server.DEFAULT_PORT, s.getPort());
    }

    @Test
    public void serverPortParserShouldNotExit() throws ParseException {
        assertEquals(false, new Server().parse(new String[]{"--port", "999"}));
    }

    @Test
    public void helpPrintedToStdOut() {
        try {
            System.setOut(new PrintStream(testOutput));
            new Server().help();
        } finally {
            System.setOut(System.out);
        }
        assertEquals("Options:" + EOL
                        + "\t-h\t--help\tDisplay this information." + EOL
                        + "\t-n\t--name\tSet the name of this server." + EOL
                        + "\t-p\t--port\tSet the port of this server." + EOL,
                     testOutput.toString());
    }

    @Test
    public void parseOnlyHelpShouldPrint() throws ParseException {
        System.setOut(new PrintStream(testOutput)); // To throw away the display
        assertEquals(true, new Server().parse(new String[]{"--help"}));
    }

    @Test
    public void parseWithHelpShouldPrint() throws ParseException {
        System.setOut(new PrintStream(testOutput)); // To throw away the display
        assertEquals(true, new Server().parse(new String[]{"--name", "jean", "--help"}));
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
        assertEquals("object1", s.get("key1"));
    }

    @Test
    public void getWithNonExistentKey() {
        Server s = createAndPopulateServer(2);
        assertEquals(null, s.get(nonExistentKey));
    }

    @Test
    public void setWithExistentKey() {
        Server s = createAndPopulateServer(2);
        String newValue = "Oh, my God! I look like Alice Cooper!";
        s.set("key2", newValue);
        assertEquals(newValue, s.get("key2"));
    }

    @Test
    public void setWithNonExistentKey() {
        Server s = createAndPopulateServer(2);
        s.set("key3", "object3");
        assertEquals("object3", s.get("key3"));
    }

    @Test
    public void typeWithExistentKey() {
        Server s = new Server();
        s.set("key", 3);
        assertEquals("Integer", s.type("key"));
    }

    @Test
    public void typeWithNonExistentKey() {
        Server s = new Server();
        assertEquals("none", s.type(nonExistentKey));
    }

    @Test
    public void decrWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.decr(defaultKey);
        assertEquals(defaultInt - 1, s.get(defaultKey));
    }

    @Test
    public void decrWithInvalidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.decr(defaultKey);
        assertEquals(-1, s.get(defaultKey));
    }

    @Test
    public void decrWithNonExistentKey() {
        Server s = new Server();
        s.decr(defaultKey);
        assertEquals(-1, s.get(defaultKey));
    }

    @Test
    public void decrByWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(defaultInt - defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void decrByWithInValidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(-defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void decrByWithNonExistentKey() {
        Server s = new Server();
        s.decrBy(defaultKey, defaultIncDec);
        assertEquals(-defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void incrWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.incr(defaultKey);
        assertEquals(defaultInt + 1, s.get(defaultKey));
    }

    @Test
    public void incrWithInvalidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.incr(defaultKey);
        assertEquals(1, s.get(defaultKey));
    }

    @Test
    public void incrWithNonExistentKey() {
        Server s = new Server();
        s.incr(defaultKey);
        assertEquals(1, s.get(defaultKey));
    }

    @Test
    public void incrByWithValidExistentKey() {
        Server s = createAndStoreDefaultInt();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(defaultInt + defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void incrByWithInValidExistentKey() {
        Server s = createAndStoreDefaultString();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void incrByWithNonExistentKey() {
        Server s = new Server();
        s.incrBy(defaultKey, defaultIncDec);
        assertEquals(defaultIncDec, s.get(defaultKey));
    }

    @Test
    public void delExistentKey() {
        Server s = createAndPopulateServer(2);
        boolean hasBeenDeleted = s.del("key1");
        assertEquals(true, hasBeenDeleted);
        assertEquals(null, s.get("key1"));
    }

    @Test
    public void delNonExistentKey() {
        Server s = createAndPopulateServer(2);
        boolean hasBeenDeleted = s.del(nonExistentKey);
        assertEquals(false, hasBeenDeleted);
    }
}