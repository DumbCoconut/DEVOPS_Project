package client.requests.dataStructures.set;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSAdd extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The member we want to put into the set.
     */
    private String member;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"sadd", "key", "member"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestSAdd(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the member we want to put into the set.
     * @return The member we want to put into the set.
     */
    public String getMember() {
        return member;
    }

    /**
     * Parse the tokens and retrieve both the key and the string.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     */
    public void parse() throws InvalidNbArgException {
        if (tokens.size() != nbExpectedTokens()) {
            throw new InvalidNbArgException(tokens.size() - 1, minNbArgs);
        }
        member = tokens.get(2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "sadd(\"" + key + "\", " + member + ")";
    }
}
