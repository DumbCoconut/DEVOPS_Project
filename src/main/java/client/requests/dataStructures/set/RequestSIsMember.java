package client.requests.dataStructures.set;

import client.requests.RequestWithKey;
import client.requests.exceptions.InvalidNbArgException;
import client.requests.exceptions.NoTokensException;

import java.util.ArrayList;

public class RequestSIsMember extends RequestWithKey {
    /**
     * The minimum number of arguments required to properly construct the request.
     */
    private final int minNbArgs = 2;

    /**
     * The member whose presence in this set is to be tested
     */
    private String member;

    /**
     * Constructor.
     *
     * @param tokens The different words of the request, e.g {"ismember", "key", "member"}.
     * @throws InvalidNbArgException When not enough arguments are provided to the request.
     * @throws NoTokensException     When no tokens are provided to the request.
     */
    public RequestSIsMember(ArrayList<String> tokens) throws InvalidNbArgException, NoTokensException {
        super(tokens);
        setNbArgs(minNbArgs);
        parse();
    }

    /**
     * Get the member whose presence in this set is to be tested
     * @return The member whose presence in this set is to be tested
     */
    public String getMember() {
        return member;
    }

    /**
     * Parse the tokens and retrieve both the key and the member.
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
        return "sismember(\"" + key + "\", " + member + ")";
    }
}
