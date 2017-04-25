package client.requests.dataStructures.list;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestLPush extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The string we want to push into the list.
     */
    private String string;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"lpush", "key", "string"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestLPush(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the string we want to push into the list.
     * @return The string we want to push into the list.
     */
    public String getString() {
        return string;
    }

    /**
     * Parse the tokens and retrieve both the key and the string.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        string = tokens.get(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "lpush(\"" + key + "\", " + string + ")";
    }
}