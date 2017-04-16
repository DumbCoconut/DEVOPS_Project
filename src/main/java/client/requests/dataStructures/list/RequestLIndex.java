package client.requests.dataStructures.list;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestLIndex extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The index we want the value of.
     */
    private String index;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"lindex", "key", "index"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestLIndex(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the index we want the value of..
     * @return the index we want the value of.
     */
    public String getIndex() {
        return index;
    }

    /**
     * Parse the tokens and retrieve both the key and the index.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        index = tokens.get(2);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return "lindex(\"" + key + "\", " + index + ")";
    }

}
