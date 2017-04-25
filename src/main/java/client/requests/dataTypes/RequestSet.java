package client.requests.dataTypes;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSet extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The value held by the key we're going to set.
     */
    private Object o;

    /**
     * Constructor.
     * @param tokens The different words of the request, e.g {"set", "key", "value"}.
     * @throws NoTokensException When no tokens are provided to the request.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public RequestSet(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the value held by the key we're going to set.
     * @return The value held by the key we're going to set.
     */
    public Object getObject() {
        return o;
    }

    /**
     * Parse the tokens and retrieve both the key and the value.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        o = tokens.get(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "set(\"" + key + "\", " + o + ")";
    }
}
