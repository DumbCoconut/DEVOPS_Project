package client;

import org.junit.Before;
import org.junit.Test;
import server.RedisLikeServer;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;

public class ClientTest {
    private Client c;
    private RedisLikeServer s;

    @Before
    public void init() {
        c = new Client();
    }

    @Test
    public void testSplitTokensNoQuotes() {
        ArrayList<String> tokens = c.splitIntoTokens("token1 3");
        String[] expected = {
                "token1",
                "3"
        };
        assertArrayEquals(expected, tokens.toArray());
    }

    @Test
    public void testSplitTokensQuotes() {
        ArrayList<String> tokens = c.splitIntoTokens("token1 \"one sentence\" \"another one\" 5");
        String[] expected = {
                "token1",
                "one sentence",
                "another one",
                "5"
        };
        assertArrayEquals(expected, tokens.toArray());
    }
}