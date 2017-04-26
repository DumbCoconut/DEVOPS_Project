package client.requests.dataTypes;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestDecrBy extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The integer we're going to decrement the key by.
     */
    private String integer;

    /**
     * Constructor.
     * @param tokens The different words of the request, e.g {"decrby", "key", "3"}.
     * @throws NoTokensException When no tokens are provided to the request.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public RequestDecrBy(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the integer we're going to decrement the key by.
     * @return the integer we're going to decrement the key by.
     */
    public String getInteger() {
        return integer;
    }

    /**
     * Parse the tokens and retrieve both the key and the integer.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        integer = tokens.get(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "decrby(\"" + key + "\", " + integer + ")";
    }
}
