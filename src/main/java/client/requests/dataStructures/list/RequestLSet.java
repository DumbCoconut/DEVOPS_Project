package client.requests.dataStructures.list;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestLSET extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 3;

    /**
     * The index in the list where to set the value.
     */
    private String index;

    /**
     * The value to set.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"lset", "key", "index", "value"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestLSET(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the index in the list where to set the value.
     * @return The index in the list where to set the value.
     */
    public String getIndex() {
        return index;
    }

    /**
     * Get the value to set.
     * @return The value to set.
     */
    public String getValue() {
        return value;
    }

    /**
     * Parse the tokens and retrieve both the key and the string.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        index = tokens.get(2);
        value = tokens.get(3);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "lset(\"" + key + "\", " + index + ", " + value + ")";
    }
}
