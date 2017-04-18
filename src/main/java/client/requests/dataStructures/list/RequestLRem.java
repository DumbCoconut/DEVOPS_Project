package client.requests.dataStructures.list;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestLREM extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 3;

    /**
     * The number of occurrences to remove.
     */
    private String count;

    /**
     * The value to remove.
     */
    private String value;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"lrem", "key", "count", "value"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestLREM(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the number of occurrences to remove.
     * @return The number of occurrences to remove.
     */
    public String getCount() {
        return count;
    }

    /**
     * Get the value to remove.
     * @return The value to remove.
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
        count = tokens.get(2);
        value = tokens.get(3);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "lrem(\"" + key + "\", " + count + ", " + value + ")";
    }
}
