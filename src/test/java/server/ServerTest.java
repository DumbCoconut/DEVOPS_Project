package server;

import org.apache.commons.cli.ParseException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ServerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
}