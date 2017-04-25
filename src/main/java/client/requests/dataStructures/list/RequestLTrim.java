package client.requests.dataStructures.list;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestLTrim extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 3;

    /**
     * The start of the sub-list.
     */
    private String start;

    /**
     * The end of the sub-list.
     */
    private String end;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"ltrim", "key", "start", "end"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestLTrim(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the start of the sub-list.
     * @return The start of the sub-list.
     */
    public String getStart() {
        return start;
    }

    /**
     * Get the end of the sub-list.
     * @return The end of the sub-list.
     */
    public String getEnd() {
        return end;
    }

    /**
     * Parse the tokens and retrieve both the key and the string.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        start = tokens.get(2);
        end = tokens.get(3);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ltrim(\"" + key + "\", " + start + ", " + end + ")";
    }
}
