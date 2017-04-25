package client.requests;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class RequestNameTest {
    RequestName r;

    @Before
    public void init() {
        r = RequestName.getInstance();
    }

    @Test
    public void getAllCmds() throws Exception {
        HashMap<RequestName.Cmd, String> h = r.getAllCmds();
        String[] expected = {
                "HELP",
                "QUIT",
                "EXIT",
                "SET_SERVER",
                "GET",
                "SET",
                "DEL",
                "TYPE",
                "INCR",
                "INCRBY",
                "DECR",
                "DECRBY"
        };
        for (String s : expected) {
            if (!h.containsValue(s)) {
                fail("Does not contain the cmd \"" + s + "\"");
            }
        }
    }

    @Test
    public void getQuitCmd() throws Exception {
        assertEquals("QUIT", r.getQuitCmd());
    }

    @Test
    public void getExitCmd() throws Exception {
        assertEquals("EXIT", r.getExitCmd());
    }

    @Test
    public void getHelpCmd() throws Exception {
        assertEquals("HELP", r.getHelpCmd());
    }

    @Test
    public void getSetServerCmd() throws Exception {
        assertEquals("SET_SERVER", r.getSetServerCmd());
    }

    @Test
    public void getGetCmd() throws Exception {
        assertEquals("GET", r.getGetCmd());
    }

    @Test
    public void getSetCmd() throws Exception {
        assertEquals("SET", r.getSetCmd());
    }

    @Test
    public void getTypeCmd() throws Exception {
        assertEquals("TYPE", r.getTypeCmd());
    }

    @Test
    public void getDecrCmd() throws Exception {
        assertEquals("DECR", r.getDecrCmd());
    }

    @Test
    public void getDecrByCmd() throws Exception {
        assertEquals("DECRBY", r.getDecrByCmd());
    }

    @Test
    public void getIncrCmd() throws Exception {
        assertEquals("INCR", r.getIncrCmd());
    }

    @Test
    public void getIncrByCmd() throws Exception {
        assertEquals("INCRBY", r.getIncrByCmd());
    }

    @Test
    public void getDelCmd() throws Exception {
        assertEquals("DEL", r.getDelCmd());
    }
}